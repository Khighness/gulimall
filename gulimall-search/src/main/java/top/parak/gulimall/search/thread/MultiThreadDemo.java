package top.parak.gulimall.search.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author KHighness
 * @since 2021-12-20
 */
@Slf4j
public class MultiThreadDemo {

    /**
     * <ol>
     * <li>{@code Executors.newSingleThreadExecutor()}core=max=1，只有一个线程
     * <li>{@code Executors.newCachedThreadPool()}     core=0，所有线程都可回收
     * <li>{@code Executors.newFixedThreadPool()}      core=max，核心线程不回收，没有究极线程
     * </ol>
     */
//    private static ExecutorService executorService =
//            new ThreadPoolExecutor(
//                    5,
//                    200,
//                    10L,
//                    TimeUnit.SECONDS,
//                    new LinkedBlockingDeque<>(),
//                    Executors.defaultThreadFactory(),
//                    new ThreadPoolExecutor.AbortPolicy());
    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // P196
//        log.info("主线程启动...");
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            log.info("当前线程: [{}]", Thread.currentThread().getId());
//            int i = 10 / 2;
//            log.info("运行结果: [{}]", i);
//            return i;
//        }, executor);
//        log.info("主线程获取结果：[{}]", future.get());
//        log.info("主线程结束...");

        // P197
//        log.info("主线程启动...");
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            log.info("当前线程: [{}]", Thread.currentThread().getId());
//            int i = 10 / 0;
//            log.info("运行结果: [{}]", i);
//            return i;
//        }, executor).whenComplete((result, throwable) -> {
//            // 虽然感知异常，但是无法修改返回数据
//            log.info("异步线程执行完成" +
//                    "，结果：[" + result + "]" +
//                    "，异常: [" + throwable + "]"
//            );
//        }).exceptionally((throwable -> {
//            // 可以感知异常，同时可以修改返回数据
//            return 10;
//        }));
//        log.info("主线程获取结果：[{}]", future.get());
//        log.info("主线程结束...");

        // P198
//        log.info("主线程启动...");
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            log.info("当前线程: [{}]", Thread.currentThread().getId());
//            int i = 10 / 0;
//            log.info("运行结果: [{}]", i);
//            return i;
//        }, executor).handle((result, throwable) -> {
//            if (result != null) {
//              return 2 * result;
//            }
//            if (throwable != null) {
//                return 0;
//            }
//            return 0;
//        });
//        log.info("主线程获取结果：[{}]", future.get());
//        log.info("主线程结束...");

        // P199
        // 线程串行化
        // thenRunAsync    不能获取到上一步的执行结果
        // thenAcceptAsync 能接收上一步结果，但是无返回值
        // thenApplyAsync  能接收上一步结果，有返回值
//        log.info("主线程启动...");
//        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
//            log.info("当前线程: [{}]", Thread.currentThread().getId());
//            int i = 10 / 2;
//            log.info("运行结果: [{}]", i);
//            return i;
//        }, executor).thenRunAsync(() -> {
//            log.info("任务2启动");
//        }, executor);
//        log.info("主线程获取结果：[{}]", future.get());
//        log.info("主线程结束...");

//        log.info("主线程启动...");
//        CompletableFuture.supplyAsync(() -> {
//            log.info("当前线程: [{}]", Thread.currentThread().getId());
//            int i = 10 / 2;
//            log.info("运行结果: [{}]", i);
//            return i;
//        }, executor).thenAcceptAsync((result) -> {
//            log.info("任务1结果：[{}]", result);
//            log.info("任务2启动");
//        }, executor);
//        log.info("主线程结束...");

        // P200
        // 任务3等待任务1和任务2执行完
//        log.info("主线程启动...");
//        CompletableFuture<Integer> future01 = CompletableFuture.supplyAsync(() -> {
//            log.info("任务1线程: [{}]", Thread.currentThread().getId());
//            int i = 10 / 4;
//            log.info("任务1结束");
//            return i;
//        }, executor);
//        CompletableFuture<Integer> future02 = CompletableFuture.supplyAsync(() -> {
//            log.info("任务2线程: [{}]", Thread.currentThread().getId());
//            int i = 10 / 4;
//            log.info("任务2结束");
//            return i;
//        }, executor);
//        future01.thenAcceptBothAsync(future02, (f1, f2) -> {
//            log.info("任务3线程: [{}]", Thread.currentThread().getId());
//            log.info("获取到，任务1结果：[{}]，任务2结果：[{}]", f1, f2);
//            log.info("任务3结束");
//        }, executor);
//        CompletableFuture<String> future03 = future01.thenCombineAsync(future02, (f1, f2) -> {
//            log.info("任务3线程: [{}]", Thread.currentThread().getId());
//            log.info("任务3结束");
//            return f1 * f2 + " -> hhh";
//        });
//        log.info("主线程结束...");

        // P201
        // 任务1和任务2任意一个完成就执行任务3
        // runAfterEitherAsync 不感知结果，没有返回值
        // acceptEitherAsync   感知结果，没有返回值
        // applyToEitherAsync  感知结果，有返回值
//        log.info("主线程启动...");
//        CompletableFuture<Integer> future01 = CompletableFuture.supplyAsync(() -> {
//            log.info("任务1线程: [{}]", Thread.currentThread().getId());
//            int i = 10 / 4;
//            log.info("任务1结束");
//            return i;
//        }, executor);
//        CompletableFuture<Integer> future02 = CompletableFuture.supplyAsync(() -> {
//            log.info("任务2线程: [{}]", Thread.currentThread().getId());
//            int i = 10 / 4;
//            try {
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                log.error(e.getMessage());
//            }
//            log.info("任务2结束");
//            return i;
//        }, executor);
//        CompletableFuture<Void> future03 = future01.runAfterEitherAsync(future02, () -> {
//            log.info("任务3线程: [{}]", Thread.currentThread().getId());
//            log.info("任务3结束");
//        }, executor);
//        log.info("主线程结束...");

        // P202
        //
        log.info("主线程启动...");
        CompletableFuture<String> future01 = CompletableFuture.supplyAsync(() -> {
            log.info("查询商品的图片信息");
            return "Hello.jpg";
        }, executor);
        CompletableFuture<String> future02 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
            log.info("查询商品的属性");
            return "黑色+256G";
        }, executor);
        CompletableFuture<String> future03 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
            log.info("查询商品的介绍");
            return "华为";
        }, executor);
        CompletableFuture<Void> all = CompletableFuture.allOf(future01, future02, future03);
        all.get();
        log.info("主线程结束...");
    }

}
