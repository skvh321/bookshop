package com.bookshop01.mypage.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.bookshop01.member.vo.CouponVO;
import com.bookshop01.member.vo.MemberVO;
import com.bookshop01.order.vo.OrderVO;

@Repository("myPageDAO")
public class MyPageDAOImpl implements MyPageDAO{
	@Autowired
	private SqlSession sqlSession;

	@Override
	public List<OrderVO> selectMyOrderGoodsList(String member_id) throws DataAccessException {
		return sqlSession.selectList("mapper.mypage.selectMyOrderGoodsList", member_id);
	}

	@Override
	public List selectMyOrderInfo(String order_id) throws DataAccessException {
		return sqlSession.selectList("mapper.mypage.selectMyOrderInfo",order_id );
	}

	@Override
	public List<OrderVO> selectMyOrderHistoryList(Map dateMap) throws DataAccessException {
		return sqlSession.selectList("mapper.mypage.selectMyOrderHistoryList",dateMap);
	}

	@Override
	public void updateMyInfo(Map memberMap) throws DataAccessException {
		sqlSession.update("mapper.mypage.updateMyInfo", memberMap);
	}

	@Override
	public MemberVO selectMyDetailInfo(String member_id) throws DataAccessException {
		return sqlSession.selectOne("mapper.mypage.selectMyDetailInfo", member_id);
	}

	@Override
	public void updateMyOrderCancel(String order_id) throws DataAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CouponVO selectMyCoupons(String member_id) throws DataAccessException {
		return sqlSession.selectOne("mapper.mypage.selectMyCoupons", member_id);
	}
	
	
}
