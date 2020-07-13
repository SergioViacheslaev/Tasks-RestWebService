<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Редактирование задачи</title>
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

<form method="post" action="<c:url value='/update_task'/>">
    <label>Новое название:  <input type="text" name="title" required /></label><br>
    <label>Новое описание: <input type="text" name="description" required /></label><br>
    <label>Новый срок выполнения: <input type="date" name="deadline_date" required /></label><br>
    <label>Новый статус выполнения: <input type="checkbox" name="done"  /></label><br>

    <input type="number" hidden name="id" value="${requestScope.task.id}"/>

    <input type="submit" value="Ok" name="Ok"><br>
</form>
</body>
</html>
