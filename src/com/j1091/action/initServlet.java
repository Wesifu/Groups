package com.j1091.action;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.j1091.dao.GoodsTypeDao;
import com.zyj.biz.GoodsTypeBiz;
import com.zyj.biz.imp.GoodsTypeBizimp;

public class initServlet  extends HttpServlet{

	private GoodsTypeBiz tbiz = new GoodsTypeBizimp();
	@Override
	public void init() throws ServletException {
		
	List<GoodsTypeDao> goodstype=tbiz.findByGoodsTypes();
	
	this.getServletContext().setAttribute("goodstype", goodstype);
		super.init();
	}

	
	
}
