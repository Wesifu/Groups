package com.j1091.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.j1091.controller.IAction;
import com.j1091.pojo.User;
import com.zyj.biz.Userbiz;
import com.zyj.biz.imp.Userbizimp;

public class UserAction implements IAction {
   
	private String msg;
	private String name;
	private String pwd;
	private Userbiz ubiz = new Userbizimp();
	private User user = new User();
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("默认方法");
		System.out.println(msg);
		return null;
	}
	//注册方法
	public String regist(HttpServletRequest request,
			HttpServletResponse response) {
		//首先判断用户名是否存
		
		String username =user.getUsername();
		System.out.println(username);
		User user1 =ubiz.findByName(username);
		//先检查看是否有 没有设置的属性
		user.setRegtime(new Date());
		if(user1!=null){
		request.setAttribute("msg", "该用户已经存在");
		return "regist.jsp";
		}else{
			//如果用户不存在， 那么进行增加操作
			int row =ubiz.insert(user);
			if(row>0){
				request.setAttribute("msg", "注册成功，请登录");
				return "login.jsp";
			}else{
				request.setAttribute("msg", "注册失败，请重新注册");
				return "regist.jsp";
			}
		}
		}
	//注销方法
	public String logout(HttpServletRequest request,
			HttpServletResponse response) {
		request.getSession().removeAttribute("user");
		return "login.jsp";
	}
	//登录方法
	public String login(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("登录方法");
	   //找到用户名
		User  user	=ubiz.findByName(name);
		
		if(user!=null&&pwd.equals(user.getUserpwd())){
			//如果登录成功，用户信息放入session
			request.getSession().setAttribute("user", user);
			return "index.jsp";
		}else{
			request.setAttribute("msg", "用户名或者密码错误");
			return "login.jsp";
		}
		
	}
	//----------------------------------

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user1) {
		this.user = user;
	}

	
}
