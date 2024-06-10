package com.entertainment.subscriber.note.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {
    private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    public static final String CACHE_NAME = "config";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(CACHE_NAME);
    }

    @CacheEvict(allEntries = true, value = {CACHE_NAME})
    @Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 500) // evict cache every 5 minutes.
    public void schedulingCacheEvict() {
        logger.debug("scheduling cache evict"); // may considering comment this line to save logs
    }

}
