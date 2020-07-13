<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Смена исполнителя задачи</title>
    <style>
        <%@include file="/WEB-INF/css/style.css" %>
    </style>

</head>
<body>


<div>Название: <c:out value="${requestScope.task.title}"/> </div>
<div>Описание: <c:out value="${requestScope.task.description}"/> </div>
<div>Срок выполнения: <c:out value="${requestScope.task.deadline_date}"/> </div>
<div>Исполнитель: <c:out value="${requestScope.task.executor.name}"/> <c:out value="${requestScope.task.executor.surname}"/>  </div>
<div>Выполнена: <c:out value="${requestScope.task.done}"/> </div>

<br />

<form method="post" action="<c:url value='/update_executor'/>">
    <label>Новый исполнитель (username): <input type="text" name="executor_username" required /></label><br>

    <input type="number" hidden name="id" value="${requestScope.task.id}"/>

    <input type="submit" value="Ok" name="Ok"><br>
</form>
</body>
</html>
