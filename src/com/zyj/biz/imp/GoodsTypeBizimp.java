package com.zyj.biz.imp;

import java.util.List;

import com.j1091.dao.GoodsTypeDao;
import com.zyj.biz.GoodsTypeBiz;

public class GoodsTypeBizimp implements GoodsTypeBiz {

	private GoodsTypeDao tdao = new GoodsTypeDao();
	@Override
	public List<GoodsTypeDao> findByGoodsTypes() {
		// TODO Auto-generated method stub
		return tdao.findAll();
	}

}
