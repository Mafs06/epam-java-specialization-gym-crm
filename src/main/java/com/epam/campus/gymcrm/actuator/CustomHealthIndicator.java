package com.epam.campus.gymcrm.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    private final DataSource dataSource; // Inject DataSource

    public CustomHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        Health.Builder healthBuilder = Health.up(); // Start with UP status by default

        // Check Database connectivity
        try {
            checkDatabaseConnection();
        } catch (SQLException e) {
            healthBuilder.down().withDetail("database", "Failed to connect: " + e.getMessage());
        }

        // Check Memory usage
        checkMemoryUsage(healthBuilder);

        return healthBuilder.build();
    }

    private void checkDatabaseConnection() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            if (!connection.isValid(1)) { // Check if the connection is valid
                throw new SQLException("Database connection is invalid.");
            }
        }
    }

    private void checkMemoryUsage(Health.Builder healthBuilder) {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long usedMemory = memoryBean.getHeapMemoryUsage().getUsed();
        long maxMemory = memoryBean.getHeapMemoryUsage().getMax();
        double usage = (double) usedMemory / maxMemory;
        double threshold = 0.9; // 90% threshold

        healthBuilder.withDetail("memory.used", usedMemory);
        healthBuilder.withDetail("memory.max", maxMemory);
        healthBuilder.withDetail("memory.usage", String.format("%.2f%%", usage * 100));

        if (usage > threshold) {
            healthBuilder.down().withDetail("memory", "Memory usage above threshold!");
        }
    }
}