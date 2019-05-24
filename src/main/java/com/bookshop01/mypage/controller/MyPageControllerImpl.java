package com.bookshop01.mypage.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop01.common.base.BaseController;
import com.bookshop01.member.vo.CouponVO;
import com.bookshop01.member.vo.MemberVO;
import com.bookshop01.mypage.service.MyPageService;
import com.bookshop01.order.vo.OrderVO;

@Controller("myPageController")
@RequestMapping(value="/mypage")
public class MyPageControllerImpl extends BaseController  implements MyPageController{
	@Autowired
	MyPageService myPageService;
	@Autowired
	MemberVO memberVO;
	@Autowired
	CouponVO couponVO;
	@Override
	@RequestMapping(value="/myPageMain.do", method=RequestMethod.GET)
	public ModelAndView myPageMain(@RequestParam(required = false,value="message") String message, 
			                                             HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		session=request.getSession();
		session.setAttribute("side_menu", "my_page");//마이페이 사이드 메뉴로 설정
		
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		memberVO = (MemberVO)session.getAttribute("memberInfo");
		String member_id = memberVO.getMember_id();
		
		List<OrderVO> myOrderList = myPageService.listMyOrderGoods(member_id);
		couponVO = myPageService.myCoupons(member_id);
		
		mav.addObject("message",message);
		mav.addObject("myOrderList",myOrderList);
		mav.addObject("couponVO",couponVO);
		
		return mav;
	}
	
	
	@Override
	@RequestMapping(value="/myOrderDetail.do",method=RequestMethod.GET)
	public ModelAndView myOrderDetail(@RequestParam("order_id")String order_id, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		HttpSession session = request.getSession();
		MemberVO orderer=(MemberVO)session.getAttribute("memberInfo");
		
		List<OrderVO> myOrderList = myPageService.findMyOrderInfo(order_id);
		mav.addObject("orderer", orderer);
		mav.addObject("myOrderList", myOrderList);
		return mav;
	}
	
