package com.bookshop01.admin.member.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.bookshop01.member.vo.MemberVO;

@Repository("adminMemberDao")
public class AdminMemberDAOImpl  implements AdminMemberDAO{
	@Autowired
	private SqlSession sqlSession;

	@Override
	public ArrayList<MemberVO> listMember(HashMap condMap) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberVO memberDetail(String member_id) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modifyMemberInfo(HashMap memberMap) throws DataAccessException {
		// TODO Auto-generated method stub
		
	}
	

}
