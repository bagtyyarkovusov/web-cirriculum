package com.bookstore.web.commands;

import com.bookstore.dao.StatsRepository;
import com.bookstore.model.SalesStat;
import com.bookstore.service.StatsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdminStatsCommandTest {

    @Test
    void exposesSalesStatsAndChartJsonToStatsView() {
        List<SalesStat> stats = List.of(
                new SalesStat(LocalDate.of(2026, 6, 30), 3, new BigDecimal("199.80"))
        );
        AdminStatsCommand command = new AdminStatsCommand(
                new StatsService(new StaticStatsRepository(stats)));
        RequestRecorder request = new RequestRecorder();

        String view = command.execute(request.proxy(), responseProxy());

        assertEquals("admin/stats", view);
        assertEquals(stats, request.attributes.get("stats"));
        assertEquals("[\"2026-06-30\"]", request.attributes.get("chartLabelsJson"));
        assertEquals("[199.80]", request.attributes.get("chartTotalSalesJson"));
        assertEquals("[3]", request.attributes.get("chartOrderCountsJson"));
    }

    private static final class StaticStatsRepository implements StatsRepository {
        private final List<SalesStat> stats;

        private StaticStatsRepository(List<SalesStat> stats) {
            this.stats = stats;
        }

        @Override
        public List<SalesStat> findDailySales() {
            return stats;
        }
    }

    private static final class RequestRecorder {
        private final Map<String, Object> attributes = new HashMap<>();

        private HttpServletRequest proxy() {
            return AdminStatsCommandTest.proxy(HttpServletRequest.class, (proxy, method, args) -> {
                if ("setAttribute".equals(method.getName())) {
                    attributes.put((String) args[0], args[1]);
                    return null;
                }
                return defaultValue(proxy, method, args);
            });
        }
    }

    private static HttpServletResponse responseProxy() {
        return proxy(HttpServletResponse.class, AdminStatsCommandTest::defaultValue);
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
