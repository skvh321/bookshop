package com.myspring.pro30.board.controller;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.service.BoardService;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.member.vo.MemberVO;

@Controller("boardController")
public class BoardControllerImpl  implements BoardController{
	private static final String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";
	@Autowired
	BoardService boardService;
	@Autowired
	ArticleVO articleVO;
	
	
	@Override
	@RequestMapping(value="/board/listArticles.do",
	                           method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception {
	   String viewName = (String)request.getAttribute("viewName");
	   List articleList = boardService.listArticles();
	   ModelAndView mav =  new ModelAndView(viewName);
	   mav.addObject("articlesList",articleList);
		return mav;
	}
	
	@Override
	@RequestMapping(value="/board/addNewArticle.do", method=RequestMethod.POST)
	public ResponseEntity addNewArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		
		Map<String,Object> articleMap = new HashMap<String, Object>();
		Enumeration enu = multipartRequest.getParameterNames();
		while(enu.hasMoreElements()) {
			String name = (String)enu.nextElement();
			String value = multipartRequest.getParameter(name);
			articleMap.put(name,value);
		}
		
		String imageFileName = upload(multipartRequest);
		HttpSession session = multipartRequest.getSession();
		MemberVO memberVO = (MemberVO)session.getAttribute("member");
		String id = memberVO.getId();
		articleMap.put("parentNO",0);//원글인 경우 parnetNO=0
		articleMap.put("id", id);//작성자 id
		articleMap.put("imageFileName", imageFileName);//첨부파일명
		
		String message;
		ResponseEntity resEnt=null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-type", "text/html; charset=utf-8");
		
		try {
			    int articleNO = boardService.addNewArticle(articleMap);
			    if(imageFileName!=null && imageFileName.length()!=0) {
			    	File srcFile = new File(ARTICLE_IMAGE_REPO +"\\"+"temp"+"\\"+imageFileName);
			    	File destFile = new File(ARTICLE_IMAGE_REPO +"\\"+articleNO);
			    	FileUtils.moveToDirectory(srcFile, destFile, true);
			    }
			    message ="<script>";
			    message+="alert('새글을 추가했습니다.');";
			    message+="location.href='"+multipartRequest.getContextPath()+"/board/listArticles.do';";
			    message+="</script>";
			    resEnt = new ResponseEntity(message,responseHeaders,HttpStatus.CREATED);
			    
		}catch(Exception e) {
			File srcFile 
			        = new File(ARTICLE_IMAGE_REPO +"\\"+"temp"+"\\"+imageFileName);
			srcFile.delete();//파일 삭제
			
			message ="<script>";
		    message+="alert('오류가 발생했습니다. 다시 시도해 주세요');";
		    message+="location.href='"+multipartRequest.getContextPath()+"/board/articleForm.do';";
		    message+="</script>";
		    resEnt = new ResponseEntity(message,responseHeaders,HttpStatus.CREATED);
		    e.printStackTrace();
		}
		return resEnt;
	}
	
