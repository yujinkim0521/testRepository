<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import = "java.util.ArrayList, com.kh.notice.model.vo.Notice"%>
    <%
    // 담은 리스트 꺼내주기
    ArrayList<Notice> list = (ArrayList<Notice>)request.getAttribute("list");
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title> 
<style>
    .outer{
        background-color: black;
        color: white;
        width: 1000px;
        height: 500px;
        margin: auto;
        margin-top: 50px;
    }

    .list-area{
        border: 1px solid white;
        text-align: center;
    }

    .list-area>tbody>tr:hover{
        background-color: grey;
        cursor: pointer;
    }
</style>
</head>
<body>

<%@include file="../common/menubar.jsp" %>

<div class="outer">
    <h2 align="center">공지사항</h2>
    <br>
    <!-- 공지사항 작성은 관리자만 가능하도록 조건 부여하기 -->
    <!-- null인지 확인하는 작업을 먼저 작성해야한다. 접근전에 확인 후 벗어나기위해(논리추론) -->
    <%if(loginUser != null && loginUser.getUserId().equals("admin")){ %>
    <div align="center">
        <a href="<%=contextPath %>/insert.no" class="btn btn-info">공지사항 작성</a>
        <br><br>
    </div>
	<%} %>
    <table class="list-area" align="center">
        <thead>
            <tr>
                <th>글번호</th>
                <th width="400px">글제목</th>
                <th width="100px">작성자</th>
                <th>조회수</th>
                <th width="100px">작성일</th>
            </tr>
        </thead>
        <tbody>
            <%if(list.isEmpty()){ %> <!-- 리스트가 비어있다면 -->
            	<tr>
                    <td>존재하는 공지사항이 없습니다.</td>
                </tr>
            <%} else{%> <!-- 목록이 있으면 반복문으로 전부 출력 -->
            	<%for(Notice n : list){ %> <!-- 순차적으로 전부 접근하는 향상된for문 사용 -->
            	<tr>
	            	<td><%=n.getNoticeNo()%></td>
	            	<td><%=n.getNoticeTitle()%></td>
	            	<td><%=n.getNoticeWriter()%></td>
	            	<td><%=n.getCount()%></td>
	            	<td><%=n.getCreateDate()%></td>
            	</tr>
            	<%} %>
            <%} %>
        </tbody>
    </table>
    </div>

    <script>
        // .list-area클래스 자손 tbody 자손 tr클릭됐을 때
        $(".list-area>tbody>tr").click(function(){
        		// 글 번호가 있어야 글 번호로 상세조회를 할 수 있으므로 글 번호 추출하여 서버에 넘기기
            console.log($(this).children().eq(0).text());

            var nno = $(this).children().eq(0).text()
            // 요청할 url?키=밸류&키=밸류...
            // 물음표 뒤의 내용을 쿼리 스트링이라고 한다.-직접 기술해서 넘기기
            // '/jsp/datail.no?nno='nno

            location.href = '<%=contextPath%>/detail.no?nno='+nno;
        });
    </script>
</body>
</html>