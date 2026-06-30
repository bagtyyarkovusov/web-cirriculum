<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>网上书店 · 销售统计</title>
    <style>
        body { font-family: -apple-system, "Segoe UI", "Microsoft YaHei", sans-serif; margin: 2rem; }
        h1 { font-size: 1.4rem; }
        nav { margin-bottom: 1rem; }
        nav a { margin-right: 12px; }
        #salesChart { width: min(100%, 960px); height: 420px; border: 1px solid #ddd; }
        table { border-collapse: collapse; margin-top: 1rem; min-width: 520px; }
        th, td { border: 1px solid #ccc; padding: 8px 12px; text-align: left; }
        th { background: #f5f5f5; }
        .muted { color: #666; }
        .number { text-align: right; }
        @media (max-width: 640px) {
            body { margin: 1rem; }
            #salesChart { height: 320px; }
            table { min-width: 100%; }
        }
    </style>
</head>
<body>
    <nav>
        <a href="${pageContext.request.contextPath}/app/admin">后台首页</a>
        <a href="${pageContext.request.contextPath}/app/admin/orders">订单管理</a>
        <a href="${pageContext.request.contextPath}/app/logout">退出</a>
    </nav>

    <h1>销售统计</h1>
    <p class="muted">统计已支付、已发货和已完成订单，已取消订单不计入销售额。</p>

    <div id="salesChart"></div>

    <c:choose>
        <c:when test="${empty stats}">
            <p class="muted">暂无销售数据。</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                    <tr><th>日期</th><th>订单数</th><th>销售额</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="s" items="${stats}">
                        <tr>
                            <td><c:out value="${s.day}"/></td>
                            <td class="number">${s.orderCount}</td>
                            <td class="number">￥${s.totalSales}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

    <script src="${pageContext.request.contextPath}/static/js/echarts.min.js"></script>
    <script>
        (function () {
            var chartNode = document.getElementById('salesChart');
            if (!chartNode || !window.echarts) {
                return;
            }

            var labels = ${chartLabelsJson};
            var totalSales = ${chartTotalSalesJson};
            var orderCounts = ${chartOrderCountsJson};
            var chart = echarts.init(chartNode);

            chart.setOption({
                title: {
                    text: '每日销售统计',
                    left: 'center'
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['销售额', '订单数'],
                    top: 30
                },
                grid: {
                    left: 70,
                    right: 70,
                    top: 84,
                    bottom: 48
                },
                xAxis: {
                    type: 'category',
                    data: labels,
                    axisPointer: { type: 'shadow' }
                },
                yAxis: [
                    {
                        type: 'value',
                        name: '销售额',
                        axisLabel: { formatter: '￥{value}' }
                    },
                    {
                        type: 'value',
                        name: '订单数',
                        minInterval: 1
                    }
                ],
                series: [
                    {
                        name: '销售额',
                        type: 'bar',
                        data: totalSales
                    },
                    {
                        name: '订单数',
                        type: 'line',
                        yAxisIndex: 1,
                        data: orderCounts,
                        smooth: true
                    }
                ]
            });

            window.addEventListener('resize', function () {
                chart.resize();
            });
        })();
    </script>
</body>
</html>
