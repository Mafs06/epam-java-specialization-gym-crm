package com.epam.campus.gymcrm.security;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

@Component
public class BruteForceProtectionService {

    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final long BLOCK_TIME = TimeUnit.MINUTES.toMillis(5);
    private ConcurrentHashMap<String, FailedLoginAttempt> loginAttempts = new ConcurrentHashMap<>();

    public boolean isAccountLocked(String username) {
        FailedLoginAttempt attempt = loginAttempts.get(username);
        if (attempt != null && attempt.getFailedAttempts() >= MAX_FAILED_ATTEMPTS) {
            long blockTimeRemaining = System.currentTimeMillis() - attempt.getFirstAttemptTime();
            if (blockTimeRemaining < BLOCK_TIME) {
                return true; // User is blocked
            } else {
                // Reset the failed attempts after the block time has passed
                loginAttempts.remove(username);
            }
        }
        return false;
    }

    public void registerFailedAttempt(String username) {
        loginAttempts.putIfAbsent(username, new FailedLoginAttempt(username));
        FailedLoginAttempt attempt = loginAttempts.get(username);
        attempt.incrementFailedAttempts();
    }

    public void registerSuccessfulAttempt(String username) {
        loginAttempts.remove(username); // Reset the failed attempts on successful login
    }

    private static class FailedLoginAttempt {
        private final String username;
        private int failedAttempts;
        private final long firstAttemptTime;

        public FailedLoginAttempt(String username) {
            this.username = username;
            this.failedAttempts = 0;
            this.firstAttemptTime = System.currentTimeMillis();
        }

        public void incrementFailedAttempts() {
            this.failedAttempts++;
        }

        public int getFailedAttempts() {
            return failedAttempts;
        }

        public long getFirstAttemptTime() {
            return firstAttemptTime;
        }
    }
}

