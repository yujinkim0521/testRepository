<%@page import="com.kh.board.model.vo.Attachment"%>
<%@page import="com.kh.board.model.vo.Board"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    Board b = (Board)request.getAttribute("board");
    Attachment at = (Attachment)request.getAttribute("at");
    
    request.getSession().setAttribute("at", at);
    
   // request.setAttribute("at", at); // 이게 안되는거같은데.....
    		//System.out.println(at); // 여기선 at가 조회되는데 servlet에서 null로 나와용.. 
    		// 이게 왜 안되냐면,, HTTP  프로토콜에서는 Request에 대해 Response 가 완료 되면 클라이언트와 연결이 종료(일회성)  ->세션사용
    		//System.out.println(b);
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
 table, table>tr, table th, table td{
 	border: 1px solid white;
	color: white;
 }
 .outer{
	background-color: black;
 }
</style>
</head>
<body>
<%@ include file = "../common/menubar.jsp" %>
	<div class="outer">
		<br>
	<h2 align="center" style="color:white">일반게시판 상세보기</h2>
	<br>
	<table id="detail-area" align="center">
		<tr>
			<th width="80">카테고리</th>
			<td width="60"><%=b.getCategory() %></td>
			<th width="60">제목</th>
			<td width="200"><%=b.getBoardTitle() %></td>
		</tr>
		<tr>
			<th>작성자</th>
			<td><%=b.getBoardWriter() %></td>
			<th>작성일</th>
			<td><%=b.getCreateDate() %></td>
		</tr>
		<tr>
			<th height="200">내용</th>
			<td colspan="3"><%=b.getBoardContent() %></td>
		</tr>
		<tr>
			<th>첨부파일</th>
			<td colspan="3">
			<%if(at == null){ %>
			<!-- 첨부파일이 없는 경우 -->
			첨부파일이 없습니다.
			<%}else{ %>
			<!-- 첨부파일이 있는 경우 -->
			<a href="<%=contextPath + at.getFilePath() + '/' + at.getChangeName()%>" download><%=at.getOriginName()%></a></td>
			<%} %>
		</tr>
	</table>
	<br><br>
	<%if(loginUser != null && loginUser.getUserId().equals(b.getBoardWriter())){ %>
	<div align="center">
		<button onclick="location.href='<%=contextPath %>/update.bo?bno=<%=b.getBoardNo() %>'" class="btn btn-info">수정하기</button>
		<button onclick="location.href='<%=contextPath %>/delete.bo?bno=<%=b.getBoardNo() %>'" class="btn btn-danger">삭제하기</button>
	</div>
	<% }%>


	</div>
</body>
</html>