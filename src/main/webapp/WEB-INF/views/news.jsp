<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script src="resources/js/jquery/jquery-3.7.1.js"></script>
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>
<script src="resources/js/bootstrap/bootstrap.bundle.js"></script>
<script src="/static/js/bootstrap.bundle.js"></script>



<link href="resources/css/bootstrap/bootstrap.css" rel="stylesheet" />
<title>News Page</title>

<style>
#pagination {
	place-content: center;
}
</style>

<script>
	var newsList = [];
	var currentPage = 1;
	var pageSize = 20; // 한 페이지에 표시할 아이템 수
	var imageUrl;

	function initCrawling() {
		$.ajax({
			type : "GET",
			url : "/crawl/news/initCrawling",
			data : {},
			success : function(response) {
				newsList = response.newsList;
				console.log(currentPage, pageSize);
				createPagination(pageSize);
			},
			error : function() {
				alert("Error crawling.");
			}
		});
	}

	function loadNews(page) {
		$.ajax({
			type : "GET",
			url : "/crawl/news/selectNewsList",
			data : {
				page : page
			},
			success : function(response) {
				newsList = response.newsList; // newsList 전역 변수에 서버에서 받은 데이터 할당

				displayNews(); // 데이터 표시
			},
			error : function() {
				alert("페이지 네이션 로드 실패" + page);
			}
		})
	}

	function displayNews() {
		// 기존에 표시된 내용 초기화
		$("#newsTableBody").empty();

		// newsList에 있는 데이터를 테이블에 추가
		for (var i = 0; i < newsList.length; i++) {
			var news = newsList[i];

			// 각 데이터를 테이블에 추가하는 로직
			var row = "<tr>" + "<td>" + news.title + "</td>" + "<td>"
					+ news.content + "</td>" + "<td>" + news.publisher
					+ "</td>" + "<td>" + news.upload + "</td>";

			// imageDto가 정의되어 있는 경우에만 imageUrl 속성에 접근
			if (news.imageDto) {
				row += "<td><img src='" + news.imageDto.imageUrl + "' alt='failed'></td>";
			} else {
				row += "<td>No Image</td>";
			}

			row += "</tr>";

			// 생성한 행을 테이블에 추가
			$("#newsTableBody").append(row);
		}
	}

	// 페이지네이션 생성 함수
	function createPagination(totalPages) {
		$("#pagination").empty(); // 기존 페이지네이션 초기화

		// Previous 버튼
		var prevButton = "<li class='page-item'><a class='page-link' href='#' data-page='Prev'>Previous</a></li>";
		$("#pagination").append(prevButton);

		// 숫자 버튼
		for (var i = 1; i <= totalPages; i++) {
			var pageButton = "<li class='page-item'><a class='page-link' href='#' data-page='" + i + "'>"
					+ i + "</a></li>";
			$("#pagination").append(pageButton);
		}

		// Next 버튼
		var nextButton = "<li class='page-item'><a class='page-link' href='#' data-page='Next'>Next</a></li>";
		$("#pagination").append(nextButton);
	}

	function sendSelectedMenuToServer(selectedMenu) {
		$.ajax({
			type : "POST", 
			url : "/crawl/news", // 실제 서버 URL로 수정
			data : {
				selectedMenu : selectedMenu
			},
			success : function(response) {
				console.log("Selected Menu successfully sent to the server.");
				
			},
			error : function() {
				alert("Failed to send the selected menu to the server.");
			}
		});
	}

	$(document).ready(function() {
		alert("초기 페이지를 로드합니다.");
		initCrawling();
		loadNews(currentPage);

		$(".dropdown-item").on("click", function() {
			selectedMenu = $(this).data("value");

			console.log(selectedMenu);
		})

		// 시작 버튼 클릭 이벤트 처리
		$("#startCrawlingBtn").on("click", function() {
			// 선택된 메뉴를 서버로 전송
			sendSelectedMenuToServer(selectedMenu);
		});

		// 페이지네이션 클릭 이벤트 처리
		$("#pagination").on("click", "a.page-link", function(event) {
			event.preventDefault();
			var clickedPage = $(this).data("page");

			if (clickedPage === "Prev") {
				if (currentPage > 1) {
					currentPage--;
					loadNews(currentPage);
				}
			} else if (clickedPage === "Next") {
				currentPage++;
				loadNews(currentPage);
			} else {
				currentPage = clickedPage;
				loadNews(currentPage);
			}
		});
	});
</script>



<div class="container pt-5">
	<div class="dropdown-center" style="float: right;">
		<button class="btn btn-secondary dropdown-toggle" type="button"
			data-toggle="dropdown" aria-expanded="false">Crawling
			Display Menu</button>
		<ul class="dropdown-menu">
			<li><a class="dropdown-item" href="#">5개씩 보기</a></li>
			<li><a class="dropdown-item" href="#">10개씩 보기</a></li>
			<li><a class="dropdown-item" href="#">20개씩 보기</a></li>
		</ul>
	</div>

	<button class="btn btn-primary" type="submit"
		style="float: left; position: relative;">Start Crawling</button>
	<h2 style="text-align: center;">News List</h2>
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
		<tbody id="newsTableBody"></tbody>
	</table>
	<nav aria-label="Page navigation">
		<ul class="pagination" id="pagination" style="place-content: center;"></ul>
	</nav>
</div>
