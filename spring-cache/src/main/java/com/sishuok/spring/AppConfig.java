package com.sishuok.spring;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-1
 * <p>Version: 1.0
 */
@Configuration
@ComponentScan(basePackages = "com.sishuok.spring.service")
@EnableCaching(proxyTargetClass = true)
public class AppConfig implements CachingConfigurer {
    @Bean
    @Override
    public CacheManager cacheManager() {

        try {
            net.sf.ehcache.CacheManager ehcacheCacheManager
                    = new net.sf.ehcache.CacheManager(new ClassPathResource("ehcache.xml").getInputStream());

            EhCacheCacheManager cacheCacheManager = new EhCacheCacheManager(ehcacheCacheManager);
            return cacheCacheManager;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }
}
