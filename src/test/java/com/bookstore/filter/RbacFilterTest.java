package com.bookstore.filter;

import com.bookstore.model.Role;
import com.bookstore.model.SessionUser;
import com.bookstore.web.SessionKeys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RbacFilterTest {

    @Test
    void allowsAuditAdminToReachAuditPaths() throws Exception {
        RbacFilter filter = new RbacFilter();
        RecordingChain chain = new RecordingChain();
        ResponseRecorder response = new ResponseRecorder();

        filter.doFilter(request("/admin/audit", Role.AUDIT_ADMIN), response.proxy(), chain);

        assertTrue(chain.invoked);
        assertEquals(0, response.errorStatus);
    }

    @Test
    void forbidsCustomerFromAdminPaths() throws Exception {
        RbacFilter filter = new RbacFilter();
        RecordingChain chain = new RecordingChain();
        ResponseRecorder response = new ResponseRecorder();

        filter.doFilter(request("/admin/books", Role.CUSTOMER), response.proxy(), chain);

        assertFalse(chain.invoked);
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.errorStatus);
    }

    @Test
    void forbidsAuditAdminFromNonAuditAdminPaths() throws Exception {
        RbacFilter filter = new RbacFilter();
        RecordingChain chain = new RecordingChain();
        ResponseRecorder response = new ResponseRecorder();

        filter.doFilter(request("/admin/books", Role.AUDIT_ADMIN), response.proxy(), chain);

        assertFalse(chain.invoked);
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.errorStatus);
    }

    private HttpServletRequest request(String path, Role role) {
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
            case "getSession" -> session;
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

    private static final class RecordingChain implements FilterChain {
        private boolean invoked;

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) {
            invoked = true;
        }
    }

    private static final class ResponseRecorder {
        private int errorStatus;

        private HttpServletResponse proxy() {
            return RbacFilterTest.proxy(HttpServletResponse.class, (proxy, method, args) -> {
                if ("sendError".equals(method.getName()) && args.length >= 1) {
                    errorStatus = (Integer) args[0];
                    return null;
                }
                return defaultValue(proxy, method, args);
            });
        }
    }
}
