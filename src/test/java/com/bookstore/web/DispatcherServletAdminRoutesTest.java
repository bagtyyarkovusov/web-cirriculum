package com.bookstore.web;

import com.bookstore.dao.AuditRepository;
import com.bookstore.dao.StatsRepository;
import com.bookstore.model.AuditLog;
import com.bookstore.model.SalesStat;
import com.bookstore.service.AuditService;
import com.bookstore.service.StatsService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DispatcherServletAdminRoutesTest {

    @Test
    void servesOperatorAdminDashboardRoute() throws Exception {
        TestableDispatcherServlet servlet = new TestableDispatcherServlet();
        servlet.init();
        RequestRecorder request = new RequestRecorder("/admin");
        ResponseRecorder response = new ResponseRecorder();

        servlet.get(request.proxy(), response.proxy());

        assertEquals(0, response.errorStatus);
        assertEquals("/WEB-INF/views/admin/dashboard.jsp", request.forwardedPath);
    }

    @Test
    void servesAuditAdminLogRoute() throws Exception {
        TestableDispatcherServlet servlet = new TestableDispatcherServlet();
        servlet.init();
        RequestRecorder request = new RequestRecorder("/admin/audit");
        ResponseRecorder response = new ResponseRecorder();

        servlet.get(request.proxy(), response.proxy());

        assertEquals(0, response.errorStatus);
        assertEquals("/WEB-INF/views/admin/audit/list.jsp", request.forwardedPath);
    }

    @Test
    void servesOperatorAdminStatsRoute() throws Exception {
        TestableDispatcherServlet servlet = new TestableDispatcherServlet();
        servlet.init();
        RequestRecorder request = new RequestRecorder("/admin/stats");
        ResponseRecorder response = new ResponseRecorder();

        servlet.get(request.proxy(), response.proxy());

        assertEquals(0, response.errorStatus);
        assertEquals("/WEB-INF/views/admin/stats.jsp", request.forwardedPath);
    }

    private static final class TestableDispatcherServlet extends DispatcherServlet {
        private void get(HttpServletRequest request, HttpServletResponse response) throws Exception {
            doGet(request, response);
        }

        @Override
        protected AuditService createAuditService() {
            return new AuditService(new EmptyAuditRepository(), LocalDateTime::now);
        }

        @Override
        protected StatsService createStatsService() {
            return new StatsService(new EmptyStatsRepository());
        }
    }

    private static final class EmptyAuditRepository implements AuditRepository {
        @Override
        public void insert(AuditLog log) {
        }

        @Override
        public List<AuditLog> search(String username, String action, String keyword,
                                     LocalDateTime from, LocalDateTime to, int limit) {
            return List.of();
        }
    }

    private static final class EmptyStatsRepository implements StatsRepository {
        @Override
        public List<SalesStat> findDailySales() {
            return List.of();
        }
    }

    private static final class RequestRecorder {
        private final String path;
        private String forwardedPath;

        private RequestRecorder(String path) {
            this.path = path;
        }

        private HttpServletRequest proxy() {
            return DispatcherServletAdminRoutesTest.proxy(HttpServletRequest.class, (proxy, method, args) -> {
                return switch (method.getName()) {
                    case "getPathInfo" -> path;
                    case "getMethod" -> "GET";
                    case "getRequestDispatcher" -> dispatcher((String) args[0]);
                    default -> defaultValue(proxy, method, args);
                };
            });
        }

        private RequestDispatcher dispatcher(String viewPath) {
            return DispatcherServletAdminRoutesTest.proxy(RequestDispatcher.class, (proxy, method, args) -> {
                if ("forward".equals(method.getName())) {
                    forwardedPath = viewPath;
                }
                return defaultValue(proxy, method, args);
            });
        }
    }

    private static final class ResponseRecorder {
        private int errorStatus;

        private HttpServletResponse proxy() {
            return DispatcherServletAdminRoutesTest.proxy(HttpServletResponse.class, (proxy, method, args) -> {
                if ("sendError".equals(method.getName()) && args.length >= 1) {
                    errorStatus = (Integer) args[0];
                    return null;
                }
                return defaultValue(proxy, method, args);
            });
        }
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
}
