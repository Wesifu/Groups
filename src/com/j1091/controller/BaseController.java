package com.j1091.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController extends HttpServlet {
	private Properties properties = null;
	// Ĭ��UTF-8
	private String charset = "UTF-8";
	private static final String Jsonregex = "^@json_";
	private static final String Redregex = "^@red_";
	// ��ʽ������
	private SimpleDateFormat smt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// ��ʽ������
	private SimpleDateFormat smt2 = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		// ���ص�ǰservlet ��init-param
		String actioncfg = config.getInitParameter("actioncfg");
		properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader()
					.getResourceAsStream(actioncfg));
			// ��������ļ�������charset��ô���������ļ�������
			if (properties.getProperty("charset") != null) {
				charset = properties.getProperty("charset");
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("�������ļ�����");
		}
		super.init(config);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// �����ַ���
		request.setCharacterEncoding(charset);
		response.setCharacterEncoding(charset);
		// �õ��������
		String act = request.getRequestURI();
		// ��ȡ /userAct.do ���/�ͽ�ȡ.do
		act = act.substring(act.lastIndexOf("/") + 1,
				act.lastIndexOf(".action"));
        System.out.println(act);
		String classPath = properties.getProperty(act);
		String resultStr = null;
		// ���俪ʼ
		try {
			if (act.indexOf("_") == -1) {
				Class actClass = Class.forName(classPath);
				IAction action = (IAction) actClass.newInstance();
				autoSetterParameter(request, actClass, action);
				resultStr = action.execute(request, response);
			} else {
				String[] act_mth = act.split("_");
				// �õ�calssPath
				classPath = properties.getProperty(act_mth[0]);
				// �ҵ�Actionclass
				Class actClass = Class.forName(classPath);
				// �õ�һ������
				IAction action = (IAction) actClass.newInstance();
				// �ҵ���Ӧ�ķ���
				Method actMethod = actClass.getMethod(act_mth[1], new Class[] {
						HttpServletRequest.class, HttpServletResponse.class });
				autoSetterParameter(request, actClass, action);
				// ��̬�ĵ��÷���
				resultStr = (String) actMethod
						.invoke(action, request, response);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resultStr != null && !resultStr.equals("")) {
			if (regexHead(Jsonregex, resultStr)) {
				response.setContentType("text/html");
				// Json����
				// ȥ��ǰ׺ �磺@json_{a:b}ȥ����Ϊ{a:b}
				PrintWriter out = response.getWriter();
				resultStr = resultStr.replaceFirst(Jsonregex, "");
				out.print(resultStr);
			} else if (regexHead(Redregex, resultStr)) {
				// �ض���
				// ȥ��ǰ׺ �磺@red_index.jspȥ����Ϊindex.jsp
				resultStr = resultStr.replaceFirst(Redregex, "");
				response.sendRedirect(resultStr);
			} else {
				// Ĭ��ת��
				request.getRequestDispatcher(resultStr).forward(request,
						response);
			}

		} else {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.print("û�з���ֵ�����飡");
		}

	}

	/**
	 * �Զ�ע���
	 * 
	 * @param request
	 * @param actClass
	 * @param action
	 */
	private void autoSetterParameter(HttpServletRequest request,
			Class actClass, IAction action) {

		String fieldName = null;
		String setterName = null;
		Method setter = null;
		Class fieldType = null;

		// ��ȡ������
		Enumeration<String> pmNames = request.getParameterNames();

		while (pmNames.hasMoreElements()) {
			try { // ����������
				// �õ�������
				fieldName = pmNames.nextElement();

				if (fieldName.indexOf(".") == -1) {
					// �Ǹ�������
					// ͨ��������--->setter���� uname --setUname(String uname)
					setterName = field2Method(1, fieldName);
					// ��ͨ������
					// ��ȡsetter��������
					fieldType = actClass.getDeclaredField(fieldName).getType();
					// ��ȡsetter����
					setter = actClass.getDeclaredMethod(setterName, fieldType);
					// �ֲ�ͬ���ͷ�װֵ
					if (fieldType.equals(String.class)) {
						setter.invoke(action, request.getParameter(fieldName));
					} else if (fieldType.equals(Integer.class)
							|| fieldType.equals(int.class)) {
						setter.invoke(action, Integer.parseInt(request
								.getParameter(fieldName)));
					} else if (fieldType.equals(Float.class)
							|| fieldType.equals(float.class)) {
						setter.invoke(action, Float.parseFloat(request
								.getParameter(fieldName)));
					} else if (fieldType.equals(Double.class)
							|| fieldType.equals(double.class)) {
						setter.invoke(action, Double.parseDouble(request
								.getParameter(fieldName)));
					} else if (fieldType.equals(Date.class)) {
						if (request.getParameter(fieldName).length() <= 12) {
							setter.invoke(action,
									smt2.parse(request.getParameter(fieldName)));
						} else {
							setter.invoke(action,
									smt1.parse(request.getParameter(fieldName)));
						}

					}

				} else {
					// ��������
					// Ҫ��װ��pojo�������
					// ��Ҫ��װ��pojo

					// ȡ�ö�������������
					String mtd_field[] = fieldName.split("\\.");
					Class pojoClz = actClass.getDeclaredField(mtd_field[0])
							.getType();
					Class fieldClz = pojoClz.getDeclaredField(mtd_field[1])
							.getType();

					// action.getUsr().setLoginName(loginName);
					// ȡ����Ҫ��װ��pojo
					Object pojo = actClass.getMethod(
							field2Method(2, mtd_field[0])).invoke(action);
					// System.out.println(pojo.getClass());
					Method fieldSetter = pojoClz.getMethod(
							field2Method(1, mtd_field[1]), fieldClz);
					// ��װֵ
					if (fieldClz.equals(String.class)) {
						fieldSetter.invoke(pojo,
								request.getParameter(fieldName));
					} else if (fieldClz.equals(Integer.class)
							|| fieldClz.equals(int.class)) {
						fieldSetter.invoke(pojo, Integer.parseInt(request
								.getParameter(fieldName)));
					} else if (fieldClz.equals(Float.class)
							|| fieldClz.equals(float.class)) {
						fieldSetter.invoke(pojo, Float.parseFloat(request
								.getParameter(fieldName)));
					} else if (fieldClz.equals(Double.class)
							|| fieldClz.equals(double.class)) {
						fieldSetter.invoke(pojo, Double.parseDouble(request
								.getParameter(fieldName)));
					} else if (fieldClz.equals(Date.class)) {
						try {
							if (request.getParameter(fieldName).length() <= 12) {

								setter.invoke(action, smt2.parse(request
										.getParameter(fieldName)));
							} else {
								setter.invoke(action, smt1.parse(request
										.getParameter(fieldName)));
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
						}
					}

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}

		}

	}

	/**
	 * ����ת������
	 * 
	 * @param type
	 *            1-setter������2-getter����
	 * @param field
	 *            ��������
	 * @return
	 */
	private String field2Method(int type, String field) {
		String result = null;
		if (type == 1) {
			result = "set" + field.substring(0, 1).toUpperCase()
					+ field.substring(1);
		}
		if (type == 2) {
			result = "get" + field.substring(0, 1).toUpperCase()
					+ field.substring(1);
		}
		return result;
	}

	/**
	 * ��֤����ͷ
	 * 
	 * @param regex
	 *            ������ʽ
	 * @param resultStr
	 *            ��֤����
	 * @return true�ܲ��� ��flaseû�ҵ�
	 */
	private boolean regexHead(String regex, String resultStr) {
		boolean reslut = false;
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(regex);
		Matcher m = p.matcher(resultStr);

		if (m.find()) {
			if (m.start() == 0) {
				reslut = true;
			}
		}
		return reslut;
	}
}
