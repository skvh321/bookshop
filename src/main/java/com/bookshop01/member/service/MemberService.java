package com.bookshop01.member.service;

import java.util.List;
import java.util.Map;

import com.bookshop01.member.vo.LocalTelNo;
import com.bookshop01.member.vo.MemberVO;

public interface MemberService {
	public MemberVO login(Map  loginMap) throws Exception;
	public void addMember(MemberVO memberVO) throws Exception;
	public String overlapped(String id) throws Exception;
	public  List<LocalTelNo> getLocalTelNo() throws Exception;
}
