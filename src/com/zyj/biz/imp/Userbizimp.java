package com.zyj.biz.imp;

import com.j1091.dao.UserDao;
import com.j1091.pojo.User;
import com.zyj.biz.Userbiz;

public class Userbizimp implements Userbiz {
   
	private UserDao udao = new UserDao();   
	@Override
	public User findByName(String name) {
		// TODO Auto-generated method stub
		return udao.findByName(name);
	}
	@Override
	public int insert(User user1) {
		// TODO Auto-generated method stub
		return udao.insert(user1);
	}

}
