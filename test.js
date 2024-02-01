/*
var page = {
			pageNo: pageNo,
			recordsTotal: result.recordsTotal,
			pageUnit:$('#pageUnit').val()
	};
*/

createPagination: function cc(targetId, pages) { 
    var pageNo   = Number(pages.pageNo); // 
    var pageSize  = Number(pages.pageUnit) || 10;
    var totalPages  =  Math.ceil(parseInt(pages.recordsTotal) / pageSize); // 

    var pagingHtml = ''; // 계산된 값에 기반하여 페이징에 대한 html 생성

    var pagingIndex = parseInt((pageNo-1)/pageSize); // 현재 페이징 그룹 계산 : ( 현재페이지-1 / 페이지 크기 ) 
    var pagingStart = (pagingIndex*pageSize)+1; // 시작 페이지 번호
    var pagingEnd  = (pagingIndex+1)*pageSize; // 끝 페이지 번호

    if (pagingEnd > totalPages){  // 끝 페이지 번호가 전체 페이지보다 크다면
        pagingEnd = totalPages; // 끝 페이지를 전체 페이지로 
    }

    /*
    a 태그의 href 어트리뷰트는 자바스크립트 인식 안함.
    인식하게끔 [javascript:]
    라고 선언부를 줘서 인식하게끔 하는 것.
    이게 싫으면 javascript:void(0); 등으로 href 화면이동 방지하고 onclick="" 에 함수이름 넣어주면됨.
    그렇다면 이 둘은 무슨차이인가? 단순한 취향차이는 아니고
    href는 본래 자바스크립트 액션을 취하는 어트리뷰트가 아니기에 this 객체를 넘겨줄 수 없음.
    onclick은 this 넘길 수 있음.
    */

    var onclickNm = (pages && pages.onclickNm) || 'fnSelectList'; // fn : function , "Select" => 동작 : 조회 기능 "List" => 데이터 형식 : 리스트
    onclickNm = 'javascript:'+onclickNm; // href에 동작시키기 위해 'javascript' 붙임


    var before = pagingStart - 1;
    pagingHtml += '<ul class="pagination">';
    if (pageNo > pageSize) {
        pagingHtml += '<li class="page-item">';
        pagingHtml += '    <a class="page-link first" href="'+onclickNm+'"><i class="xi-step-backward"></i><span class="sr-only">첫 페이지</span></a>';
        pagingHtml += '</li>';
        pagingHtml += '<li class="page-item">';
        pagingHtml += '    <a class="page-link previous" href="'+onclickNm+'('+before+');"><i class="xi-play"></i><span class="sr-only">이전 페이지</span></a>';
        pagingHtml += '</li>';
    } else {
        pagingHtml += '<li class="page-item none">';
        pagingHtml += '    <a class="page-link first" href="javascript:void(0);"><i class="xi-step-backward"></i><span class="sr-only">첫 페이지</span></a>';
        pagingHtml += '</li>';
        pagingHtml += '<li class="page-item none">';
        pagingHtml += '    <a class="page-link previous" href="javascript:void(0);"><i class="xi-play"></i><span class="sr-only">이전 페이지</span></a>';
        pagingHtml += '</li>';
    }

    for (var i = pagingStart; i <= pagingEnd; i++) {
        if(i == pagingStart){
            if (pageNo == i) {
                pagingHtml += '<li class="page-item"><a href=\"'+onclickNm+'(' + i + '); " class="page-link active">'+ i +'</a></li>';
            }else {
                pagingHtml += '<li class="page-item"><a href=\"'+onclickNm+'(' + i + '); " class="page-link">'+ i +'</a></li>';
            }
        }else{
            if (pageNo == i) {
                pagingHtml += '<li class="page-item"><a href=\"'+onclickNm+'(' + i + '); " class="page-link active">'+ i +'</a></li>';
            } else {
                pagingHtml += '<li class="page-item"><a href=\"'+onclickNm+'(' + i + '); " class="page-link">'+ i +'</a></li>';
            }
        }
    }

    var after = pagingEnd + 1;
    var lastIndex = parseInt((totalPages-1)/pageSize);
    var last = (lastIndex*pageSize)+1;


    if (pagingEnd < totalPages) {
//				pagingHtml += '  <li class="nav-item next"><a href=\"javascript:fnSelectList(' + after + ');\" class="nav-link"> <i class="ic-play-right"></i><span class="sr-only">다음 페이지</span> </a></li><li class="nav-item last"><a href=\"javascript:fnSelectList(' + totalPages + ');\" class="nav-link"> <i class="ic-next"></i><span class="sr-only">마지막 페이지</span></a></li>';
        pagingHtml += '<li class="page-item">';
        pagingHtml += '    <a class="page-link next" href=\"'+onclickNm+'(' + after + ');\"><i class="xi-play"></i><span class="sr-only">다음 페이지</span></a>';
        pagingHtml += '</li>';
        pagingHtml += '<li class="page-item">';
        pagingHtml += '    <a class="page-link last" href=\"'+onclickNm+'(' + last + ');\"><i class="xi-step-forward"></i><span class="sr-only">마지막 페이지</span></a>';
        pagingHtml += '</li>';
    }else{
        pagingHtml += '<li class="page-item none">';
        pagingHtml += '    <a class="page-link next" href="javascript:void(0);"><i class="xi-play"></i><span class="sr-only">다음 페이지</span></a>';
        pagingHtml += '</li>';
        pagingHtml += '<li class="page-item none">';
        pagingHtml += '    <a class="page-link last" href="javascript:void(0);"><i class="xi-step-forward"></i><span class="sr-only">마지막 페이지</span></a>';
        pagingHtml += '</li>';
    }
    pagingHtml += '</ul>'
    $("#"+targetId).empty().append(pagingHtml);
}