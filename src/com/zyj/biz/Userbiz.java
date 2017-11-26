package com.zyj.biz;

import com.j1091.pojo.User;

public interface Userbiz {

	User findByName(String name);

	int insert(User user1);

}
