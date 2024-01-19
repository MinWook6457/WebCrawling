<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<title>News Page</title>

<script>
	
</script>

<h2>News List</h2>
<c:forEach var="news" items="${newsList}"> <!-- 컨트롤러에서 받아온 newsList를 반복을 돌면서 실행 -->>
    <div>
        <h3>Title : ${news.title}</h3>
        <p>Content : ${news.content}</p>
        <p>Publisher: ${news.publisher}</p>
        <p>Upload Date: ${news.upload}</p>
        <p>URL: ${news.url}</p>
        
        <!-- 이미지 출력 -->>
         <img src="${imagePath}/${news.url}.jpg" alt="News Image">
    </div>
    <hr/>
</c:forEach>