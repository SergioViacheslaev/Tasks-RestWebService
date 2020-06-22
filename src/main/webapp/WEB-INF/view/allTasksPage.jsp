<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Сервис задач</title>
    <style>
        <%@include file="/WEB-INF/css/style.css" %>
    </style>

</head>
<body>

<h2>Список всех задач пользователей</h2>

<c:forEach var="task" items="${requestScope.tasks}">
    <ul>
        Название: <c:out value="${task.title}"/> <br>
        Описание: <c:out value="${task.description}"/> <br>
        Срок выполнения: <c:out value="${task.deadline_date}"/> <br>
        Исполнитель: <c:out value="${task.executor.name}"/> <c:out value="${task.executor.surname}"/> <br>
        Выполнена: <c:out value="${task.done}"/> <br>
    </ul>
    <hr/>

</c:forEach>

<a href="<c:url value="/tasksmenu"/>">
    <img src="<%=request.getContextPath()%>/images/backToMyTasks.png" alt="Logo" width="120" height="60"></a>
</a>


</body>
</html>
