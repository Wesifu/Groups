package com.j1091.action;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.j1091.pojo.Goods;
import com.j1091.pojo.User;
import com.jspsmart.upload.SmartFile;
import com.jspsmart.upload.SmartFiles;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.zyj.biz.Goodsbiz;
import com.zyj.biz.imp.Goodsbizimp;

/**
 * Servlet implementation class AddGood
 */
@WebServlet("/UpdGood")
public class UpdGood extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private ServletConfig config;
	private Goodsbiz gbiz = new Goodsbizimp();
    @Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		this.config =config;
	}

	/**
     * @see HttpServlet#HttpServlet()
     */
    public UpdGood() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//�õ���ʵ����Ŀ·��
		String realpth =this.getServletContext().getRealPath("/gpic");
		 // �½�һ��SmartUpload����  
        SmartUpload su = new SmartUpload();  
        // �ϴ���ʼ��  
        su.initialize(config, request, response);;  
        //�����ϴ�����  
        //1.����ÿ���ϴ��ļ�����󳤶�Ϊ10MB  
        su.setMaxFileSize(10 * 1024 * 1024);  
        //2.�������ϴ��ļ��ĳ���  
        su.setTotalMaxFileSize(30 * 1024 * 1024);  
        //3.�趨�����ϴ����ļ�  
        // su.setAllowedFilesList("txt,jpg");  
        //4.�趨��ֹ�ϴ����ļ�  
        try {
			su.setDeniedFilesList("exe,bat,jsp,htm,html,,");
			 //�ϴ��ļ�  
	        su.upload();  
	      SmartFiles smartFiles = su.getFiles();
	      
	      if(smartFiles!=null){
	    	  //�õ��ļ�
	    	  SmartFile smartFile = smartFiles.getFile(0);
	    	  //�õ��ļ���
	    	  String gname =smartFile.getFileName();
	    	  
	    	  //session�������õ��û�
	    	  User user =(User) request.getSession().getAttribute("user");
	    	  //����ʵ�壬���з�װ����
	    	  Goods goods = new Goods();
	    	  //System.out.println("aaaaaaaaaaaa"+su.getRequest().getParameter("goodpic"));
	    	  goods.setId(Integer.valueOf(su.getRequest().getParameter("goodsid")));
	    	  goods.setName(su.getRequest().getParameter("name"));
	    	  goods.setDetails(su.getRequest().getParameter("details"));
	    	  if (gname.equals("")) {
	    		  goods.setGoodspic(su.getRequest().getParameter("goodpic"));
			  }
	    	  else {
	    		  goods.setGoodspic("gpic/"+gname);
	    	  }
	    	  
	    	  goods.setPrice(Double.valueOf(su.getRequest().getParameter("price")));
	    	  goods.setTuangoucount(Integer.valueOf(su.getRequest().getParameter("tuangoucount")));
	    	  goods.setType(Integer.valueOf(su.getRequest().getParameter("type")));
	    	  goods.setTraderid(user.getId()); 
	    	  
	    	  gbiz.update(goods);
	    	  
	    	 if (gname.equals("")) {
				
			 }else {
				 smartFile.saveAs(realpth+"/"+gname);
			 }
	    	  
	    	  request.getRequestDispatcher("index.jsp").forward(request, response);
	      }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SmartUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
       
		
	}

}
