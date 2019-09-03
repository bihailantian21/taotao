<html>
<head>
    <title>测试页面</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
<#--学生信息：<br>
学号：${student.id}<br>
姓名：${student.name}<br>
年龄：${student.age}<br>
住址：${student.address}<br>-->

<#--学生列表：<br>
<table border="1">
    <tr>
        <th>学号</th>
        <th>姓名</th>
        <th>年龄</th>
        <th>地址</th>
    </tr>
		<#list stuList as stu>
		   <tr>
               <td>${stu.id}</td>
               <td>${stu.name}</td>
               <td>${stu.age}</td>
               <td>${stu.address}</td>
           </tr>
        </#list>
</table>-->


<#--学生列表：<br>
<table border="1">
    <tr>
        <th>序号</th>
        <th>学号</th>
        <th>姓名</th>
        <th>年龄</th>
        <th>地址</th>
    </tr>
		<#list stuList as stu>
		   <tr>
               <td>${stu_index}</td>
               <td>${stu.id}</td>
               <td>${stu.name}</td>
               <td>${stu.age}</td>
               <td>${stu.address}</td>
           </tr>
        </#list>
</table>-->



学生列表：<br>
<table border="1">
    <tr>
        <th>序号</th>
        <th>学号</th>
        <th>姓名</th>
        <th>年龄</th>
        <th>地址</th>
    </tr>
		<#list stuList as stu>
            <#if stu_index%2==0>
		   <tr bgcolor="red">
            <#else>
		   <tr bgcolor="blue">
            </#if>
		   		<td>${stu_index}</td>
			    <td>${stu.id}</td>
				<td>${stu.name}</td>
				<td>${stu.age}</td>
				<td>${stu.address}</td>
		   </tr>
        </#list>
</table>
<br>
日期处理类型：${date?date}
日期处理类型：${date?time}
日期处理类型：${date?datetime}

日期处理类型：${date?string('yyyy/MM/dd HH:mm:ss')}

<br>
null值的处理：${var!"默认值"}

<br>
使用if判断null值:
    <#if val??>
    var是有值的
    <#else>
    var是Null
    </#if>

<br>
页脚的添加:
    <#include "hello.ftl">





</body>
</html>

