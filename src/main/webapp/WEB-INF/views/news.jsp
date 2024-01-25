<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script src="resources/js/jquery/jquery-3.7.1.js"></script>
<script src="resources/js/bootstrap/bootstrap.bundle.js"></script>

<link href="resources/css/bootstrap/bootstrap.css" rel="stylesheet" />
<title>News Page</title>

<script>
    var newsList = [];
    var currentPage = 1;

    function initCrawling() {
        $.ajax({
            type: "GET",
            url: "/initCrawling",
            data: {},
            success: function (response) {
                console.log(response)
            },
            error: function () {
                alert("Error crawling.");
            }
        });
    }

    function loadNews(page, pageSize) {
        $.ajax({
            type: "GET",
            url: "/selectNewsList",
            data: {
                page: page,
                pageSize: pageSize
            },
            success: function (response) {
            	console.log(response);
                newsList = response.newsList;
                displayNews();
                console.log(newsList);
            },
            error: function () {
                alert("load News Error");
            }
        })
    }
    // 테이블 갱신 함수
    function displayNews() {
        // 기존에 표시된 내용 초기화
        $("#newsTableBody").empty();

        // newsList에 있는 데이터를 테이블에 추가
        for (var i = 0; i < newsList.length; i++) {
            var news = newsList[i];
            var imageUrl = news.imageDto ? news.imageDto.imageUrl : ""; // 이미지가 있는 경우만 URL 가져오기

            // 각 데이터를 테이블에 추가하는 로직
            var row = "<tr>" +
                "<td>" + news.title + "</td>" +
                "<td>" + news.content + "</td>" +
                "<td>" + news.publisher + "</td>" +
                "<td>" + news.upload + "</td>" +
                "<td><img src='" + imageUrl + "' alt='Image'></td>" +
                "</tr>";

            // 생성한 행을 테이블에 추가
            $("#newsTableBody").append(row);
        }
    }

    // 버튼 클릭 시 다음 페이지 호출
    $("button").on("click", function () {      
        loadNews(currentPage++, 20);        
    });

    // 초기 페이지 로드
    $(document).ready(function () {
        // 초기 페이지 로딩 시에도 displayNews 함수 호출
        loadNews(currentPage, 20);
    });

    //버튼 클릭 시 params를 ajax 통신으로 보내도록 수정
    $("button").on("click", function () {
        var url = "/initCrawling/button";
        var params = {
            page: currentPage,
            pageSize: 20
        };

        $.ajax({
            type: "GET",
            url: url,
            data: params,
            dataType: "json",
            success: function (response) {
                newsList = response.newsList;
                displayNews();
                console.log(newsList);
            },
            error: function () {
                alert("btn error");
            }
        });
    });
</script>

<div class="container pt-5">
    <button class="btn btn-primary" type="submit" style="float: right; position: relative;">Button</button>
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
        <ul class="pagination" id="pagination">
            <li class="page-item"><a class="page-link" href="#">Previous</a></li>
            <li class="page-item"><a class="page-link" href="#">1</a></li>
            <li class="page-item"><a class="page-link" href="#">2</a></li>
            <li class="page-item"><a class="page-link" href="#">3</a></li>
            <li class="page-item"><a class="page-link" href="#">Next</a></li>
        </ul>
    </nav>
</div>
