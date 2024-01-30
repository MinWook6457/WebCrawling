<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script src="resources/js/jquery/jquery-3.7.1.js"></script>
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>
<script src="resources/js/bootstrap/bootstrap.bundle.js"></script>
<!-- <script src="/static/js/bootstrap.bundle.js"></script> -->



<link href="resources/css/bootstrap/bootstrap.css" rel="stylesheet" />
<title>News Page</title>

<style>
#pagination {
	place-content: center;
}
</style>

<script>
// 	var newsList = [];
	var currentPage = 1;  // 초기 페이지

// 	var imageUrl;
	
	$(document).ready(function() {
// 		alert("초기 페이지를 로드합니다.");
// 		initCrawling(); 
		loadNews(1);

		$(".dropdown-item").on("click", function() {
			pageSize = parseInt($(this).text());
			currentPage = 1; // 드랍 다운 메뉴로 크기 변경 시 마다 초기 페이지로 초기화
			loadNews(currentPage); 			
		})

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

	function initCrawling() {
		$.ajax({
			type : "GET",
			url : "/crawl/news/initCrawling",
			data : {
				pageSize : pageSize,
				currentPage : currentPage
				},
			success : function(response) {
// 				newsList = response.initResponse.newsList;
// 				createPagination(response.ajaxResponse.totalItems);
				loadNews(1);
			},
			error : function() {
				alert("Error crawling.");
			}
		});
	}

	function loadNews(page) {
		var param = {
			page : page	
		};
		
		$.ajax({
			type : "POST",
			url : "/crawl/news/selectNewsList",
			contentType : "application/json;",
			data : JSON.stringify(param),
			success : function(res) {
				console.log(res)
				var list = res.content;
				var html = '';
				
				$("#newsTableBody").empty();
		
				// newsList에 있는 데이터를 테이블에 추가
				for (var i = 0; i < list.length; i++) {
					html += '<tr>';
					html += '	<td>' + list[i].title + '</td>';
					html += '	<td>' + list[i].content + '</td>';
					html += '	<td>' + list[i].publisher + '</td>';
					html += '	<td>' + list[i].upload + '</td>';
					if (list[i].imageDto) {
						html += "<td><img src='" + "${list[i].imageDto.imageUrl}.jpg" + "'></td>";
					} else {
						html += "<td>No Image</td>";
					}
					html += '</tr>';
				}
				$("#newsTableBody").html(html);
				createPagination(res.totalPages, res.totalElements);
				
			},
			error : function() {
				alert("페이지 네이션 로드 실패" + page);
			}
		})
	}
	
// 	function postPageData(){
// 		$.ajax({
// 			type : "POST",
// 			url : 
			
// 		})
// 	}
		
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
	function createPagination(totalPages, totalElements) {
		var pageUnit = 5; // 기본 pageUnit 설정
		
		var html = ''; // html 생성 
		$("#pagination").empty(); // 기존 페이지네이션 초기화
		var currentGroup = Math.ceil(totalPages/pageUnit); 
		var first = (currentGroup * pageUnit)+1;
		var last = (currentGroup+1) * pageUnit;
		
		var onclickNm = (totalPages && totalPages.onclickNm) || 'fnSelectList';
		onclickNm = 'javascript:' + onclickNm;
		
		var pageSize = totalElements / totalPages;
		

		if(currentGroup > pageUnit) {
			html += "<li class='page-item'> <a class='page-link first' href='onclickNm' data-page='Prev'>Previous</a></li>";
		}
		
		for(var i=0; i<pageSize; i++) {
			html += "<li class='page-item'><a class='page-link' href='onclickNm('"+i+"')'>" + i + "</a></li>";
		}
		
		// Previous 버튼
		var prevButton = "<li class='page-item'><a class='page-link' href='onclickNm' data-page='Prev'>Previous</a></li>";
		$("#pagination").append(prevButton);

		// 숫자 버튼
		for (var i = 1; i <= pageSize; i++) {
			var pageButton = "<li class='page-item'><a class='page-link' href='onclickNm' data-page='" + i + "'>"
					+ i + "</a></li>";
			$("#pagination").append(pageButton);
		}

		// Next 버튼
		var nextButton = "<li class='page-item'><a class='page-link' href='onclickNm' data-page='Next'>Next</a></li>";
		$("#pagination").append(nextButton);
		
		 // 현재 페이지 표시
	    $("#pagination").find("[data-page='" + currentPage + "']").parent().addClass("active");
	}
</script>



<div class="container pt-5">
	<div class="dropdown-center" style="float: right;">
		<button class="btn btn-secondary dropdown-toggle" type="button"
			data-toggle="dropdown" aria-expanded="false">Crawling Display Menu</button>
		<ul class="dropdown-menu">
			<li><a class="dropdown-item" href="#">5개씩 보기</a></li>
			<li><a class="dropdown-item" href="#">10개씩 보기</a></li>
			<li><a class="dropdown-item" href="#">20개씩 보기</a></li>
		</ul>
	</div>

	<button class="btn btn-primary" type="button"
		style="float: left; position: relative;">Start Crawling</button>
	<h2 class="text-center">News List</h2>
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
		<ul class="pagination" id="pagination"></ul>
	</nav>
</div>
