package com.bookshop01.cart.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.bookshop01.cart.vo.CartVO;
import com.bookshop01.goods.vo.GoodsVO;

@Repository("cartDAO")
public class CartDAOImpl  implements  CartDAO{
	@Autowired
	private SqlSession sqlSession;

	@Override
	public List<CartVO> selectCartList(CartVO cartVO) throws DataAccessException {
		return sqlSession.selectList("mapper.cart.selectCartList",cartVO);
	}

	@Override
	public List<GoodsVO> selectGoodsList(List<CartVO> cartList) throws DataAccessException {
		List<GoodsVO> myGoodsList;
		myGoodsList = sqlSession.selectList("mapper.cart.selectGoodsList", cartList);
		return myGoodsList;
	}

	@Override
	public boolean selectCountInCart(CartVO cartVO) throws DataAccessException {
		String result=sqlSession.selectOne("mapper.cart.selectCountInCart",cartVO);
		return Boolean.parseBoolean(result);
	}

	@Override
	public void insertGoodsInCart(CartVO cartVO) throws DataAccessException {
		sqlSession.insert("mapper.cart.insertGoodsInCart", cartVO);
	}

	@Override
	public void updateCartGoodsQty(CartVO cartVO) throws DataAccessException {
		sqlSession.update("mapper.cart.updateCartGoodsQty", cartVO);
		
	}


	@Override
	public void deleteCartGoods(int cart_id, int goods_id, String member_id) throws DataAccessException {
		Map cartMap = new HashMap();
		cartMap.put("cart_id", cart_id);
		cartMap.put("goods_id",goods_id);
		cartMap.put("member_id", member_id);
		
		sqlSession.delete("mapper.cart.deleteGoodsInCart", cartMap);
	}
	
}
