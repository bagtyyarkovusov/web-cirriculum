package com.bookstore.security;

import java.time.LocalDateTime;

/**
 * Login failure policy: five consecutive failures lock the account for 30 minutes.
 */
public final class LoginGuard {

    public static final int MAX_FAILURES = 5;
    public static final int LOCK_MINUTES = 30;

    private LoginGuard() {
    }

    public static boolean isLocked(LocalDateTime lockUntil, LocalDateTime now) {
        return lockUntil != null && lockUntil.isAfter(now);
    }

    public static FailureState recordFailure(int currentFailCount, LocalDateTime now) {
        int nextCount = currentFailCount + 1;
        LocalDateTime lockUntil = nextCount >= MAX_FAILURES ? now.plusMinutes(LOCK_MINUTES) : null;
        return new FailureState(nextCount, lockUntil);
    }

    public static FailureState recordSuccess() {
        return new FailureState(0, null);
    }

    public static final class FailureState {
        private final int failCount;
        private final LocalDateTime lockUntil;

        public FailureState(int failCount, LocalDateTime lockUntil) {
            this.failCount = failCount;
            this.lockUntil = lockUntil;
        }

        public int getFailCount() {
            return failCount;
        }

        public LocalDateTime getLockUntil() {
            return lockUntil;
        }
    }
}
