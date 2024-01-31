<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script src="resources/js/jquery/jquery-3.7.1.js"></script>
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>
<script src="resources/js/bootstrap/bootstrap.bundle.js"></script>

<link href="resources/css/bootstrap/bootstrap.css" rel="stylesheet" />
<title>News Page</title>

<style>
#pagination {
	place-content: center;
}
</style>

<script>
// 	var newsList = [];
	
// 	var imageUrl;
	
	$(document).ready(function() {
// 		alert("초기 페이지를 로드합니다.");
		loadNews(1);
		

		$(".dropdown-item").on("click", function() {
			currentPage = 1; // 드랍 다운 메뉴로 크기 변경 시 마다 초기 페이지로 초기화
			loadNews(currentPage); 			
		})

		// 페이지네이션 클릭 이벤트 처리
// 		$("#pagination").on("click", "a.page-link", function(event) {
// 			event.preventDefault();
// 			var clickedPage = $(this).data("page");

// 			if (clickedPage === "Prev") {
// 				if (currentPage > 1) {
// 					currentPage--;
// 					console.log("Previous clicked. New Page:", currentPage);
// 					loadNews(currentPage,defaultPageUnit);
// 				}
// 			} else if (clickedPage === "Next") {
// 				currentPage++;
// 				console.log("Next clicked. New Page:", currentPage);
// 				loadNews(currentPage,defaultPageUnit);
// 			} else {
// 				currentPage = clickedPage;
// 				console.log("Page clicked. New Page:", currentPage);
// 				loadNews(currentPage,defaultPageUnit);
// 			}
// 		});
	});

	function initCrawling() {
		$.ajax({
			type : "GET",
			url : "/crawl/news/initCrawling",
			data : {
				currentPage : currentPage,
				pageUnit : pageUnit
			},
			success : function(res) {
// 				newsList = response.initResponse.newsList;
// 				createPagination(response.ajaxResponse.totalItems);
				loadNews(res.currentPage,res.pageUnit);
			},
			error : function() {
				alert("Error crawling.");
			}
		});
	}

	function loadNews(pageNo) {		
		var param = {
			pageNo : pageNo,
			defaultPageUnit : $('#pageUnit').val()
		}
		$.ajax({
			type : "POST",
			url : "/crawl/news/selectNewsList",
			contentType : "application/json;",
			data : JSON.stringify(param),
			success : function(res) {
				
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
						html += "<td><img src='" + list[i].imageDto.image_url + "'></td>";
					} else {
						html += "<td>No Image</td>";
					}
					html += '</tr>';
				}
				$("#newsTableBody").html(html);
				param.totalPages = res.totalPages;
				param.totalElements = res.totalElements;
				param.onclickNm = 'loadNews';
				createPagination(param);
				
			},
			error : function(res) {
				alert("페이지 네이션 로드 실패" + res);
			}
		})
	}
	
	/*
	var param = {
			page : page,
			pageSize : pageSize
			totalPages = res.totalPages;
			totalElements = res.totalElements;
		}
	*/
	
	// 페이지네이션 생성 함수
	
	/*
Prev : 이전 페이지 (이전 그룹)
Next : 다음 페이지 (다음 그룹)
Fisrt : 첫 페이지로
End : 끝 페이지로
	*/
