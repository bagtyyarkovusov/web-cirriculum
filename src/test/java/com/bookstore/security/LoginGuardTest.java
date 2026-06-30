package com.bookstore.security;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginGuardTest {

    private final LocalDateTime now = LocalDateTime.of(2026, 6, 30, 9, 30);

    @Test
    void isLocked_returnsTrueOnlyWhenLockUntilIsInTheFuture() {
        assertFalse(LoginGuard.isLocked(null, now));
        assertFalse(LoginGuard.isLocked(now, now));
        assertTrue(LoginGuard.isLocked(now.plusSeconds(1), now));
    }

    @Test
    void recordFailure_incrementsCountBeforeThreshold() {
        LoginGuard.FailureState state = LoginGuard.recordFailure(2, now);

        assertEquals(3, state.getFailCount());
        assertNull(state.getLockUntil());
    }

    @Test
    void recordFailure_locksForThirtyMinutesOnFifthFailure() {
        LoginGuard.FailureState state = LoginGuard.recordFailure(4, now);

        assertEquals(5, state.getFailCount());
        assertEquals(now.plusMinutes(30), state.getLockUntil());
    }

    @Test
    void recordSuccessClearsFailureState() {
        LoginGuard.FailureState state = LoginGuard.recordSuccess();

        assertEquals(0, state.getFailCount());
        assertNull(state.getLockUntil());
    }
}
