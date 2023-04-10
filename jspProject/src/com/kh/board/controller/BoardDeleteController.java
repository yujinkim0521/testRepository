package com.kh.board.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Attachment;

/**
 * Servlet implementation class BoardDeleteController
 */
@WebServlet("/delete.bo")
public class BoardDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardDeleteController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// a태그니까 get방식, 폼은 멀티타입방식 아님.
		
		int boardNo = Integer.parseInt(request.getParameter("bno")); // url에서 설정한 키값 bno추출
//		Attachment at = (Attachment)request.getSession().getAttribute("at");
//		System.out.println(boardNo); //확인완룡~~~
//		System.out.println(at); // 확인완룡~~~
//		
//		int fileNo = 0;
//		int result = 0;
//		
//		if(at !=null) {
//			fileNo = at.getFileNo();
//			result = new BoardService().deleteBoard(boardNo, fileNo);
//		}else {
//			result = new BoardService().deleteBoard(boardNo, fileNo);
//		}
		
		int result = new BoardService().deleteBoard(boardNo);
		
		
		if(result > 0) { // 둘 다 삭제 성공
			request.getSession().setAttribute("alertMsg", "삭제했습니다.");
			response.sendRedirect(request.getContextPath()+"/list.bo?currentPage=1");
		}else { 
			request.setAttribute("errorMsg", "삭제못했어요");
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
