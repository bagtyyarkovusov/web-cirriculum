package com.bookstore.service;

import com.bookstore.dao.AuditRepository;
import com.bookstore.model.AuditLog;
import com.bookstore.model.Role;
import com.bookstore.model.SessionUser;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AuditServiceTest {

    private final LocalDateTime now = LocalDateTime.of(2026, 6, 30, 11, 0);

    @Test
    void logStoresAuthenticatedUserAction() {
        FakeAuditRepository repo = new FakeAuditRepository();
        AuditService service = new AuditService(repo, () -> now);
        SessionUser user = new SessionUser(7L, "operator", "运营员", Role.OPERATOR_ADMIN);

        service.log(user, "BOOK_STATUS", "bookId=10,status=OFF", "127.0.0.1");

        AuditLog saved = repo.inserted.get(0);
        assertEquals(7L, saved.getUserId());
        assertEquals("operator", saved.getUsername());
        assertEquals("BOOK_STATUS", saved.getAction());
        assertEquals("bookId=10,status=OFF", saved.getDetail());
        assertEquals("127.0.0.1", saved.getIp());
        assertEquals(now, saved.getCreatedAt());
    }

    @Test
    void logStoresAnonymousSecurityActionWithoutUser() {
        FakeAuditRepository repo = new FakeAuditRepository();
        AuditService service = new AuditService(repo, () -> now);

        service.log(null, "LOGIN_FAILURE", "username=missing", "10.0.0.5");

        AuditLog saved = repo.inserted.get(0);
        assertNull(saved.getUserId());
        assertNull(saved.getUsername());
        assertEquals("LOGIN_FAILURE", saved.getAction());
        assertEquals("username=missing", saved.getDetail());
        assertEquals("10.0.0.5", saved.getIp());
        assertEquals(now, saved.getCreatedAt());
    }

    @Test
    void searchDelegatesFiltersAndLimitToRepository() {
        FakeAuditRepository repo = new FakeAuditRepository();
        AuditService service = new AuditService(repo, () -> now);
        LocalDateTime from = now.minusDays(1);
        LocalDateTime to = now.plusDays(1);

        service.search("operator", "BOOK_STATUS", "bookId=10", from, to);

        assertEquals("operator", repo.username);
        assertEquals("BOOK_STATUS", repo.action);
        assertEquals("bookId=10", repo.keyword);
        assertEquals(from, repo.from);
        assertEquals(to, repo.to);
        assertEquals(200, repo.limit);
    }

    private static final class FakeAuditRepository implements AuditRepository {
        private final List<AuditLog> inserted = new ArrayList<>();
        private String username;
        private String action;
        private String keyword;
        private LocalDateTime from;
        private LocalDateTime to;
        private int limit;

        @Override
        public void insert(AuditLog log) {
            inserted.add(log);
        }

        @Override
        public List<AuditLog> search(String username, String action, String keyword,
                                     LocalDateTime from, LocalDateTime to, int limit) {
            this.username = username;
            this.action = action;
            this.keyword = keyword;
            this.from = from;
            this.to = to;
            this.limit = limit;
            return List.of();
        }
    }
}
