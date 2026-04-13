package com.logbook.studentmanagement.config;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CacheMetricsLogger {
    private final CacheManager cacheManager;
    @Scheduled(fixedRate = 10000)
    public void logCacheStats() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            org.springframework.cache.Cache springCache = cacheManager.getCache(cacheName);

            if (springCache instanceof CaffeineCache caffeineCache) {
                Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
                com.github.benmanes.caffeine.cache.stats.CacheStats stats = nativeCache.stats();

                log.info("[CACHE STATS] Cache '{}': size={}, hits={}, misses={}, hitRate={}, evictions={}",
                        cacheName,
                        nativeCache.estimatedSize(),
                        stats.hitCount(),
                        stats.missCount(),
                        String.format("%.3f", stats.hitRate()),
                        stats.evictionCount()
                );
            }
        });
    }

}
