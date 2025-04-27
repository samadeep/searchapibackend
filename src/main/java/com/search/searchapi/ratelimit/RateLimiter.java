package com.search.searchapi.ratelimit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimiter {
    private final Map<String, Window> windows = new ConcurrentHashMap<>();
    private final int windowSize;
    private final int maxRequests;
    private final boolean enabled;

    public RateLimiter(
            @Value("${search.rate-limit.enabled:true}") boolean enabled,
            @Value("${search.rate-limit.window-size:60}") int windowSize,
            @Value("${search.rate-limit.max-requests:100}") int maxRequests) {
        this.enabled = enabled;
        this.windowSize = windowSize;
        this.maxRequests = maxRequests;
    }

    public boolean tryAcquire(String key) {
        if (!enabled) {
            return true;
        }

        Window window = windows.computeIfAbsent(key, k -> new Window());
        return window.tryAcquire();
    }

    private class Window {
        private final AtomicInteger counter = new AtomicInteger(0);
        private long windowStart = System.currentTimeMillis();

        public boolean tryAcquire() {
            long now = System.currentTimeMillis();
            long elapsed = now - windowStart;

            if (elapsed >= windowSize * 1000) {
                // Reset window
                counter.set(0);
                windowStart = now;
            }

            return counter.incrementAndGet() <= maxRequests;
        }
    }
} 