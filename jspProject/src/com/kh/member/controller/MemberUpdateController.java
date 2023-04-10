package com.kh.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.member.model.service.MemberService;
import com.kh.member.model.vo.Member;

/**
 * Servlet implementation class MemberUpdateController
 */
@WebServlet("/update.me")
public class MemberUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberUpdateController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 변경된 정보 수정하기
		request.setCharacterEncoding("UTF-8");
		
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		String address = request.getParameter("address");
		String[] interests = request.getParameterValues("interest");
		
		String interest = "";
		
		
		if(interests != null) {
			interest = String.join(",", interests);
		}
		
		Member m = new Member(userId, userName, phone, email, address, interest);
		System.out.println(m);
		
		// service에게 요청 전달
		Member updateMem = new MemberService().updateMember(m);
		
		HttpSession session = request.getSession();
		// 수정완료 후 성공시 정보 변경 완료되었습니다. 알림메세지 후 마이페이지로 이동(변경정보 적용)
		if(updateMem != null) { // service에서 null로 초기화해놓고 성공시에만 정보를 담았기 때문에 null로 비교함
			session.setAttribute("alertMsg", "정보변경 완료되었습니다.");
			session.setAttribute("loginUser", updateMem); // 키값은 중복이 안되기때문에 동일 키값으로 작성하면 정보 갱신됨
			response.sendRedirect(request.getContextPath()+"/myPage.me"); // 디렉토리 노출되지 않게 루트 뒤에 myPage요청 매핑주소넣기
			// MyPageController -> myPage.jsp
		}else { // 실패
		// 실패시 에러페이지로 포워딩(위임)
			request.setAttribute("errorMsg", "정보변경실패");
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
		}
	}

}
