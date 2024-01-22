<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<title>News Page</title>
<style>
/* Optional: Add some styling for better presentation */
body {
	font-family: Arial, sans-serif;
	margin: 20px;
}

h2 {
	color: #333;
}

div {
	margin-bottom: 20px;
}

img {
	max-width: 100%;
	height: auto;
}
</style>
</head>
<body>

	<h2>News List</h2>

	<c:forEach var="news" items="${newsList}">
		<div>
			<h3>Title: ${news.title}</h3>
			<p>Content: ${news.content}</p>
			<p>Publisher: ${news.publisher}</p>
			<p>Upload Date: ${news.upload}</p>
			<p>URL: ${news.url}</p>


			<!-- Display the image -->
			<img src="${news.imageDto.imageUrl}.jpg">
		</div>
		<hr />
	</c:forEach>

</body>
</html>
