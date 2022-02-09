package ie.nuig.i3market.semantic.engine.config;

import com.github.jsonldjava.shaded.com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration extends CachingConfigurerSupport {

    @Bean
    @Override
    public CacheManager cacheManager() {

        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager() {

            @Override
            protected Cache createConcurrentMapCache(final String name) {
                return new ConcurrentMapCache(name, CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS)
                        .maximumSize(50).build().asMap(), false);
            }
        };

        cacheManager.setCacheNames(
                Arrays.asList("offeringsByProviderId",
                        "getAllDataOffering",
                        "offeringById",
                        "offeringByCategory",
                        "contractParameters",
                        "providerByCategory",
                        "offeringID"));

        return cacheManager;

    }

}
