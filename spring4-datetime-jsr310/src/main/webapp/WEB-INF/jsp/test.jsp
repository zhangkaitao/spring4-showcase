<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>


<spring:eval expression="entity.dateTime"/><br/>
<spring:eval expression="entity.date"/><br/>
<spring:eval expression="entity.time"/><br/>


</body>
</html>