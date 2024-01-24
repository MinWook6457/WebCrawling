<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script src="resources/js/jquery/jquery-3.7.1.js"></script>
<script src="resources/js/bootstrap/bootstrap.bundle.js"></script>

<link href="resources/css/bootstrap/bootstrap.css" rel="stylesheet" />
<title>News Page</title>

<script>
	function crawling() {
		$.ajax({
			type : "GET",
			url : "/crawling",
			data : application.json,
			success : function(response) {
				alert(response);
			},
			error : function() {
				alert("Error crawling.");
			}
		});
	}

	function loadNews(page, pageSize) {
		$.ajax({
			type : "GET",
			url : "/selectNewsList",
			data : {
				page : page,
				pageSize : pageSize
			},
			dataType : "json",
			success : function(response) {
				newsList = response.newsList
				
				console.log(newsList)
			},
			error : function() {
				alert("Error loading News")
			}
		})
	}

	$(document).ready(function() {
		// 페이지 로딩 시 자동으로 크롤링 초기화
		crawling();
	});
</script>


<div class="container pt-5">
	<h2>News List</h2>
	<table class="table">
		<thead>
			<tr>
				<th>Title</th>
				<th>Content</th>
				<th>Publisher</th>
				<th>Upload Date</th>
				<th>Image</th>
			</tr>
		</thead>
	</table>
	<nav aria-label="Page navigation">
		<ul class="pagination" id="pagination">

		</ul>
	</nav>
</div>
