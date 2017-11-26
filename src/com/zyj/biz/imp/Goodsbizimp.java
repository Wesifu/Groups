package com.zyj.biz.imp;

import java.util.List;

import com.j1091.dao.GoodsDao;
import com.j1091.dao.OrdersDao;
import com.j1091.pojo.Goods;
import com.j1091.pojo.Orders;
import com.zyj.biz.Goodsbiz;

public  class Goodsbizimp implements Goodsbiz {

	GoodsDao gdao= new GoodsDao();
	OrdersDao odao = new OrdersDao();
	@Override
	public int pageCount(int typeid) {
		// TODO Auto-generated method stub
		return gdao.findCount(typeid);
	}
	@Override
	public List<Goods> findByPage(int pageCurr, int typeid, int pageSize) {
		if(typeid==0){
			List<Goods> goods=gdao.findBypage(pageCurr*pageSize, pageSize);
			return goods;
		}else{
			List<Goods> goods=gdao.findBypage(pageCurr*pageSize, pageSize,typeid);
		return goods;
	}
		}
	@Override
	public void insert(Goods goods) {
		
		gdao.insert(goods);
	}
	@Override
	public Goods findByID(int gid) {
		// TODO Auto-generated method stub
		return (Goods) gdao.findById(gid);
	}
	@Override
	public void update(Goods goods) {
		
		gdao.update(goods);
	}
	@Override
	public void insertOrder(Orders orders) {
		odao.insert(orders);
		
	}


}
