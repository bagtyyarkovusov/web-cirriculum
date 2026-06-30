<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="section" value="admin"/>
<c:set var="pageTitle" value="销售统计"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="page-head"><div><h1>销售统计</h1>
  <p class="muted">统计已支付、已发货和已完成订单，已取消订单不计入销售额。</p></div></div>
<div class="panel" style="margin-bottom:16px">
  <div id="chart"></div>
</div>
<c:choose>
  <c:when test="${empty stats}">
    <div class="empty"><svg class="icon"><use href="#i-chart"/></svg><p>暂无销售数据。</p></div>
  </c:when>
  <c:otherwise>
    <table class="table">
      <thead><tr><th>日期</th><th class="right">订单数</th><th class="right">销售额</th></tr></thead>
      <tbody>
        <c:forEach var="s" items="${stats}">
          <tr>
            <td><c:out value="${s.day}"/></td>
            <td class="right num">${s.orderCount}</td>
            <td class="right price">￥${s.totalSales}</td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </c:otherwise>
</c:choose>
<script src="${ctx}/static/js/echarts.min.js"></script>
<script>
  (function () {
    var chartNode = document.getElementById('chart');
    if (!chartNode || !window.echarts) { return; }
    var labels = ${chartLabelsJson};
    var totalSales = ${chartTotalSalesJson};
    var orderCounts = ${chartOrderCountsJson};
    var chart = echarts.init(chartNode);
    chart.setOption({
      title: { text: '每日销售统计', left: 'center' },
      tooltip: { trigger: 'axis' },
      legend: { data: ['销售额', '订单数'], top: 30 },
      grid: { left: 70, right: 70, top: 84, bottom: 48 },
      xAxis: { type: 'category', data: labels, axisPointer: { type: 'shadow' } },
      yAxis: [
        { type: 'value', name: '销售额', axisLabel: { formatter: '￥{value}' } },
        { type: 'value', name: '订单数', minInterval: 1 }
      ],
      series: [
        { name: '销售额', type: 'bar', data: totalSales, itemStyle: { color: '#2563eb' } },
        { name: '订单数', type: 'line', yAxisIndex: 1, data: orderCounts, smooth: true }
      ]
    });
    window.addEventListener('resize', function () { chart.resize(); });
  })();
</script>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
