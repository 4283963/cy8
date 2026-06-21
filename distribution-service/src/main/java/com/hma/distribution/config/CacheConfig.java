package com.hma.distribution.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hma.distribution.dto.CachedRuleEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableCaching
@EnableConfigurationProperties({CacheProperties.class, MonitorProperties.class})
public class CacheConfig {

    @Value("${hma.cache.rules.max-size:1000}")
    private long rulesMaxSize;

    @Value("${hma.cache.rules.expire-after-write:30m}")
    private Duration rulesExpireAfterWrite;

    @Bean(name = "ruleDownloadCache")
    public Cache<String, CachedRuleEntry> ruleDownloadCache() {
        return Caffeine.newBuilder()
                .maximumSize(rulesMaxSize)
                .expireAfterWrite(rulesExpireAfterWrite)
                .recordStats()
                .build();
    }

    @Bean(name = "cacheRefreshExecutor")
    public AsyncTaskExecutor cacheRefreshExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(64);
        executor.setThreadNamePrefix("cache-refresh-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(10);
        executor.initialize();
        return new TaskExecutorAdapter(executor);
    }

    @Bean(name = "accessStatExecutor")
    public AsyncTaskExecutor accessStatExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(1024);
        executor.setThreadNamePrefix("access-stat-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return new TaskExecutorAdapter(executor);
    }
}
