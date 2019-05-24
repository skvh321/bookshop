package com.bookshop01.admin.order.dao;

import java.util.ArrayList;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.bookshop01.member.vo.MemberVO;
import com.bookshop01.order.vo.OrderVO;

@Repository("adminOrderDAO")
public class AdminOrderDAOImpl  implements AdminOrderDAO{
	@Autowired
	private SqlSession sqlSession;

	@Override
	public ArrayList<OrderVO> selectNewOrderList(Map condMap) throws DataAccessException {
		ArrayList<OrderVO> ordeList=(ArrayList)sqlSession.selectList("mapper.admin.order.selectNewOrderList",condMap);
		return ordeList;
	}

	@Override
	public void updateDeliveryState(Map deliveryMap) throws DataAccessException {
		sqlSession.update("mapper.admin.order.updateDeliveryState", deliveryMap);
		
	}

	@Override
	public ArrayList<OrderVO> selectOrderDetail(int order_id) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberVO selectOrderer(String member_id) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
