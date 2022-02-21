package com.ledungcobra.configuration.cache;

import com.hazelcast.config.*;
import com.ledungcobra.common.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public Config hazelCacheConfig() {

        var config = new MapConfig()
                .setName(Constants.USER_CACHE)
                .setTimeToLiveSeconds(50)
                .setEvictionConfig(new EvictionConfig()
                        .setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_SIZE)
                        .setSize(200)
                        .setEvictionPolicy(EvictionPolicy.LRU));

        return new Config()
                .setInstanceName("hazel-instance")
                .addMapConfig(config);
    }
}
