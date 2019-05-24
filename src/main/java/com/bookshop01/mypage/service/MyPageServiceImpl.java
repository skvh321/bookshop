package com.bookshop01.mypage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookshop01.member.vo.CouponVO;
import com.bookshop01.member.vo.MemberVO;
import com.bookshop01.mypage.dao.MyPageDAO;
import com.bookshop01.mypage.vo.MyPageVO;
import com.bookshop01.order.vo.OrderVO;

@Service("myPageService")
@Transactional(propagation=Propagation.REQUIRED)
public class MyPageServiceImpl  implements MyPageService{
	@Autowired
	MyPageDAO myPageDAO;

	@Override
	public List<OrderVO> listMyOrderGoods(String member_id) throws Exception {
		return myPageDAO.selectMyOrderGoodsList(member_id);
	}

	@Override
	public List findMyOrderInfo(String order_id) throws Exception {
		return myPageDAO.selectMyOrderInfo(order_id);
	}

	@Override
	public List<OrderVO> listMyOrderHistory(Map dateMap) throws Exception {
		return myPageDAO.selectMyOrderHistoryList(dateMap);
	}

	@Override
	public MemberVO modifyMyInfo(Map memberMap) throws Exception {
		String member_id = (String)memberMap.get("member_id");
		myPageDAO.updateMyInfo(memberMap);//수정처리
		return myPageDAO.selectMyDetailInfo(member_id);//수정된 db정보 추출
	}

	@Override
	public void cancelOrder(String order_id) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MemberVO myDetailInfo(String member_id) throws Exception {
		return myPageDAO.selectMyDetailInfo(member_id);
	}

	@Override
	public CouponVO myCoupons(String member_id) throws Exception {
		return myPageDAO.selectMyCoupons(member_id);
	}

	
}
