package com.bookshop01.order.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.bookshop01.order.vo.OrderVO;

@Repository("orderDAO")
public class OrderDAOImpl implements OrderDAO {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public List<OrderVO> listMyOrderGoods(OrderVO orderBean) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertNewOrder(List<OrderVO> myOrderList) throws DataAccessException {
		int order_id =selectOrderID();
		for(int i=0;i<myOrderList.size();i++) {
			OrderVO orderVO = myOrderList.get(i);
			orderVO.setOrder_id(order_id);
			sqlSession.insert("mapper.order.insertNewOrder",orderVO);
		}
	}
	
    //주문자 id 조회메소드
	private int selectOrderID() {
		return sqlSession.selectOne("mapper.order.selelctOrederID");
	}

	@Override
	public OrderVO findMyOrder(String order_id) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeGoodsFromCart(OrderVO myOrderVO) throws DataAccessException {
		 sqlSession.delete("mapper.order.deleteGoodsFromCart", myOrderVO); 
		 }
}

