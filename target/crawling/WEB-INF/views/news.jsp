<!-- src/main/webapp/WEB-INF/views/news.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>News</title>
</head>
<body>
    <h1>News Articles</h1>
    <c:forEach var="info" items="${info}">
        <h2>${info.title}</h2>
        <p>${info.content}</p>
        <p>${info.publisher}</p>
        <p>${info.upload}</p>
        <img src="${article.url}" alt="url">
        <!-- 필요한 다른 정보도 동일한 방식으로 표시 가능 -->
    </c:forEach>
</body>
</html>
