package top.parak.gulimall.seckill.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 任务调度配置
 *
 * @author KHighness
 * @since 2022-01-20
 * @email parakovo@gmail.com
 */
@Configuration
@EnableAsync
@EnableScheduling
public class GulimallScheduledConfig {

    /**
     * CRON表达式
     * <pre>
     *  ┌───────────── second (0 - 59)
     *  │ ┌───────────── minute (0 - 59)
     *  │ │ ┌───────────── hour (0 - 23)
     *  │ │ │ ┌───────────── dayOfMonth (1 - 31)
     *  │ │ │ │ ┌───────────── month (1 - 12) (or JAN-DEC)
     *  │ │ │ │ │ ┌───────────── dayOfWeek (1 - 7) (or MON-SUN)
     *  │ │ │ │ │ │
     *  │ │ │ │ │ │
     *  S M H D M W
     *  </pre>
     *
     *  <b>NOTE Spring任务调度</b>
     *  <ol>
     *  <li>cron表达式只有6位，没有第七位的年</li>
     *  <li>在dayOfWeek位置，1-7代表周一到周日</li>
     *  <li>当前任务会阻塞下一个任务的执行</li>
     *  </ol>
     */
    @Scheduled(cron = "0 0 0 ? * 1")
    public void highness() {

    }

}
