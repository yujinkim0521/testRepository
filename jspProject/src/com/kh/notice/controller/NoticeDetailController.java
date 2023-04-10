package com.kh.notice.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.kh.notice.model.service.NoticeService;
import com.kh.notice.model.vo.Notice;

/**
 * Servlet implementation class NoticeDetailController
 */
@WebServlet("/detail.no")
public class NoticeDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeDetailController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 공지사항 하나의 정보를 조회해와서 request에 담아 위임하기
		int noticeNo = Integer.parseInt(request.getParameter("nno"));
		//System.out.println(nno);
		
		// 해당 글번호로 그 글 정보에 대해 조회수를 증가시키고 난 뒤를 조회해오기
		// 조회수 증가 메소드
		int result = new NoticeService().increaseCount(noticeNo);
		
		// selectNotice
		
		if(result > 0) { // 조회수 증가 잘 되었으면 게시글 정보 조회
			
			Notice n = new NoticeService().selectNotice(noticeNo);
			//System.out.println(n);
						
			request.setAttribute("notice", n);
			request.getRequestDispatcher("views/notice/noticeDetailView.jsp").forward(request, response); // 페이지위임
			
		}else {// 조회수 증가 실패시 에러페이지
			request.setAttribute("errorMsg", "공지사항 조회실패");
			request.getRequestDispatcher("views/common/errorPage/jsp").forward(request, response);
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
