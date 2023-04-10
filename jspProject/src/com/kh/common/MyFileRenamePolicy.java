package com.kh.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.oreilly.servlet.multipart.FileRenamePolicy;

public class MyFileRenamePolicy implements FileRenamePolicy{
	// 파일명 변경 메소드 재정의하기
	// 기존의 파일명을 전달받아 수정작업 후 해당 파일명을 반환해주는 메소드
	@Override
	public File rename(File originFile) {
		
		// 원본파일명("hello.jpg")
		String originName = originFile.getName();
		
		// 수정파일명 : 파일 업로드 된 시간(년/월/일/시/분/초) + 5자리 랜덤값
		// 확장자 : 그대로
		// hello.jpg -> 2023040615463012443.jpg
		
		// 1. 파일 업로드 시간
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); 
		
		// 2. 랜덤 숫자5자리
		int ranNum = (int)(Math.random()*90000)+10000;
		
		// 3. 원본파일 확장자 추출
		// 파일명에서 가장 마지막.기준으로 추출
		String ext = originName.substring(originName.lastIndexOf("."));
		
		String changeName = currentTime + ranNum + ext;
		
		// 4. 원본파일을 파일명 변경하여 전달
		return new File(originFile.getParent(), changeName);
	}

}