	@Override
	public ModelAndView cancelMyOrder(String order_id, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	@RequestMapping(value="/listMyOrderHistory.do", method=RequestMethod.GET)
	public ModelAndView listMyOrderHistory(@RequestParam Map<String, String> dateMap, 
			                                                    HttpServletRequest request,
			                                                    HttpServletResponse response) throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		HttpSession session = request.getSession();
		memberVO =(MemberVO)session.getAttribute("memberInfo");
		String member_id = memberVO.getMember_id();
		
		String fixedSearchPeriod = dateMap.get("fixedSearchPeriod");
		String beginDate=null;
		String endDate=null;
		
		String[] tempDate = calcSearchPeriod(fixedSearchPeriod).split(",");
		beginDate=tempDate[0];
		endDate=tempDate[1];
		dateMap.put("beginDate",beginDate);
		dateMap.put("endDate", endDate);
		dateMap.put("member_id",member_id);
		
		List<OrderVO> myOrderHistList=myPageService.listMyOrderHistory(dateMap);
		
		String beginDate1[]=beginDate.split("-");//검색일자를 년,월,일로 분리해서 화면에 전달
		String endDate1[]=endDate.split("-");
		
		mav.addObject("beginYear", beginDate1[0]);
		mav.addObject("beginMonth", beginDate1[1]);
		mav.addObject("beginDay", beginDate1[2]);
		
		mav.addObject("endYear", endDate1[0]);
		mav.addObject("endMonth", endDate1[1]);
		mav.addObject("endDay", endDate1[2]);
		
		mav.addObject("myOrderHistList", myOrderHistList);
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/listMyOrderHistorySearch.do", 
	                          method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView listMyOrderHistorySearch(@RequestParam Map<String, String> dateMap, 
			                                                    HttpServletRequest request,
			                                                    HttpServletResponse response) throws Exception {
		/* String viewName=(String)request.getAttribute("viewName"); */
		ModelAndView mav = new ModelAndView("/mypage/listMyOrderHistory");
		HttpSession session = request.getSession();
		memberVO =(MemberVO)session.getAttribute("memberInfo");
		String member_id = memberVO.getMember_id();
	
		dateMap.put("beginDate",dateMap.get("beginYear")+"-"+dateMap.get("beginMonth")+'-'+dateMap.get("beginDay"));
		dateMap.put("endDate", dateMap.get("endYear")+"-"+dateMap.get("endMonth")+'-'+dateMap.get("endDay"));
		dateMap.put("member_id",member_id);
		
		 List<OrderVO> myOrderHistList=myPageService.listMyOrderHistory(dateMap);
		  
		  mav.addObject("beginYear", dateMap.get("beginYear"));
		  mav.addObject("beginMonth",dateMap.get("beginMonth"));
		  mav.addObject("beginDay", dateMap.get("beginDay"));
		  
		  mav.addObject("endYear", dateMap.get("endYear")); 
		  mav.addObject("endMonth",dateMap.get("endMonth")); 
		  mav.addObject("endDay", dateMap.get("endDay"));
		  
		  mav.addObject("myOrderHistList", myOrderHistList);
		 
		return mav;
	}
	
	//회원정보
	@Override
	@RequestMapping(value="/myDeatilInfo.do",method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView myDetailInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
	   String viewName = (String)request.getAttribute("viewName");
	   System.out.println("viewName="+viewName);
	   ModelAndView mav = new ModelAndView(viewName);
		return mav;
	}
	
	@Override
	@RequestMapping(value="/modifyMyInfo.do",method=RequestMethod.POST)
	public ResponseEntity modifyMyInfo(@RequestParam("attribute") String attribute, 
			                                            @RequestParam("value") String value, 
			                                              HttpServletRequest request,
			                                              HttpServletResponse response) throws Exception {
		Map<String,String> memberMap = new HashMap<String,String>();
		String val[]=null;
		HttpSession session = request.getSession();
		
		memberVO = (MemberVO)session.getAttribute("memberInfo");
		String member_id = memberVO.getMember_id();//
		if(attribute.equals("member_birth")) {
			val=value.split(",");
			memberMap.put("member_bitrh_y",val[0]);
			memberMap.put("member_bitrh_m",val[1]);
			memberMap.put("member_bitrh_d",val[2]);
			memberMap.put("member_bitrh_gn",val[3]);
		}else if(attribute.equals("tel")) {
			val=value.split(",");
			memberMap.put("tel1",val[0]);
			memberMap.put("tel2",val[1]);
			memberMap.put("tel3",val[2]);
		}else if(attribute.equals("hp")) {
			val=value.split(",");
			memberMap.put("hp1",val[0]);
			memberMap.put("hp2",val[1]);
			memberMap.put("hp3",val[2]);
			memberMap.put("smssts_yn", val[3]);
		}else if(attribute.equals("email")) {
			val=value.split(",");
			memberMap.put("email1",val[0]);
			memberMap.put("email2",val[1]);
			memberMap.put("emailsts_yn",val[2]);
		}else if(attribute.contentEquals("address")) {
			val=value.split(",");
			memberMap.put("zipcode",val[0]);
			memberMap.put("roadAddress",val[1]);
			memberMap.put("jibunAddress",val[2]);
			memberMap.put("namujiAddress", val[3]);
		}else {
			memberMap.put(attribute, value);
		}
		
		memberMap.put("member_id", member_id);
		
		//수정된 회원 정보를 다시 세션에 저장
		memberVO = myPageService.modifyMyInfo(memberMap);
		session.removeAttribute("memberInfo");
		session.setAttribute("memberInfo", memberVO);
		
		String message = null;
		ResponseEntity resEntity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		message = "mod_success";
		resEntity = new ResponseEntity(message, responseHeaders, HttpStatus.OK);
		return resEntity;
	}
}
