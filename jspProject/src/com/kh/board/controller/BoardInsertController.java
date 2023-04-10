package com.kh.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;
import com.kh.board.model.vo.Category;
import com.kh.common.MyFileRenamePolicy;
import com.oreilly.servlet.MultipartRequest;

/**
 * Servlet implementation class BoardInsertController
 */
@WebServlet("/insert.bo")
public class BoardInsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardInsertController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 카테고리 목록 조회해오기
		ArrayList<Category> list = new BoardService().categoryList();
		
		request.setAttribute("clist", list);
		
		request.getRequestDispatcher("views/board/boardEnrollFrom.jsp").forward(request, response);
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		/*String category = request.getParameter("category");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String file = request.getParameter("upfile"); // 파일 경로만 나옴. 사용자가 아니면 서버에서 보이지 않음
		// 그냥 폼태그로 보내는게 아니고, 파일의 전송 방식을 변경해야 함.
		// 폼태그에 enctype="multipart/form-data" 추가, 특정 라이브러리 추가
		int userNo = Integer.parseInt(request.getParameter("userNo"));
		*/
		
		/*
		 * form에서 mulitpart/form-data형식으로 전송했기 때문에 기존 request의 parameter영역에서 꺼낼 수 없음
		 * 특정 라이브러리를 사용해서 전달받아야 함(servlets.com/cos)
		 * */
		
		// cos.jar 라이브러리 추가 후 작업하기
		// form 전송 방식이 일반방식이 아니라 multipart/form-data방식이라면 request를 multipart객체로 이관시켜서 다뤄야한다.
		
		// enctype이 multipart로 작성되어 넘어왔을 때만 수정이 되도록
		if(ServletFileUpload.isMultipartContent(request)) {
			// 파일 업로드를 위한 라이브러리 cos.jar
			
			// 1. 전송되는 파일을 처리할 작업 내용(용량제한, 전달된 파일을 저장할 경로 설정)
			// 1_1. 용량 제한하기(10Mbyte) byte-kbyte-mbyte-gbyte-tbyte
			
			int maxSize = 10 * 1024 * 1024;
			
			// 1_2. 전송된 파일을 저장할 서버의 폴더 경로 알아내기
			/*
			 * 세션 객체에서 제공하는 getRealPath메소드를 사용
			 * WebContent에 board_files폴더 경로까지는 잡아주어야한다. 해당 폴더에 저장될것이기 때문에.
			 * */
			// 경로 마지막에 / 붙여주기(그 폴더안에 첨부파일 저장할거라서 어차피 / 또 붙여야하니까 미리 붙여놓음)
			String savePath = request.getSession().getServletContext().getRealPath("/resources/board_files/");
			// getservletContext() : 가장 상위폴더로
			/*
			 * 2. 전달된 파일명 수정 및 서버에 업로드 작업
			 * -HttpServletRequest request -> MultipartRequest multiRequest로 변환하기
			 * 
			 * 매개변수 생성자로 생성
			 * MultipartRequest multiRequest = new MultipartRequest(request객체, 저장할 폴더 경로, 용량제한, 인코딩값, 파일명 수정객체);
			 * 
			 * 위 구문 한 줄이 실행되는 순간 지정한 경로에 파일이 업로드됨
			 * 사용자가 올린 파일명은 그대로 업로드 하지 않는게 일반적-같은 파일명이 있는 경우나 한글/특수문자가 포함된 파일명의 경우
			 * 업로드 되는 서버에 따라 오류발생 여지있음 -> 기본적으로 파일명을 변경해주는 객체 제공(DefaultFileRenamePolicy객체
			 * 내부적으로 rename()메소드가 실행되며 파일명 수정을 해줌.
			 * 기본적으로 제공된 객체는 파일명 수정을 파일명 뒤에 숫자를 카운팅하는 형식으로만 진행
			 * ex) hello.jpg, hello1.jpg, hello2.jpg...
			 * 
			 * 해당 rename작업을 원하는 방식대로 정의하여 사용해보기(rename메소드 정의)
			 * */
			// 기본 제공 파일명 변경 객체 사용
//			MultipartRequest multiRequest = new MultipartRequest(request, savePath, maxSize, "UTF-8", new DefaultFileRenamePolicy());
			MultipartRequest multiRequest = new MultipartRequest(request, savePath, maxSize, "UTF-8", new MyFileRenamePolicy());
			
			// 3. DB에 저장할 정보들 추출(멀티파트에 담았으니까 request가 아닌 멀티파트에서 추출)
			// - 카테고리 번호, 제목, 내용, 작성자회원번호를 추출하여 board 테이블에 insert하기
			
			String category = multiRequest.getParameter("category");
			String title = multiRequest.getParameter("title");
			String content = multiRequest.getParameter("content");
			String boardWriter = multiRequest.getParameter("userNo");
			
			Board b = new Board();
			b.setCategory(category);
			b.setBoardTitle(title);
			b.setBoardContent(content);
			b.setBoardWriter(boardWriter);
			//System.out.println(b);
			
			// DB에 테이블이 board와 attachment가 따로 구성되어있으니까 게시글과 첨부파일을 따로 관리
			
			Attachment at = null; //처음에는 null로 초기화, 첨부파일이 있다면 객체 생성
			
			// multiRequest.getOriginalFileName("키");
			 //첨부파일이 있을 경우 원본명반환, 없는 경우 null 반환
			if(multiRequest.getOriginalFileName("upfile") != null) {
				// 조회가 된 경우(첨부파일이 있다)
				at = new Attachment();
				at.setOriginName(multiRequest.getOriginalFileName("upfile")); // 원본명
				at.setChangeName(multiRequest.getFilesystemName("upfile")); // 수정명(실제 서버에 업로드된 파일명)
				at.setFilePath("/resources/board_files");
			}
			
			int result = new BoardService().insertBoard(b,at);
			
			if(result > 0) {
				request.getSession().setAttribute("alert", "게시글 작성 성공");
				response.sendRedirect(request.getContextPath() + "/list.bo?currentPage=1");
			}else { 
				// 실패시에는 업로드된 파일을 지워주는 작업 필요(게시글은 없는데 업로드 파일이 자원을 쓰고 있어서)
				if(at != null) {
					// 해당 파일 경로 잡아서 File객체 생성 후 delete메소드로 파일 삭제 작업
					new File(savePath + at.getChangeName()).delete();
				}
				request.setAttribute("errorMsg", "게시글 작성 실패");
				request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
			}
			
		}
		
		
		
	}

}
