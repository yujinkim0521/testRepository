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
 * Servlet implementation class BoardUpdateController
 */
@WebServlet("/update.bo")
public class BoardUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardUpdateController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 받은 글 번호로 해당 게시글 정보 조회후 수정페이지로 전달(-수정페이지는 작성하기 페이지 이용해서 만들기) boardUpdateForm.jsp
		
		int boardNo = Integer.parseInt(request.getParameter("bno"));
		//System.out.println(boardNo);
		
		Board b = new BoardService().selectBoard(boardNo);
		Attachment at = new BoardService().selectAttachment(boardNo);
		ArrayList<Category> clist = new BoardService().categoryList();
		
		request.setAttribute("b", b);
		request.setAttribute("at", at);
		request.setAttribute("clist", clist);
		
		request.getRequestDispatcher("views/board/boardUpdateForm.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 수정처리 - 첨부파일 유무차이로 생각해보기. 있으면 원래 있던 첨부파일데이터에서 수정해야함.. 없는경우 지금 글 번호 가져가서 지금 게시글에 새롭게 추가할 수 있게 해야함..
		// 성공시 detailView띄우고, 실패시 에러페이지
		request.setCharacterEncoding("UTF-8");
		
		// enctpye이 멀티파트 형식인지 확인
		if(ServletFileUpload.isMultipartContent(request)) {
			// 1. 파일 용량제한
			int maxSize = 10 * 1024 * 1024;
			// 2. 저장시킬 서버 저장경로(물리적 경로)
			String savePath = request.getSession().getServletContext().getRealPath("/resources/board_files/");
			
			// 3. 파일명 수정작업 객체 추가
			MultipartRequest multiRequest = new MultipartRequest(request, savePath, maxSize, "UTF-8", new MyFileRenamePolicy());
			
			// 4. 수정작업에 필요한 기존 데이터 추출
			int boardNo = Integer.parseInt(multiRequest.getParameter("boardNo"));
			String boardTitle = multiRequest.getParameter("title");
			String boardContent = multiRequest.getParameter("content");
			String category = multiRequest.getParameter("category"); // value=categoryNo임
			//System.out.println(category);
			
			
			Board b = new Board();
			b.setBoardNo(boardNo);
			b.setBoardTitle(boardTitle);
			b.setBoardContent(boardContent);
			b.setCategory(category);
			
			// 5. 새로 전달된 첨부파일이 있는 경우 처리하기-update 해당데이터에서 수정작업
			Attachment at = null;
			if(multiRequest.getOriginalFileName("reUpfile") != null) {
				// 첨부파일이 있으면
				at = new Attachment();
				at.setOriginName(multiRequest.getOriginalFileName("reUpfile"));
				at.setChangeName(multiRequest.getFilesystemName("reUpfile"));
				at.setFilePath("/resources/board_files");
			
				// jsp에서 히든으로 숨겨가지고온 파일번호, 변경된 이름(서버에 저장된 이름)으로 기존에 있던 첨부파일의 유무 판단.
				if(multiRequest.getParameter("fileNo") != null) {
					// 새로운 첨부파일이 있고, 기존에도 첨부파일이 있었던 경우
					// update attachment
					// 기존 파일번호(식별자)를 이용하여 데이터 변경.
					at.setFileNo(Integer.parseInt(multiRequest.getParameter("fileNo")));
					
					// 기존 첨부파일 삭제하기.
					new File(savePath + "/"+multiRequest.getParameter("originFileName")).delete();
									
				}else {
					// 기존에 첨부파일이 없었는데, 새로 파일을 첨부하는 경우
					// 현재 게시글 번호를 참조하도록 insert하기
					at.setRefBno(boardNo);
				}
				
			
			}
			// update실행
			int result = new BoardService().updateBoard(b, at);
			// 새로운 첨부파일도 없고, 기존에도 첨부파일 없는경우 -> board update
			// 새로운 첨부파일 없고, 기존에 첨부파일 없는 경우 -> board-update / attachment-insert
			// 새로운 첨부파일 있고, 기존 첨부파일 있는 경우 -> board-update / attachment-update

			if (result > 0) {
				request.getSession().setAttribute("alertMsg", "게시글 수정 성공");
				response.sendRedirect(request.getContextPath() + "/detail.bo?bno=" + boardNo);
			} else {
				request.setAttribute("errorMsg", "게시글 수정 실패");
				request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
			}

		}
	}

}
