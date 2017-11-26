package com.j1091.dao;

import java.util.List;

import com.j1091.pojo.User;

public class UserDao extends BaseC3p0PoolDao {

	public User findByName(String name) {
		// TODO Auto-generated method stub
		return (User) super.findOne("select * from t_user where username = ?", name);
	}

	

}
