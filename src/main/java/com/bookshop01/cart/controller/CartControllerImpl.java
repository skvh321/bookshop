package com.bookshop01.cart.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop01.cart.service.CartService;
import com.bookshop01.cart.vo.CartVO;
import com.bookshop01.common.base.BaseController;
import com.bookshop01.goods.vo.GoodsVO;
import com.bookshop01.member.vo.MemberVO;

@Controller("cartController")
@RequestMapping(value="/cart")
public class CartControllerImpl extends BaseController implements CartController{
	@Autowired
	CartService cartService;
	@Autowired
	CartVO cartVO;
	@Autowired
	MemberVO memberVO;
	
	
	@Override
	@RequestMapping(value="/myCartList.do" ,method=RequestMethod.GET)
	public ModelAndView myCartMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO)session.getAttribute("memberInfo");
		String member_id = memberVO.getMember_id();
		cartVO.setMember_id(member_id);
		Map<String,List>cartMap = cartService.myCartList(cartVO);
		session.setAttribute("cartMap", cartMap);//장바구니 목록화면에서 상품주문시 사용,세션에 저장
		return mav;
	}
	
	@Override
	@RequestMapping(value="/addGoodsInCart.do",method=RequestMethod.POST) 
	public String addGoodsInCart(@RequestParam("goods_id") int goods_id, 
			                                   HttpServletRequest request,
			                                   HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		memberVO = (MemberVO)session.getAttribute("memberInfo");
		if(memberVO!=null) {
		
		String member_id = memberVO.getMember_id();
		cartVO.setMember_id(member_id);
		cartVO.setGoods_id(goods_id);
		//카트에 동일아이디로 저장한 동일상품이 있는지 확인
		boolean isAlreadyExisted = cartService.findCartGoods(cartVO);
		if(isAlreadyExisted) {//있으면 
			return "already_existed";
		}else {//없으면 추가
			cartService.addGoodsInCart(cartVO);
			return "add_success";
		 }
		}else {
			return "add_fail";
		}
	}
	
	
	@Override
	@RequestMapping(value="/modifyCartQty.do",method=RequestMethod.POST)
	public String modifyCartQty(@RequestParam("goods_id")int goods_id,
			                                 @RequestParam("cart_goods_qty")int cart_goods_qty, 
			                                 HttpServletRequest request,
			                                 HttpServletResponse response) throws Exception {
	   HttpSession session=request.getSession();
	   memberVO = (MemberVO)session.getAttribute("memberInfo");
	   String member_id = memberVO.getMember_id();
	   cartVO.setGoods_id(goods_id);
	   cartVO.setMember_id(member_id);
	   cartVO.setCart_goods_qty(cart_goods_qty);
	   boolean result = cartService.modifyCartQty(cartVO);
	   
	   if(result==true) {
		   return "modify_success";
	   }else {
		   return "modify_failed";
	   }
	}
	
	@Override
	@RequestMapping(value="/removeCartGoods.do",method=RequestMethod.POST)
	public ModelAndView removeCartGoods(@RequestParam("cart_id") int cart_id, 
			                                                 @RequestParam("goods_id") int goods_id,
			                                                 HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		memberVO = (MemberVO)session.getAttribute("memberInfo");
		String member_id = memberVO.getMember_id();
		   
		System.out.println("cart_id="+cart_id);
		System.out.println("goods_id="+goods_id);
		ModelAndView mav=new ModelAndView();
		cartService.removeCartGoods(cart_id,goods_id,member_id);
		mav.setViewName("redirect:/cart/myCartList.do");
		return mav;
	}
	
}
