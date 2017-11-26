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
		System.out.println("Ĭ�Ϸ���");
		System.out.println(msg);
		return null;
	}
	//ע�᷽��
	public String regist(HttpServletRequest request,
			HttpServletResponse response) {
		//�����ж��û����Ƿ��
		
		String username =user.getUsername();
		System.out.println(username);
		User user1 =ubiz.findByName(username);
		//�ȼ�鿴�Ƿ��� û�����õ�����
		user.setRegtime(new Date());
		if(user1!=null){
		request.setAttribute("msg", "���û��Ѿ�����");
		return "regist.jsp";
		}else{
			//����û������ڣ� ��ô�������Ӳ���
			int row =ubiz.insert(user);
			if(row>0){
				request.setAttribute("msg", "ע��ɹ������¼");
				return "login.jsp";
			}else{
				request.setAttribute("msg", "ע��ʧ�ܣ�������ע��");
				return "regist.jsp";
			}
		}
		}
	//ע������
	public String logout(HttpServletRequest request,
			HttpServletResponse response) {
		request.getSession().removeAttribute("user");
		return "login.jsp";
	}
	//��¼����
	public String login(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("��¼����");
	   //�ҵ��û���
		User  user	=ubiz.findByName(name);
		
		if(user!=null&&pwd.equals(user.getUserpwd())){
			//�����¼�ɹ����û���Ϣ����session
			request.getSession().setAttribute("user", user);
			return "index.jsp";
		}else{
			request.setAttribute("msg", "�û��������������");
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
