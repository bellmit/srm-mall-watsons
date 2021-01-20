package org.srm.mall.common.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.srm.mall.config.AsyncTaskConfig;

import java.lang.reflect.Method;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
@EnableAsync
public class WatsonsAddressAsyncTaskConfig {

    @Bean("addressExecutor")
    public ThreadPoolTaskExecutor getAddressExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        taskExecutor.setCorePoolSize(16);
        // 设置最大线程数
        taskExecutor.setMaxPoolSize(100);
        // 设置队列容量
        taskExecutor.setQueueCapacity(1000);
        // 设置线程活跃时间（秒）
        taskExecutor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        taskExecutor.setThreadNamePrefix("address");
        // 设置拒绝策略
        taskExecutor.setRejectedExecutionHandler(new WatsonsAddressAsyncTaskConfig.CustomRejectedExecutionHandler());
        //初始化线程池
        taskExecutor.initialize();
        return taskExecutor;
    }


    @Bean
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new WatsonsAddressAsyncTaskConfig.SelfAsyncExceptionHandler();
    }

    /**
     * 自定义线程池异常处理
     */
    class SelfAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
            log.info("Exception message - " + throwable.getMessage());
            log.info("Method name - " + method.getName());
            for (Object param : obj) {
                log.info("Parameter value - " + param);
            }
        }
    }

    /**
     * 自定义线程池拒绝策略 当线程池队列满的时候阻塞线程
     */
    class CustomRejectedExecutionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                // 阻塞线程
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                log.info(ExceptionUtils.getStackTrace(e));
            }
        }
    }

}
