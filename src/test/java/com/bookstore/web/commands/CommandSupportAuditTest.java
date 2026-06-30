package com.bookstore.web.commands;

import com.bookstore.dao.AuditRepository;
import com.bookstore.model.AuditLog;
import com.bookstore.model.Role;
import com.bookstore.model.SessionUser;
import com.bookstore.service.AuditService;
import com.bookstore.web.SessionKeys;
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

class CommandSupportAuditTest {

    @Test
    void auditLogsCurrentSessionUserAndRemoteAddress() {
        FakeAuditRepository repo = new FakeAuditRepository();
        AuditService auditService = new AuditService(repo, () ->
                LocalDateTime.of(2026, 6, 30, 12, 30));
        SessionUser user = new SessionUser(9L, "customer", "客户", Role.CUSTOMER);

        CommandSupport.audit(request(user), auditService, "CHECKOUT", "orderId=3");

        AuditLog saved = repo.inserted.get(0);
        assertEquals(9L, saved.getUserId());
        assertEquals("customer", saved.getUsername());
        assertEquals("CHECKOUT", saved.getAction());
        assertEquals("orderId=3", saved.getDetail());
        assertEquals("192.0.2.10", saved.getIp());
    }

    private HttpServletRequest request(SessionUser user) {
        HttpSession session = proxy(HttpSession.class, (proxy, method, args) -> {
            if ("getAttribute".equals(method.getName())
                    && args.length == 1
                    && SessionKeys.CURRENT_USER.equals(args[0])) {
                return user;
            }
            return defaultValue(proxy, method, args);
        });
        return proxy(HttpServletRequest.class, (proxy, method, args) -> switch (method.getName()) {
            case "getSession" -> session;
            case "getRemoteAddr" -> "192.0.2.10";
            default -> defaultValue(proxy, method, args);
        });
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