function createPagination(pages) {
    var pageNo = Number(pages.pageNo);
    var pageUnit = Number(pages.defaultPageUnit);
    var totalPages = pages.totalPages;
    var totalElements = pages.totalElements;

    console.log('createPagination is called')
    console.log(pages);

    console.log('받아온 페이지 값 : ' + totalPages , totalElements)
 

    $("#pagination").empty(); // 기존 페이지네이션 초기화
    var currentGroup = Math.ceil(pageNo / pageUnit);
    var first = (currentGroup - 1) * pageUnit + 1;
    var last = Math.min(currentGroup * pageUnit, totalPages);

    var onclickNm = (pages && pages.onclickNm) || 'fnSelectList';
    onclickNm = 'javascript:' + onclickNm;

    var totalPageNo = totalElements / totalPages; // 총 페이지 번호

    console.log("총 페이지 넘버 : " + totalPageNo);

    var before = Math.max(first - 1, 1);
    var after = Math.min(last + 1, totalPages + 1);

    var html = '<ul class="pagination">'; // html 생성
    if(pageNo > pageUnit) {
    	html += "<li class='page-item'><a class='page-link' href='" + onclickNm + "('1')'>First</a></li>";
    }else {
    	html += "<li class='page-item disabled'><a class='page-link' href='javascript:void(0)'>First</a></li>";
    }
    
    if(pageNo > 1) {
    	html += "<li class='page-item'><a class='page-link' href='" + onclickNm + "("+before+")'>Prev</a></li>";
    }else {
    	html += "<li class='page-item disabled'><a class='page-link' href='javascript:void(0)'>Prev</a></li>";
    }
    
    for (var i = first; i <= last; i++) {
        html += "<li class='page-item" + (pageNo == i ? ' active' : '') + "'><a class='page-link' href='" + onclickNm + "(" + i + ")'>" + i + "</a></li>";
    }
    
    if (pageNo < totalPages) {
        html += "<li class='page-item'><a class='page-link' href='" + onclickNm + "(" + after + ")'>Next</a></li>";
    }else{
    	html += "<li class='page-item' disabled><a class='page-link' href='javascript:void(0)'>Next</a></li>";
    }
    
    if(currentGroup == Math.ceil(totalPages/pageUnit)) {
    	html += "<li class='page-item disabled'><a class='page-link' href='javascript:void(0)'>End</a></li>";
    }else {
    	html += "<li class='page-item'><a class='page-link' href='" + onclickNm + "(" + last + ")'>End</a></li>";
    }
    
//     if (currentGroup < totalPageNo) {
//     	if(currentGroup == 1){
//             html += "<li class='page-item' disabled><a class='page-link' href='" + onclickNm + "(" + before + ")'>Previous</a></li>";
//     	}else{
//             html += "<li class='page-item'><a class='page-link' href='" + onclickNm + "(" + before + ")'>Previous</a></li>";
//     	}
//     }

//     for (var i = first; i <= last; i++) {
//         html += "<li class='page-item'><a class='page-link" + (pageNo == i ? ' active' : '') + "' href='" + onclickNm + "(" + i + ")' data-page='" + i + "'>" + i + "</a></li>";
//     }

//     if (currentGroup < totalPageNo) {	
//         html += "<li class='page-item'><a class='page-link' href='" + onclickNm + "(" + after + ")'>Next</a></li>";
//         html += "<li class='page-item'><a class='page-link' href='" + onclickNm + "(" + last + ")' >End</a></li>";
//     }else if(pageNo == totalPages){
//     	html += "<li class='page-item' disabled><a class='page-link' href='javascript:void(0)'>Next</a></li>";
//     }
    
    html += '</ul>';
    $("#pagination").append(html);
}
	
// Prev 버튼 클릭 이벤트 처리
// $("#pagination").on("click", "a[data-page='Prev']", function (event) {
//     event.preventDefault();
//     if (currentPage > 1) {
//         currentPage--;
//         loadNews(currentPage, pageUnit);
//     }
// });

// $("#pagination").on("click", "a[data-page='Next']", function (event) {
//     event.preventDefault();
//     if (currentGroup < totalPages) {
//         currentPage++;
//         loadNews(currentPage, pageUnit);
//     }
// });

</script>



<div class="container pt-5">
	<div class="dropdown-center" id="pageDiv" style="float: right;">
		<input type="hidden" name="pageUnit" id="pageUnit" value="10" />
		<button class="btn btn-secondary dropdown-toggle" type="button"
			data-toggle="dropdown" aria-expanded="false">Crawling
			Display Menu</button>
		<ul class="dropdown-menu">
			<li><a class="dropdown-item" href="javascript:void(0);"
				data-pageunit="10">10개씩 보기</a></li>
			<li><a class="dropdown-item" href="javascript:void(0);"
				data-pageunit="50">50개씩 보기</a></li>
			<li><a class="dropdown-item" href="javascript:void(0);"
				data-pageunit="0">전체 보기</a></li>
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
