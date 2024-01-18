<!-- src/main/webapp/WEB-INF/views/news.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>News</title>
</head>
<body>
    <h1>News Articles</h1>
    <c:forEach var="info" items="${newsList}">
        <h2>${article.title}</h2>
        <p>${article.content}</p>
        <p>${article.source}</p>
        <p>${article.uploadTime}</p>
        <img src="${article.imageUrl}" alt="Article Image">
        <!-- 필요한 다른 정보도 동일한 방식으로 표시 가능 -->
    </c:forEach>
</body>
</html>
