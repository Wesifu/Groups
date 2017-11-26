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
		//拿到真实的项目路径
		String realpth =this.getServletContext().getRealPath("/gpic");
		 // 新建一个SmartUpload对象  
        SmartUpload su = new SmartUpload();  
        // 上传初始化  
        su.initialize(config, request, response);;  
        //设置上传限制  
        //1.限制每个上传文件的最大长度为10MB  
        su.setMaxFileSize(10 * 1024 * 1024);  
        //2.限制总上传文件的长度  
        su.setTotalMaxFileSize(30 * 1024 * 1024);  
        //3.设定允许上传的文件  
        // su.setAllowedFilesList("txt,jpg");  
        //4.设定禁止上传的文件  
        try {
			su.setDeniedFilesList("exe,bat,jsp,htm,html,,");
			 //上传文件  
	        su.upload();  
	      SmartFiles smartFiles = su.getFiles();
	      
	      if(smartFiles!=null){
	    	  //拿到文件
	    	  SmartFile smartFile = smartFiles.getFile(0);
	    	  //拿到文件名
	    	  String gname =smartFile.getFileName();
	    	  
	    	  //session作用域拿到用户
	    	  User user =(User) request.getSession().getAttribute("user");
	    	  //创建实体，进行封装数据
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
