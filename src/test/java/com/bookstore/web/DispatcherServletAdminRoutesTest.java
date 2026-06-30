package com.bookstore.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
    void servesAuditAdminDashboardRoute() throws Exception {
        TestableDispatcherServlet servlet = new TestableDispatcherServlet();
        servlet.init();
        RequestRecorder request = new RequestRecorder("/admin/audit");
        ResponseRecorder response = new ResponseRecorder();

        servlet.get(request.proxy(), response.proxy());

        assertEquals(0, response.errorStatus);
        assertEquals("/WEB-INF/views/admin/dashboard.jsp", request.forwardedPath);
    }

    private static final class TestableDispatcherServlet extends DispatcherServlet {
        private void get(HttpServletRequest request, HttpServletResponse response) throws Exception {
            doGet(request, response);
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
