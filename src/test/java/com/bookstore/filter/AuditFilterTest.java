package com.bookstore.filter;

import com.bookstore.dao.AuditRepository;
import com.bookstore.model.AuditLog;
import com.bookstore.model.Role;
import com.bookstore.model.SessionUser;
import com.bookstore.service.AuditService;
import com.bookstore.web.SessionKeys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuditFilterTest {

    @Test
    void logsAuditAdminViewerAccessAfterChainRuns() throws Exception {
        FakeAuditRepository repo = new FakeAuditRepository();
        AuditFilter filter = new AuditFilter(new AuditService(repo, () ->
                LocalDateTime.of(2026, 6, 30, 12, 0)));
        RecordingChain chain = new RecordingChain();

        filter.doFilter(request("/admin/audit", "GET", Role.AUDIT_ADMIN), response(), chain);

        assertTrue(chain.invoked);
        AuditLog saved = repo.inserted.get(0);
        assertEquals(1L, saved.getUserId());
        assertEquals("audit_admin", saved.getUsername());
        assertEquals("AUDIT_VIEW", saved.getAction());
        assertEquals("method=GET,path=/admin/audit", saved.getDetail());
        assertEquals("127.0.0.1", saved.getIp());
    }

    private HttpServletRequest request(String path, String methodName, Role role) {
        SessionUser user = new SessionUser(1L, role.name().toLowerCase(), "Test User", role);
        HttpSession session = proxy(HttpSession.class, (proxy, method, args) -> {
            if ("getAttribute".equals(method.getName())
                    && args.length == 1
                    && SessionKeys.CURRENT_USER.equals(args[0])) {
                return user;
            }
            return defaultValue(proxy, method, args);
        });
        return proxy(HttpServletRequest.class, (proxy, method, args) -> switch (method.getName()) {
            case "getPathInfo" -> path;
            case "getMethod" -> methodName;
            case "getSession" -> session;
            case "getRemoteAddr" -> "127.0.0.1";
            default -> defaultValue(proxy, method, args);
        });
    }

    private ServletResponse response() {
        return proxy(ServletResponse.class, AuditFilterTest::defaultValue);
    }

    private static <T> T proxy(Class<T> type, InvocationHandler handler) {
        Object proxy = Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class<?>[]{type},
                handler);
        return type.cast(proxy);
    }

    private static Object defaultValue(Object proxy, Method method, Object[] args) {
        if ("toString".equals(method.getName())) {
            return proxy.getClass().getInterfaces()[0].getSimpleName() + "Proxy";
        }
        if ("hashCode".equals(method.getName())) {
            return System.identityHashCode(proxy);
        }
        if ("equals".equals(method.getName())) {
            return proxy == args[0];
        }
        Class<?> returnType = method.getReturnType();
        if (!returnType.isPrimitive()) {
            return null;
        }
        if (returnType == boolean.class) {
            return false;
        }
        if (returnType == char.class) {
            return '\0';
        }
        return 0;
    }

    private static final class RecordingChain implements FilterChain {
        private boolean invoked;

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) {
            invoked = true;
        }
    }

    private static final class FakeAuditRepository implements AuditRepository {
        private final List<AuditLog> inserted = new ArrayList<>();

        @Override
        public void insert(AuditLog log) {
            inserted.add(log);
        }

        @Override
        public List<AuditLog> search(String username, String action, String keyword,
                                     LocalDateTime from, LocalDateTime to, int limit) {
            return List.of();
        }
    }
}
