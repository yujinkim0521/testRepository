package com.kh.board.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Board;
import com.kh.common.model.vo.PageInfo;

/**
 * Servlet implementation class BoardListController
 */
@WebServlet("/list.bo")
public class BoardListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardListController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 페이징 처리하기
		int listCount; // 현재 총 게시글의 개수
		int currentPage; // 현재 페이지
		int pageLimit; // 페이지 하단에 보여질 페이징바의 최대 페이지 개수
		int boardLimit; // 한 페이지에서 보여질 게시글의 최대 개수
		
		int maxPage; // 가장 마지막 페이지가 몇인지(총 페이지 개수)
		int startPage; // 페이지 하단에 보여질 페이징바의 시작 수 
		int endPage; // 페이지 하단에 보여질 페이징바의 끝 수
		
		// listCount : 총 게시글 개수 구하기
		listCount = new BoardService().selectListCount();
		
		// currentPage : 현재 페이지
		currentPage = Integer.parseInt(request.getParameter("currentPage"));
		
		// pageLimit : 현재 페이지 하단에 보여질 페이징 바의 페이지 최대개수(목록단위)
		pageLimit = 10;
		
		// boardLimit : 한 페이지에 보여질 게시글 개수(게시글 단위)
		boardLimit = 10;
		
		// maxPage : listCount와 boardLimit의 영향을 받는 수(나누고 올림)
		maxPage = (int)(Math.ceil((double)listCount/boardLimit)); // 정수형 int끼리 계산하면 소숫점 안나오니까 double로 형변환 후 산수실행
		//System.out.println(maxPage);
		
		// startPage : 페이징바의 시작 수-pageLimit과 CurrentPage의 영향을 받는 수
		// startPage = 1, 11, 21, 31, 41, ... ((currentPage-1)/pageLimit)*pageLimit+1
		startPage = ((currentPage-1)/pageLimit)*pageLimit+1;
		
		// endPage
		endPage = startPage + pageLimit -1;
		
		if(endPage > maxPage) { // 끝 수가 총 페이지 수보다 크다면 해당 수를 총 페이지수로 바꿔주기(13페이지까지 있는데 페이징바가 20까지로 나오는 것 방지)
			endPage = maxPage;
		}
		
		// 페이지 정보들을 하나의 공간에 담아보내기(vo 만듦.)
		PageInfo pi = new PageInfo(listCount, currentPage, pageLimit, boardLimit, maxPage, startPage, endPage);
//		System.out.println(pi);
		// 현재 사용자가 요청한 페이지에 (currentPage)에 보여질 게시글 리스트 조회
		
		ArrayList<Board> list = new BoardService().selectList(pi);
		
//		for(Board b : list) {
//			System.out.println(b);
//		}
		
		// 조회된 리스트와 페이징정보 request로 보내기
		request.setAttribute("list", list);
		request.setAttribute("pi", pi);
		request.getRequestDispatcher("views/board/boardListView.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
