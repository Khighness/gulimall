package top.parak.gulimall.product.web;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import top.parak.gulimall.product.entity.CategoryEntity;
import top.parak.gulimall.product.service.CategoryService;
import top.parak.gulimall.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-12-09
 */
@Slf4j
@Controller
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redisson;

    @GetMapping(path = {"/", "index.html"})
    public String indexPage(Model model) {

        // 1. 查出所有的一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categories();
        model.addAttribute("categorys", categoryEntities);

        // 默认前缀prefix：classpath:/templates/
        // 默认后缀suffix：.html
        // 视图解析器拼串：prefix + 返回值 + suffix
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Object getCatalogJson() {
        Map<String, List<Catelog2Vo>> catalogJson = categoryService.getCatalogJsonDbWithSpringCache();

        return catalogJson;
    }

    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        // redisson使用流程
        // 1. 获取锁
        // 锁的名称就是key，只要锁的名字一样，就是同一把锁
        RLock lock = redisson.getLock("my-lock");
        try {
            // 2. 加锁
            // 阻塞式等待，默认加的锁都是30S的时间
            // 业务时间超长，锁会自动续期
            // lock.lock();
            // 指定锁的超时时间，10S自动解锁
            // 业务时间超长，锁不会自动续期
            lock.lock(10, TimeUnit.SECONDS);
            // 3. 执行业务
            log.info("「TID: {}」redisson加锁成功，执行业务", Thread.currentThread().getId());
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            log.error("occur exception while sleeping: {}", e.getMessage());
        } finally {
            log.info("「TID: {}」执行业务完毕，redisson解锁", Thread.currentThread().getId());
            // 4. 解锁
            // 即使不手动解锁，也会在30S内自动释放
            lock.unlock();
        }

        // 假设解锁代码没有运行，redisson会不会出现死锁？
        // 实验准备：两个服务，一个运行10000端口，一个运行10001端口
        // 实验步骤：浏览器发送两个请求：localhost:10000/hello localhost:10001/hello
        //         给先抢到锁的服务在睡眠期间断掉，观察未抢到请求的窗口
        // 实验结果：未抢到锁的窗口在等待60秒后打印出Hello

        // 秘密：redisson的看门狗
        // @see org.redisson.api.RLock#lock
        // 1. 锁的自动续期：如果业务超长，运行期间每(30/3=10)S检查一次，给锁续上新的30S，不用担心因为业务时间长，锁自动过期而被删掉。
        // 2. 锁的自动释放：加锁的业务只要运行完成，就不会给当前业务自动续期，即使不手动解锁，锁默认也会在30S自动释放。

        return "Hello!";
    }

    /**
     * 写入
     */
    @ResponseBody
    @GetMapping("/write")
    public String writeValue() {
        // 读写锁
        RReadWriteLock readWriteLock = redisson.getReadWriteLock("rw-lock");
        // 获取写锁
        RLock lock = readWriteLock.writeLock();
        String uuid = UUID.randomUUID().toString();

        try {
            // 加写锁
            lock.lock();
            log.info("「TID: {}」redisson加写锁成功", Thread.currentThread().getId());
            TimeUnit.SECONDS.sleep(30);
            stringRedisTemplate.opsForValue().set("write_value", uuid);
        } catch (InterruptedException e) {
            log.error("occur exception while writing: {}", e.getMessage());
        } finally {
            // 解锁
            lock.unlock();
            log.info("「TID: {}」redisson解开写锁", Thread.currentThread().getId());
        }

        return uuid;
    }

    /**
     * 读取
     *
     * 写锁互斥，读锁共享
     * (1) 读 + 读：相当于无锁
     * (2) 写 + 写：阻塞方式
     * (3) 写 + 读：等待写锁释放
     * (4) 读 + 写：等待写锁释放
     * 只要有写的存在，都必须等待
     */
    @ResponseBody
    @GetMapping("/read")
    public String readValue() {
        // 读写锁
        RReadWriteLock readWriteLock = redisson.getReadWriteLock("rw-lock");
        // 获取读锁
        RLock lock = readWriteLock.readLock();
        String value = null;

        try {
            // 加读锁
            lock.lock();
            log.info("「TID: {}」redisson加读锁成功", Thread.currentThread().getId());
            TimeUnit.SECONDS.sleep(30);
            value = stringRedisTemplate.opsForValue().get("write_value");
        } catch (Exception e) {
            log.error("occur exception while reading: {}", e.getMessage());
        } finally {
            // 解锁
            lock.unlock();
            log.info("「TID: {}」redisson解开读锁", Thread.currentThread().getId());
        }

        return value;
    }

    /**
     * 车库停车
     */
    @ResponseBody
    @GetMapping("/park")
    public String park() throws InterruptedException {
        RSemaphore semaphore = redisson.getSemaphore("park");
        // 获取一个信号量，占一个车位
        // 阻塞获取，一直试探
        // semaphore.acquire();
        // return "OK";
        // 尝试获取，试探一次
        boolean b = semaphore.tryAcquire();
        return "OK => " + b;
    }

    /**
     * 离开车库
     */
    @ResponseBody
    @GetMapping("/go")
    public String go() {
        RSemaphore semaphore = redisson.getSemaphore("park");
        // 释放一个信号量
        semaphore.release();
        return "OK";
    }

    /**
     * 锁门
     * 五个班的人全部走完，锁大门
     */
    @ResponseBody
    @GetMapping("/lockDoor")
    public String lockDoor() throws InterruptedException {
        RCountDownLatch door = redisson.getCountDownLatch("door");
        // 设置5个班
        door.trySetCount(5);
        // 等待闭锁都完成
        door.await();
        return "放假了";
    }

    @ResponseBody
    @GetMapping("/gogogo/{id}")
    public String gogogo(@PathVariable("id") Long id) {
        RCountDownLatch door = redisson.getCountDownLatch("door");
        door.countDown();
        return id + "班的人都走了";
    }

}
