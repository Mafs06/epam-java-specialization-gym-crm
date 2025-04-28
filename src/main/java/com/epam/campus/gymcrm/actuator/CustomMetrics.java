package com.epam.campus.gymcrm.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CustomMetrics {

    // Counter to track number of successful operations
    private final Counter successfulOperationsCounter;

    // Gauge to track the number of new users
    private final AtomicInteger newUsers = new AtomicInteger(0);
    private final Gauge newUsersGauge;

    public CustomMetrics(MeterRegistry meterRegistry) {
        // Create and register the counter
        successfulOperationsCounter = meterRegistry.counter("successful_operations");

        // Create and register the gauge with an AtomicInteger (it can go up and down)
        newUsersGauge = Gauge.builder("new_users", newUsers, AtomicInteger::get)
                .description("Number of new users in the system")
                .register(meterRegistry);
    }

    // Method to increment the successful operations counter
    public void incrementSuccessfulOperation() {
        successfulOperationsCounter.increment();
    }

    // Method to update the number of new users
    public void addNewUser() {
        newUsers.incrementAndGet();
    }
}

