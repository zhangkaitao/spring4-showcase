<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title></title>
</head>
<body>
验证失败了！<br/><br/><br/>
<spring:hasBindErrors name="user">
    <c:if test="${errors.fieldErrorCount > 0}">
        字段错误：<br/>
        <c:forEach items="${errors.fieldErrors}" var="error">
            <spring:message var="message" code="${error.code}" arguments="${error.arguments}"
                            text="${error.defaultMessage}"/>
            ${error.field}------${message}<br/>
        </c:forEach>
    </c:if>

    <c:if test="${errors.globalErrorCount > 0}">
        全局错误：<br/>
        <c:forEach items="${errors.globalErrors}" var="error">
            <spring:message var="message" code="${error.code}" arguments="${error.arguments}"
                            text="${error.defaultMessage}"/>
            <c:if test="${not empty message}">
                ${message}<br/>
            </c:if>
        </c:forEach>
    </c:if>
</spring:hasBindErrors>


</body>
</html>