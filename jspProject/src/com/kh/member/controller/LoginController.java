package com.kh.member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.member.model.service.MemberService;
import com.kh.member.model.vo.Member;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/login.me")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*
		 * HttpServletRequest객체와 HttpServletResponse객체
		 * -request : 서버로 요청할 때의 정보들이 담겨있다. (요청시 전달값, 요청 전송 방식 등)
		 * -response : 요청에 대해 응답에 필요한 객체
		 * 
		 * GET / POST 방식 차이
		 * -GET : 사용자가 입력한 값이 url에 노출되고 데이터의 길이제한이 있으며 즐거찾기를 할 수 있다.
		 * -POST : url에 노출되지 않고 데이터 길이제한이 없으며 즐겨찾기를 할 수 없다.
		 * */
		
		// 1) post요청일 경우 전달값에 한글이 표함되어있다면 encoding설정 먼저 하기
		request.setCharacterEncoding("UTF-8");
		
		// 2) 요청시 전달한 데이터 추출하기
		String userId = request.getParameter("userId");
		String userPwd = request.getParameter("userPwd");
		
		// 조회해온 Member객체(loginUser)
		Member loginUser = new MemberService().loginMember(userId, userPwd);
		
		// System.out.println("조회된 Member : " + m);
		
		/*
		 * 응답페이지에 전달할 값이 있다면 값을 어딘가에 담아서 전달해야한다. (이 때 담아줄 수 있는 내장 객체 4가지)
		 * -servlet scope
		 * 1) application : application에 담은 데이터는 웹 애플리케이션 전역에서 꺼내 쓸 수 있다.
		 * 2) session : session에 담은 데이터는 모든 jsp와 servlet에서 꺼내 쓸 수 있다.
		 * 				한 번 담은 데이터는 직접 지우기 전까지, 서버가 멈추기 전까지, 브라우저가 종료되기 전까지는 사용 가능하다.
		 * 3) request : request에 담은 데이터는 해당 request를 포워딩(위임)한 응답 jsp에서만 꺼내 쓸 수 있다.
		 * 4) page : 해당 jsp페이지에서만 꺼내 쓸 수 있음.
		 * 
		 * 공통적으로 데이터를 담고자 한다면
		 * -setAttribute("키","벨류");
		 * 데이터를 꺼내고자 한다면
		 * -getAttribute("키");
		 * 데이터를 지우고자 한다면
		 * -removeAttribute("키");
		 * 
		 * */
		
		if(loginUser == null) { // 로그인 실패 (에러 페이지로 응답해보기)
			// 보내고자하는 에러페이지에 에러 메세지를 포워딩(위임)
			request.setAttribute("errorMsg", "로그인에 실패했습니다.");
			// 응답페이지 jsp에 위임할 때 필요한 객체(RequestDispatcher)
			RequestDispatcher view = request.getRequestDispatcher("views/common/errorPage.jsp");
			// 포워딩(위임)-해당 경로로 view화면은 보여지지만 url은 변경되지 않는다.(맨 처음 요청했을 때의 url이 남아있음)
			view.forward(request, response);
		}else { // 로그인 성공 -index페이지로 돌아가기
			// 로그인 한 회원의 정보를 계속 가지고 다닐 것이기 때문에 session에 담아준다.
			// request에 담으면 다른 화면으로 이동 시 로그인이 풀림
			
			// session에 담으려면 Session객체를 먼저 가져와야한다.
			HttpSession session = request.getSession();
			
			// 조회된 유저 정보 객체를 담아주기
			session.setAttribute("loginUser", loginUser);
			session.setAttribute("alertMsg", "성공적으로 로그인 되었습니다.");
			
			// 응답 방식 2가지
			// 1. 위임(포워딩) - 요청을 유지한 채로 위임하는 방법(url에 기존 주소가 남아있게 된다)
			// 해당 경로로 view화면은 보여지지만 url은 변경되지 않는다.(맨 처음 요청했을 때의 url이 남아있음)
			// RequestDispatcher view = request.getRequestDispatcher("응답페이지 경로");
			// view.forward(request, response);
			
			// 2. 재요청(리다이렉트) - 새 페이지를 보여줘(url에 기존 주소 남아있지 않음)
			// response.sendRedirect("경로");
			// response.sendRedirect("/jsp"); // contextRoot(localhost:888/jsp)넣어주면 index페이지로 감
			
			// 로그인 성공하면 index페이지로
			//response.sendRedirect(request.getContextPath()); // contextRoot가져오는 방법
			
			// 로그인 성공하면 기존에 보던 페이지로 이동
			String before = request.getHeader("Referer");
			//System.out.println(before);
			response.sendRedirect(before);
		}
		
		
	}

}
