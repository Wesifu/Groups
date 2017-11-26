package com.zyj.bo;

import com.j1091.pojo.Goods;

public class GWL {
    
	private  Goods goods;
    private  int count;
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
    
    public void add(){
    	count =count+1;
    }
    
    public void sub(){
    	count =count-1;
    }
}
