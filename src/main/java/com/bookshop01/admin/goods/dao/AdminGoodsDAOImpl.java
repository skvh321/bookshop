package com.bookshop01.admin.goods.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.bookshop01.goods.vo.GoodsVO;
import com.bookshop01.goods.vo.ImageFileVO;
import com.bookshop01.order.vo.OrderVO;

@Repository("adminGoodsDAO")
public class AdminGoodsDAOImpl  implements AdminGoodsDAO{
	@Autowired
	private SqlSession sqlSession;

	@Override
	public int insertNewGoods(Map newGoodsMap) throws DataAccessException {
		return sqlSession.insert("mapper.admin.goods.insertNewGoods", newGoodsMap);
	}
	
	@Override
	public List<GoodsVO> selectNewGoodsList(Map condMap) throws DataAccessException {
		return sqlSession.selectList("mapper.admin.goods.selectNewGoodsList",condMap);
	}

	@Override
	public GoodsVO selectGoodsDetail(int goods_id) throws DataAccessException {
		return sqlSession.selectOne("mapper.admin.goods.selectGoodsDetail", goods_id);
	}
	
	@Override
	public List selectGoodsImageFileList(int goods_id) throws DataAccessException {
		return sqlSession.selectList("mapper.admin.goods.selectGoodsImageFileList", goods_id);
	}

	@Override
	public void insertGoodsImageFile(List fileList) throws DataAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateGoodsInfo(Map goodsMap) throws DataAccessException {
		sqlSession.update("mapper.admin.goods.updateGoodsInfo",goodsMap);
	}

	@Override
	public void updateGoodsImage(List<ImageFileVO> imageFileList) throws DataAccessException {
		for(int i=0;i<imageFileList.size();i++) {
			ImageFileVO imageFileVO = imageFileList.get(i);
			sqlSession.update("mapper.admin.goods.updateGoodsImage",imageFileVO);
		}
	}

	@Override
	public void deleteGoodsImage(int image_id) throws DataAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteGoodsImage(List fileList) throws DataAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<OrderVO> selectOrderGoodsList(Map condMap) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateOrderGoods(Map orderMap) throws DataAccessException {
		// TODO Auto-generated method stub
		
	}






	

}
