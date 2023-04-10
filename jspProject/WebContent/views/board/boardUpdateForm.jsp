<%@page import="com.kh.board.model.vo.Attachment"%>
<%@page import="com.kh.board.model.vo.Board"%>
<%@page import="com.kh.board.model.vo.Category"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    ArrayList<Category> list = (ArrayList<Category>)request.getAttribute("clist");
    Board b = (Board)request.getAttribute("b");
    Attachment at = (Attachment)request.getAttribute("at");
  
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	#update-form>table{
		border: 1px solid white;
	}
	#update-form input, textarea{
		width: 100%;
		box-sizing: border-box;
	}
</style>
</head>
<body>
	<%@include file="../common/menubar.jsp" %>
	
	<div class="outer">
		<h2 align="center">글수정페이지</h2>
		<!-- 카테고리, 제목, 첨부파일, 작성자번호(여기서 hidden으로 넘기거나, 서블릿에서 세선에 담아두기 -->
		<form action="<%=contextPath%>/update.bo" method="post" id="update-form" enctype="multipart/form-data">
			<input type="hidden" name="boardNo" value="<%=b.getBoardNo() %>"><!-- 게시글번호 숨겨보내기 -->
			<!-- 카테고리 테이블에서 조회해온 카테고리 목록 선택상자 만들기 -->
			<script>
			$(function(){
				//카테고리 작업
				// option에 있는 텍스트와 조회해 온 게시글의 카테고리와 일치여부 판단 -> 선택되어있게 보여주기
				$("#update-form option").each(function(){
					// 현재 접근된 요소객체의 text와 조회해온 카테고리가 같다면
					if($(this).text() =="<%=b.getCategory()%>"){ 
						// 해당 요소를 선택되어있게 만들기
						$(this).attr("selected", true);
					}
				});
			});
			
			</script>
			
			<table align="center">
				<tr>
					<th width="100">카테고리</th>
					<td width="500">
						<select name="category">
							<%for(Category c : list){ %>
							<option value="<%=c.getCategoryNo() %>"><%=c.getCategoryName() %></option>
							<%} %>
						</select>
					</td>
				</tr>
				<tr>
					<th>제목</th>
					<td><input type="text" name="title" required value="<%=b.getBoardTitle()%>"></td>
				</tr>
				<tr>
					<th>내용</th>
					<td><textarea name="content" rows="10" cols="30" required><%=b.getBoardContent() %></textarea>
					</td>
				</tr>
				<tr>
					<th>첨부파일<th>
					<td>
						<%if(at != null){ %>
						<!-- 기존 첨부파일이 있었을 경우 수정할 때 첨부파일 정보를 보내야한다. -->
						<!--파일번호, 변경된 파일명 전달하기 -->
							<%=at.getOriginName() %>
							<input type="hidden" name="fileNo" value="<%=at.getFileNo() %>">
							<input type="hidden" name="originFileName" value="<%=at.getChangeName() %>">
						<%} %>
						<input type="file" name="reUpfile">
				
					</td>
				</tr>
			</table>
			<br>
			<div align="center">
			<button type="submit">게시글 등록</button>
			<button type="reset">초기화</button>
			</div>
		</form>
	</div>
	
</body>
</html>