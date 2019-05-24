package com.bookshop01.goods.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
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

import com.bookshop01.common.base.BaseController;
import com.bookshop01.goods.service.GoodsService;
import com.bookshop01.goods.vo.GoodsVO;

import net.sf.json.JSONObject;

@Controller("goodsController")
@RequestMapping(value="/goods")
public class GoodsControllerImpl extends BaseController   implements GoodsController {
	@Autowired
	GoodsService goodsService;

	@Override
	@RequestMapping(value="/goodsDetail.do", method=RequestMethod.GET)
	public ModelAndView goodsDetail(@RequestParam("goods_id")String goods_id, 
			                                         HttpServletRequest request, 
			                                         HttpServletResponse response)throws Exception {
		String viewName = (String)request.getAttribute("viewName");
		HttpSession session = request.getSession();
		Map goodsMap = goodsService.goodsDetail(goods_id);
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("goodsMap", goodsMap);
		GoodsVO goodsVO = (GoodsVO)goodsMap.get("goodsVO");
		addGoodsInQuick(goods_id,goodsVO,session);
		return mav;
		
	}

	@Override
	@RequestMapping(value="/keywordSearch.do",method=RequestMethod.GET)
	public @ResponseBody String keywordSearch(@RequestParam("keyword") String keyword, 
			                                                         HttpServletRequest request, 
			                                                         HttpServletResponse response) throws Exception {
	 
	 if(keyword==null|| "".equals(keyword))
		return null;
	 
	 System.out.println("------------keyword:"+keyword);
	 keyword=keyword.toUpperCase();
	 List<String> keywordList = goodsService.keywordSearch(keyword);
	 
	 //최종 완성될 JSONObject 선언(전체)
	 JSONObject jsonObject = new  JSONObject();
	 jsonObject.put("keyword", keywordList);
	 
	 //toString()
	 String jsonInfo = URLEncoder.encode(jsonObject.toString() , "UTF-8");
	 System.out.println("-------jsonInfo-String:"+jsonInfo);
	 return jsonInfo;	
	}

	@Override
	@RequestMapping(value="/searchGoods.do",method=RequestMethod.GET)
	public ModelAndView searchGoods(@RequestParam("searchWord") String searchWord, 
			                                          HttpServletRequest request, 
			                                          HttpServletResponse response)
			throws Exception {
	   String viewName = (String)request.getAttribute("viewName");
	   //pageNum
	   //상품건수
	   //page당 건수
	   //PageNum의 시작번호, 끝
	   //페이징 처리
	    int pageNum=1;//전체 페이지 시작 번호
	 	int limit = 10;//한페이지당 보여줄 상품 수
	   
		if(request.getParameter("pageNum")!=null) {
			pageNum=Integer.parseInt(request.getParameter("pageNum"));
		}
		
		//전체 건수
		int total_record = goodsService.getListCount(searchWord);
		
	   //토탈페이지
	   int total_page=0;
	   
	   if(total_record%limit==0) {// 예) 상품수 100인경우 10페이지
			total_page=total_record/limit;
			Math.floor(total_page);
		}else {
			total_page=total_record/limit; //예)상품수 106인 경우 10페이지 + 1페이지
			Math.floor(total_page);
			total_page= total_page+1;
		}
	   
	   int total_segment=0;
	    if(total_page%limit==0) {
	    	total_segment = total_page/limit;
	    	Math.floor(total_segment);
	    }else {
	    	total_segment = total_page/limit;
	    	Math.floor(total_segment);
	    	total_segment=total_segment+1;
	    }
	   
		int beginRow=(pageNum - 1)*limit+1;
		int endRow = beginRow+limit-1;
		
		GoodsVO goodsVO = new GoodsVO();
		goodsVO.setSearchWord(searchWord);
		goodsVO.setBeginRow(beginRow);
		goodsVO.setEndRow(endRow);
		//15페이지가 속한 segment는 11페이지가 시작페이지, 20페이지까 끝페이지
		int beginSegment=(int)((pageNum - 1)/limit)*limit+1;
		int endSegment = beginSegment+limit-1;
		
		
		List<GoodsVO> goodsList = goodsService.searchGoods(goodsVO);
		
	   ModelAndView mav = new ModelAndView(viewName);
	   
	   System.out.println("pageNum="+pageNum);
	   System.out.println("total_page="+total_page);
	   System.out.println("total_record="+total_record);
	   System.out.println("searchWord="+searchWord);
	   
	   
	   mav.addObject("goodsList", goodsList);
	   mav.addObject("pageNum", pageNum);
	   mav.addObject("total_page", total_page);
	   mav.addObject("total_record", total_record);
	   //검색조건, 검색어 추가 
	   mav.addObject("searchWord",searchWord);
	   
	   mav.addObject("beginSegment",beginSegment);
	   mav.addObject("endSegment",endSegment);
	   
	   return mav;
	}
	

