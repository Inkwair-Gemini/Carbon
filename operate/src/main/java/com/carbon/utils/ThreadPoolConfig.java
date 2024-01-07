package com.carbon.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @projectName: Carbon
 * @package: com.carbon.utils
 * @className: ThreadPoolConfig
 * @author: Doctor.H
 * @description: TODO
 * @date: 2024/1/7 10:46
 */
@Configuration
public class ThreadPoolConfig {
    static int cpuNums = Runtime.getRuntime().availableProcessors();
    private static final int corePoolSize = cpuNums*2+1;
    private static final int maximumPoolSize = cpuNums * 5;

    @Primary
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maximumPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(50);
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.setThreadNamePrefix("task--thread");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}

