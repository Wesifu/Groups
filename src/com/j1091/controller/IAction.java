package com.j1091.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IAction {
	// Ĭ�ϴ���������߼����� 
	//String[1.Ĭ��ת����index.jsp��,2.�ض���(@red_index.jsp),3. ajax: @json_{sfsdf}]
	String execute(HttpServletRequest request, HttpServletResponse response);
}
