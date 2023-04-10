package com.kh.board.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;
import com.kh.board.model.vo.Category;

/**
 * Servlet implementation class BoardDetailController
 */
@WebServlet("/detail.bo")
public class BoardDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardDetailController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 게시판 글 정보 조회해 올 글 번호
		int boardNo = Integer.parseInt(request.getParameter("bno"));		
		
		// 해당 게시글 번호로 조회수 증가시키는 메소드
		int result = new BoardService().increaseCount(boardNo);
		
		// 조회수 증가되었으면 게시글 정보 조회
		if(result > 0) {
			Board b = new BoardService().selectBoard(boardNo); 
			// Board의 category는 String categoryNo로 생성되어있는데, dao-sql문에서 category테이블과 조인하여 카테고리 이름으로 set함
			Attachment at = new BoardService().selectAttachment(boardNo);
			//System.out.println(at);
			
			// request에 담아서 jsp에서 꺼내기
			request.setAttribute("board", b);
			request.setAttribute("at", at);
	
			request.getRequestDispatcher("views/board/boardDetailView.jsp").forward(request, response);
			
		}else { // 조회수 증가 실패
			request.setAttribute("errorMsg", "게시글조회실패");
			request.getRequestDispatcher("view/common/errerPage.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