	private void addGoodsInQuick(String goods_id, GoodsVO goodsVO, HttpSession session) {
	 boolean already_existed=false;
	 List<GoodsVO> quickGoodsList;//최근본 상품 저장 list
	 quickGoodsList=( List<GoodsVO>)session.getAttribute("quickGoodsList");
	 
	 if(quickGoodsList!=null) {//추가
		 if(quickGoodsList.size() < 4) {
			 for(int i=0;i<quickGoodsList.size();i++) {
				 GoodsVO _goodsBean =(GoodsVO)quickGoodsList.get(i);
				 if(goods_id.equals(_goodsBean.getGoods_id())) {
					  already_existed=true;
					  break;
				 }
			 }
			 if(already_existed==false) {
				 quickGoodsList.add(goodsVO);
			 }
		 }
	 }else {//최초
		 quickGoodsList = new ArrayList<GoodsVO>();
		 quickGoodsList.add(goodsVO);
	 }
	 session.setAttribute("quickGoodsList", quickGoodsList);
	 session.setAttribute("quickGoodsListNum", quickGoodsList.size());
	}

	@Override
	@RequestMapping(value="/goodsList.do",method=RequestMethod.GET)
	public ModelAndView listGoodsBySort(@RequestParam("sort")String sort, 
			                                               HttpServletRequest request,
			                                               HttpServletResponse response)
			throws Exception {
		 String viewName = (String)request.getAttribute("viewName");
		 //페이징 처리
		    int pageNum=1;//전체 페이지 시작 번호
		 	int limit = 10;//한페이지당 보여줄 상품 수
		   
			if(request.getParameter("pageNum")!=null) {
				pageNum=Integer.parseInt(request.getParameter("pageNum"));
			}
			
			//전체 건수
			int total_record = goodsService.getListCountBySort(sort);
						
		   //토탈페이지
		   int total_page=0;
		   
		   if(total_record%limit==0) {// 예) 상품수 100인경우 10페이지
				total_page=total_record/limit;
				Math.floor(total_page);
			}else {
				total_page=total_record/limit; //예)상품수 106인 경우 10페이지 + 1페이지
				Math.floor(total_page);
				total_page= total_page+1;
			}
		   
		   int total_segment=0;
		    if(total_page%limit==0) {
		    	total_segment = total_page/limit;
		    	Math.floor(total_segment);
		    }else {
		    	total_segment = total_page/limit;
		    	Math.floor(total_segment);
		    	total_segment=total_segment+1;
		    }
		   
			int beginRow=(pageNum - 1)*limit+1;
			int endRow = beginRow+limit-1;
			
			GoodsVO goodsVO = new GoodsVO();
		/* goodsVO.setSearchWord(searchWord); */
			
			goodsVO.setGoods_sort(sort);
			goodsVO.setBeginRow(beginRow);
			goodsVO.setEndRow(endRow);
			
			//15페이지가 속한 segment는 11페이지가 시작페이지, 20페이지까 끝페이지
			int beginSegment=(int)((pageNum - 1)/limit)*limit+1;
			int endSegment = beginSegment+limit-1;
			
			
			List<GoodsVO> goodsList = goodsService.searchGoodsBySort(goodsVO);
			
		   ModelAndView mav = new ModelAndView(viewName);
		   mav.addObject("goodsList", goodsList);
		   mav.addObject("pageNum", pageNum);
		   mav.addObject("total_page", total_page);
		   mav.addObject("total_record", total_record);
		   //검색조건, 검색어 추가 
		   mav.addObject("sort",sort);
		   
		   mav.addObject("beginSegment",beginSegment);
		   mav.addObject("endSegment",endSegment);
		   
		return mav;
	}
}
