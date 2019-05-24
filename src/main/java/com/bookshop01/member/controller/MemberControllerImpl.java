package com.bookshop01.member.controller;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop01.common.base.BaseController;
import com.bookshop01.member.service.MemberService;
import com.bookshop01.member.vo.LocalTelNo;
import com.bookshop01.member.vo.MemberVO;

@Controller("memberController")
@RequestMapping(value="/member")
public class MemberControllerImpl extends BaseController implements MemberController{
	@Autowired
	MemberService memberService;
	@Autowired
	MemberVO memberVO;
	
	@Override
	@RequestMapping(value="/login.do",method=RequestMethod.POST)
	public ModelAndView login(@RequestParam Map<String, String> loginMap, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView mav = new ModelAndView();
		//DB의 회원정보 추출
		memberVO=memberService.login(loginMap);
		
		System.out.println(memberVO!=null);
		//로그인시 회원 정보가 존재하면...
		if(memberVO!=null && memberVO.getMember_id()!=null) {
			HttpSession session = request.getSession();
			session = request.getSession();
			session.setAttribute("isLogOn", true);//로그온 플래그를 true로 지정
			session.setAttribute("memberInfo", memberVO);//회원정보를 session에 저장
			
			String action=(String)session.getAttribute("action");
			if(action!=null && action.contentEquals("/order/orderEachGoods.do")) {
				mav.setViewName("forward:"+action);
			}else {
				mav.setViewName("redirect:/main/main.do");
			}
		}else {//로그인시 회원정보가 존재하지 않으면....
			String message = "아이디나 비밀번호가 틀립니다. 다시 로그인 해주세요";
			mav.addObject("message",message);
			mav.setViewName("/member/loginForm");
		}
		return mav;
	}
	
	@Override
	@RequestMapping(value="/logout.do",method=RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    ModelAndView mav =new ModelAndView();
	    HttpSession session = request.getSession();
	    //session에 로그아웃처리 및 회원정보 제거
	    session.setAttribute("isLogOn", false);
	    session.removeAttribute("memberInfo");
	    mav.setViewName("redirect:/main/main.do");
		return mav;
	}
	@Override
	@RequestMapping(value="/addMember.do",method=RequestMethod.POST)
	public ResponseEntity addMember(@ModelAttribute("memberVO")MemberVO member, 
			                                          HttpServletRequest request, 
			                                          HttpServletResponse response) throws Exception {
		System.out.println(member.getMember_name());
		ResponseEntity resEntity=null;
		response.setContentType("text/html;charset=utf-8");
		String message=null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html;charset=utf-8");
		try {
			    memberService.addMember(member);
			    message = "<script>";
			    message +="alert('회원가입완료.로그인 창으로 이동합니다.');";
			    message +="location.href='"+request.getContextPath()+"/member/memberForm.do'";
			    message += "</script>";
		}catch(Exception e) {
			     message = "<script>";
		         message +="alert('작업중 오류발생. 다시 시도해 주세요');";
		         message +="location.href='"+request.getContextPath()+"/member/memberForm.do'";
		         message += "</script>";
		}
		 resEntity = new ResponseEntity(message,responseHeaders,HttpStatus.OK);
		
		return resEntity;
		
	}
	
	
	@Override
	@RequestMapping(value="/overlapped.do" ,method=RequestMethod.POST)
	public ResponseEntity overlapped(String id, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResponseEntity resEnt=null;
		String result=memberService.overlapped(id);
		resEnt = new ResponseEntity(result,HttpStatus.OK);
		return resEnt;
	}
	
	@RequestMapping(value="/*Form.do", method=RequestMethod.GET)
	public ModelAndView viewForm(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		 List<LocalTelNo> localTelNo = memberService.getLocalTelNo();
		ModelAndView mav = new ModelAndView(viewName);
		System.out.println("viewName:"+viewName);
		mav.addObject("localTelNo", localTelNo);
		return mav;
	}
	
}