	//파일 업로드 메소드
	private String upload(MultipartHttpServletRequest multipartRequest) throws Exception{
		String imageFileName=null;
		Map<String,String> artcleMap = new HashMap<String, String>();
		Iterator<String> fileNames = multipartRequest.getFileNames();
		
		while(fileNames.hasNext()) {
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			imageFileName = mFile.getOriginalFilename();
			File file = new File(ARTICLE_IMAGE_REPO+"\\"+imageFileName);
			if(mFile.getSize()!=0) {//File null check
				if(!file.exists()) {//경로상에 파일이 존재하지 않으면
					if(file.getParentFile().mkdirs()) {//경로에 해당 파일 디렉토리들 생성
						file.createNewFile();//파일 생성
					}
				}
				mFile.transferTo(new  File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName));
			}
		}
		return imageFileName;
	}
	@Override
	@RequestMapping(value="/board/viewArticle.do", method=RequestMethod.GET)
	public ModelAndView viewArticle(int articleNO, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	    String viewName = (String)request.getAttribute("viewName");
	    ModelAndView mav = new ModelAndView();
	    articleVO = boardService.viewArticle(articleNO);//서비스에 해당번호의 게시글 정보 요청
	    mav.setViewName(viewName);
	    mav.addObject("article",articleVO);
		return mav;
	}
	
	@Override
	@RequestMapping(value="/board/removeArticle.do",method=RequestMethod.POST)
	public ResponseEntity removeArticle(int articleNO, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
         String message;
         ResponseEntity resEnt=null;
         HttpHeaders responseHeaders = new HttpHeaders();
         responseHeaders.add("Content-Type", "text/html; charset=utf-8");
         try {
        	   //db에서 글 삭제
        	   boardService.removeArticle(articleNO);
        	   //upload폴더에서 첨부파일 삭제
        	   File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
        	   FileUtils.deleteDirectory(destDir);
        	   
        	    message ="<script>";
			    message+="alert('글을 삭제하였습니다.');";
			    message+="location.href='"+request.getContextPath()+"/board/listArticles.do';";
			    message+="</script>";
        	    resEnt = new ResponseEntity(message, responseHeaders,HttpStatus.CREATED);
         }catch(Exception e) {
        	    message ="<script>";
			    message+="alert('작업 중 오류가 발생하였습니다.');";
			    message+="location.href='"+request.getContextPath()+"/board/listArticles.do';";
			    message+="</script>";
     	        resEnt = new ResponseEntity(message, responseHeaders,HttpStatus.CREATED);
        	    e.printStackTrace();
         }
         
		return resEnt;
	}
	
	@Override
	@RequestMapping(value="/board/*Form.do", method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    String viewName = (String)request.getAttribute("viewName");
	    String parentNO = request.getParameter("parentNO");
	    ModelAndView mav =  new ModelAndView(viewName);
	    mav.addObject("parentNO", parentNO);
	    return mav;
	}
	
	

	@Override
	@RequestMapping(value="/board/modArticle.do", method=RequestMethod.POST)
	public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		Map<String,Object> articleMap = new HashMap<String, Object>();
		Enumeration enu = multipartRequest.getParameterNames();
		while(enu.hasMoreElements()) {
			String name = (String)enu.nextElement();
			String value = multipartRequest.getParameter(name);
			articleMap.put(name,value);
		}
		
		String imageFileName = upload(multipartRequest);
		HttpSession session = multipartRequest.getSession();
		MemberVO memberVO = (MemberVO)session.getAttribute("member");
		String id = memberVO.getId();
		//articleMap.put("parentNO",0);//원글인 경우 parnetNO=0
		articleMap.put("id", id);//작성자 id
		articleMap.put("imageFileName", imageFileName);//첨부파일명
		
		//내용보기 form에서 hidden으로 넘어온 글번호
		String articleNO = (String)articleMap.get("articleNO");
		
		String message;
		ResponseEntity resEnt=null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-type", "text/html; charset=utf-8");
		
		try {
   			    //db에 수정 처리
			    boardService.modArticle(articleMap);
			    if(imageFileName!=null && imageFileName.length()!=0) {
			    	File srcFile = new File(ARTICLE_IMAGE_REPO +"\\"+"temp"+"\\"+imageFileName);
			    	File destFile = new File(ARTICLE_IMAGE_REPO +"\\"+articleNO);
			    	FileUtils.moveToDirectory(srcFile, destFile, true);
			    }
			    message ="<script>";
			    message+="alert('글을 수정하였습니다.');";
			    message+="location.href='"+multipartRequest.getContextPath()
			                  +"/board/viewArticle.do?articleNO="+articleNO+"';";
			    message+="</script>";
			    resEnt = new ResponseEntity(message,responseHeaders,HttpStatus.CREATED);
			    
		}catch(Exception e) {
			File srcFile 
			        = new File(ARTICLE_IMAGE_REPO +"\\"+"temp"+"\\"+imageFileName);
			srcFile.delete();//파일 삭제
			
			message ="<script>";
		    message+="alert('오류가 발생했습니다. 다시 시도해 주세요');";
		    message+="location.href='"+multipartRequest.getContextPath()
		                  +"/board/viewArticle.do?articleNO="+articleNO+"';";
		    message+="</script>";
		    resEnt = new ResponseEntity(message,responseHeaders,HttpStatus.CREATED);
		    e.printStackTrace();
		}
		return resEnt;
	}

	@Override
	@RequestMapping(value="/board/addReply.do", method=RequestMethod.POST)
	public ResponseEntity addReply(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		Map<String,Object> articleMap = new HashMap<String, Object>();
		Enumeration enu = multipartRequest.getParameterNames();
		while(enu.hasMoreElements()) {
			String name = (String)enu.nextElement();
			String value = multipartRequest.getParameter(name);
			System.out.println("name===="+name);
			System.out.println("value===="+value);
			articleMap.put(name,value);
		}
		
		String imageFileName = upload(multipartRequest);
		HttpSession session = multipartRequest.getSession();
		MemberVO memberVO = (MemberVO)session.getAttribute("member");
		String id = memberVO.getId();
		//articleMap.put("parentNO",0);//원글인 경우 parnetNO=0
		articleMap.put("id", id);//작성자 id
		articleMap.put("imageFileName", imageFileName);//첨부파일명
		
		System.out.println("parentNO:"+articleMap.get("parentNO"));
		
		String message;
		ResponseEntity resEnt=null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-type", "text/html; charset=utf-8");
		
		try {
			    int articleNO = boardService.addNewArticle(articleMap);
			    if(imageFileName!=null && imageFileName.length()!=0) {
			    	File srcFile = new File(ARTICLE_IMAGE_REPO +"\\"+"temp"+"\\"+imageFileName);
			    	File destFile = new File(ARTICLE_IMAGE_REPO +"\\"+articleNO);
			    	FileUtils.moveToDirectory(srcFile, destFile, true);
			    }
			    message ="<script>";
			    message+="alert('답변을 추가했습니다.');";
			    message+="location.href='"+multipartRequest.getContextPath()+"/board/listArticles.do';";
			    message+="</script>";
			    resEnt = new ResponseEntity(message,responseHeaders,HttpStatus.CREATED);
			    
		}catch(Exception e) {
			File srcFile 
			        = new File(ARTICLE_IMAGE_REPO +"\\"+"temp"+"\\"+imageFileName);
			srcFile.delete();//파일 삭제
			
			message ="<script>";
		    message+="alert('오류가 발생했습니다. 다시 시도해 주세요');";
		    message+="location.href='"+multipartRequest.getContextPath()+"/board/articleForm.do';";
		    message+="</script>";
		    resEnt = new ResponseEntity(message,responseHeaders,HttpStatus.CREATED);
		    e.printStackTrace();
		}
		return resEnt;
	}
	
}
