package com.expensetracker.expense_tracker.config;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * Rate limiting configuration properties.
 * Externalized to application.yaml for environment-specific tuning.
 */
@Configuration
@ConfigurationProperties(prefix = "rate-limit")
@Getter
@Setter
public class RateLimitConfig {
    
    /**
     * Rate limit for authentication endpoints (register/login).
     * Format: requests per time window
     */
    private int authRequestsPerMinute = 5;
    
    /**
     * Rate limit for expense creation endpoint.
     */
    private int expenseCreationRequestsPerMinute = 10;
    
    /**
     * Rate limit for general API endpoints.
     */
    private int generalRequestsPerMinute = 30;
    
    /**
     * Time window duration in minutes.
     */
    private int timeWindowMinutes = 1;
    
    /**
     * Get time window as Duration.
     */
    public Duration getTimeWindow() {
        return Duration.ofMinutes(timeWindowMinutes);
    }
}
