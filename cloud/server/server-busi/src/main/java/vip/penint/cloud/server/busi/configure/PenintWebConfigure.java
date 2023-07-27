package vip.penint.cloud.server.busi.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import vip.penint.cloud.common.core.entity.constant.PenintConstant;

import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @author penint
 */
@Configuration
public class PenintWebConfigure {

    /**
     * 注册异步线程池
     */
    @Bean(PenintConstant.ASYNC_POOL)
    public ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("Penint-Async-Thread");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
