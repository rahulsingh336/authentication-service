package com.luxoft.authentication.service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import javax.annotation.PreDestroy;

@Slf4j
@Component
public class RedisStartStopConfig {

    private RedisServer redisServer;

    public RedisStartStopConfig() {
        log.info("Starting in memory redis");
        try {
            redisServer = RedisServer.builder().port(6370).build();
            redisServer.start();
        } catch (Exception e) {
            //do nothing
        }
    }

    @PreDestroy
    public void destroy() {
        log.info("Stopping in memory redis");
        redisServer.stop();
    }
}
