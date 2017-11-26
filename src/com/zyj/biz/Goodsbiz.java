package com.zyj.biz;

import java.util.List;

import com.j1091.pojo.Goods;
import com.j1091.pojo.Orders;

public interface Goodsbiz {

	int pageCount(int typeid);

	List<Goods> findByPage(int pageCurr, int typeid, int pageSize);

	void insert(Goods goods);

	Goods findByID(int gid);

	void update(Goods goods);
	
	void insertOrder(Orders orders);


}
