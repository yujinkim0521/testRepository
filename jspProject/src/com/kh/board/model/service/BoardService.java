package com.kh.board.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.board.model.dao.BoardDao;
import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;
import com.kh.board.model.vo.Category;
import com.kh.common.JDBCTemplate;
import com.kh.common.model.vo.PageInfo;

public class BoardService {

	public int selectListCount() { // 총 게시글 개수 구하기
		
		Connection conn = JDBCTemplate.getConnection();
		
		// 처리된 행 수가 아닌 총 게시글의 개수를 조회해옴(트랜젝션X)
		int listCount = new BoardDao().selectListCount(conn);
		
		JDBCTemplate.close(conn);
		
		return listCount;
	}

	public ArrayList<Board> selectList(PageInfo pi) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		ArrayList<Board> list = new BoardDao().selectList(conn, pi);
		
		JDBCTemplate.close(conn);
		
		return list;
	}

	public ArrayList<Category> categoryList(){
		
		Connection conn = JDBCTemplate.getConnection();
		
		ArrayList<Category> list = new BoardDao().categoryList(conn);
		
		JDBCTemplate.close(conn);
		
		return list;		
		
	}
	public int insertBoard(Board b, Attachment at) {
		
		Connection conn = JDBCTemplate.getConnection();
		// 게시글이 작성될 때 첨부파일이 있거나 또는 없는 경우도 생각해서 작업하기
		int result = new BoardDao().insertBoard(conn, b);
		
		// 첨부파일이 있는 경우에 Attachment테이블에 insert작업하기
		int result2 = 1; // 첨부파일이 없어도 게시글 commit은 진행할 수 있도록 0이 아닌 1로 초기화
		if(at != null) {
			result2 = new BoardDao().insertAttachment(conn,at);
		}
		
		if(result > 0 && result2 > 0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result * result2; // 둘 중 하나라도 0이면 0을 반환
	}

	public int increaseCount(int boardNo) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new BoardDao().increaseCount(conn, boardNo);
		
		if(result > 0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}

	public Board selectBoard(int boardNo) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		Board b = new BoardDao().selectBoard(conn,boardNo);
		
		JDBCTemplate.close(conn);
		
		return b;
	}

	public Attachment selectAttachment(int boardNo) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		Attachment at = new BoardDao().selectAttachment(conn, boardNo);
		
		JDBCTemplate.close(conn);
		
		return at;
	}


	public int updateBoard(Board b, Attachment at) {
		
		Connection conn = JDBCTemplate.getConnection();
		// 새로운 첨부파일도 없고, 기존에도 첨부파일 없는경우 -> board update
		// 새로운 첨부파일 없고, 기존에 첨부파일 없는 경우 -> board-update / attachment-insert
		// 새로운 첨부파일 있고, 기존 첨부파일 있는 경우 -> board-update / attachment-update
		
		int result1 = new BoardDao().updateBoard(conn, b);
		
		int result2 = 1;
		
		if(at != null) { // 새롭게 추가된 첨부파일이 있는 경우
			if(at.getFileNo() != 0) { // 기존에 첨부파일이 있던경우 - 변경
				
				result2 = new BoardDao().updateAttachment(conn,at);
				
			}else { // 기존에 첨부파일이 없던 경우 - 추가
				result2 = new BoardDao().newInsertAttachment(conn,at);
			}
		}

		if(result1 > 0 && result2 > 0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
	
		return result1 * result2;
	}
	
//	public int deleteBoard(int boardNo, int fileNo) {
//		
//		Connection conn = JDBCTemplate.getConnection();
//		
//		int result1 = 1;
//		int result2 = 1;
//		
//		if(fileNo == 0) { // 첨부파일이 없으면 보드만 삭제
//			result1 = new BoardDao().deleteBoard(conn, boardNo);		
//		}else {
//			result1 = new BoardDao().deleteBoard(conn, boardNo);
//			result2 = new BoardDao().deleteAttachment(conn, fileNo);		
//		}
//		
//		System.out.println(result1);
//		System.out.println(result2);
//		
//		if(result1 > 0 && result2 > 0) {
//			JDBCTemplate.commit(conn);
//		}else {
//			JDBCTemplate.rollback(conn);
//		}
//				
//		JDBCTemplate.close(conn);
//		
//		return result1 * result2;
//	}

	public int deleteBoard(int boardNo) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new BoardDao().deleteBoard(conn, boardNo);
		
		if(result > 0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}

}
