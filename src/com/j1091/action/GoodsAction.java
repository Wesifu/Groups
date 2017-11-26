package com.j1091.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.j1091.controller.IAction;
import com.j1091.pojo.Goods;
import com.j1091.pojo.Orders;
import com.j1091.pojo.User;
import com.zyj.biz.Goodsbiz;
import com.zyj.biz.imp.Goodsbizimp;
import com.zyj.bo.AjaxReturn;
import com.zyj.bo.GWL;
import com.zyj.bo.PaymentUtil;

public class GoodsAction implements IAction {
	//每页显示多少条
	private int pageSize =8;
	//当前页
	private int pageCurr =0;
	//最后一页（数据总数）
	private int lastpage =0;
	//商品类型ID
	private int typeid =0; //默认全部
	Goodsbiz gbiz = new Goodsbizimp();
    private int gid;
	
    private double sum;
    
    
  //接收付款成功   goods_CallbackServlet.action
  	public String CallbackServlet(HttpServletRequest request,
  			HttpServletResponse response) {
  		// 验证请求来源和数据有效性
  		// 阅读支付结果参数说明
  		// System.out.println("==============================================");
  		String p1_MerId = request.getParameter("p1_MerId");
  		String r0_Cmd = request.getParameter("r0_Cmd");
  		String r1_Code = request.getParameter("r1_Code");
  		String r2_TrxId = request.getParameter("r2_TrxId");
  		String r3_Amt = request.getParameter("r3_Amt");
  		String r4_Cur = request.getParameter("r4_Cur");
  		String r5_Pid = request.getParameter("r5_Pid");
  		String r6_Order = request.getParameter("r6_Order");
  		String r7_Uid = request.getParameter("r7_Uid");
  		String r8_MP = request.getParameter("r8_MP");
  		String r9_BType = request.getParameter("r9_BType");
  		String rb_BankId = request.getParameter("rb_BankId");
  		String ro_BankOrderId = request.getParameter("ro_BankOrderId");
  		String rp_PayDate = request.getParameter("rp_PayDate");
  		String rq_CardNo = request.getParameter("rq_CardNo");
  		String ru_Trxtime = request.getParameter("ru_Trxtime");

  		// hmac
  		String hmac = request.getParameter("hmac");
  		// 利用本地密钥和加密算法 加密数据
  		String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
  		boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
  				r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
  				r8_MP, r9_BType, keyValue);
  		if (isValid) {
  			// 有效
  			if (r9_BType.equals("1")) {
  				// 浏览器重定向
  			  return  "@json_"+("支付成功！订单号：" + r6_Order + "金额：" + r3_Amt);
  			} else if (r9_BType.equals("2")) {
  				
  				// 服务器点对点，来自于易宝的通知
  				System.out.println("收到易宝通知，修改订单状态！");//
  				// 回复给易宝success，如果不回复，易宝会一直通知
  				return  "@json_"+("success");
  			}

  		} else {
  			throw new RuntimeException("数据被篡改！");
  		}
  		
  		return "index.jsp";
  	
  	}
    //goods_pay.action
    public String pay(HttpServletRequest request,
 			HttpServletResponse response) {
    	System.out.println("---------------");
		String orderid = request.getParameter("orderid");
		String money = request.getParameter("money");
		String pd_Id = request.getParameter("pd_FrpId");
		System.out.println(orderid+"  "+money+"     "+pd_Id);

		String p0_Cmd = "Buy";
		String p1_MerId = "10001126856";// 真实
		String p2_Order = orderid; //唯一 不能为空
		String p3_Amt = money;
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		String p8_Url = "http://localhost:8080/Groups/goods_CallbackServlet.action";//接收付款成功后的回傳值
		String p9_SAF = "";
		String pa_MP = "";
		String pd_FrpId = pd_Id; //银行编号
		String pr_NeedResponse = "";

		String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";

		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur,
				p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId,
				pr_NeedResponse, keyValue);
		
		// 将所有参数 发送给 易宝指定URL
		request.setAttribute("pd_FrpId", pd_FrpId);
		request.setAttribute("p0_Cmd", p0_Cmd);
		request.setAttribute("p1_MerId", p1_MerId);
		request.setAttribute("p2_Order", p2_Order);
		request.setAttribute("p3_Amt", p3_Amt);
		request.setAttribute("p4_Cur", p4_Cur);
		request.setAttribute("p5_Pid", p5_Pid);
		request.setAttribute("p6_Pcat", p6_Pcat);
		request.setAttribute("p7_Pdesc", p7_Pdesc);
		request.setAttribute("p8_Url", p8_Url);
		request.setAttribute("p9_SAF", p9_SAF);
		request.setAttribute("pa_MP", pa_MP);
		request.setAttribute("pr_NeedResponse", pr_NeedResponse);
		request.setAttribute("hmac", hmac);
		
		
		
		 return "confirm.jsp";
    	
    }
  //Groups/goods_order.action?sum=15.0
    public String order(HttpServletRequest request,
 			HttpServletResponse response) {
	User use =(User) request.getSession().getAttribute("user");
	if(use!=null){
		Orders orders = new Orders();
		//订单编号
		orders.setOrderid(use.getId()+""+new Date().getTime());
		//用户id
		orders.setUserid(use.getId());
		orders.setRealname(use.getRealname());
		orders.setAddress(use.getAddress());
		orders.setPhone(use.getPhone());
		orders.setEmail(use.getEmail());
		orders.setTotal(sum);
		orders.setTime(new Date());
		orders.setStatus(0);//0代表未付款 1代表已经付款 
		
		gbiz.insertOrder(orders);
		
		request.setAttribute("order", orders);
		return "order.jsp";
	}
    	return "login.jsp";
    }
    
   //减少数量
//window.location.href="/Groups/goods_subshopcar.action?gid="+this.id;
    public String subshopcar(HttpServletRequest request,
 			HttpServletResponse response) {
    	//生成购物车
    	Map<Integer,GWL> gwc = (Map<Integer, GWL>) request.getSession().getAttribute("gwc");
	   if(gwc==null){
		   gwc = new HashMap();
	   }
    	//判断商品是否在购物车
    	GWL gwl =	gwc.get(gid);
    //判断是否第一次添加商品
    	if(gwl==null){
    		gwl = new GWL();
    		gwl.setGoods((Goods)gbiz.findByID(gid));
    		gwl.setCount(0);
    		
    	}
    	if(gwl.getCount()>1){
    		gwl.sub();
    		gwc.put(gid, gwl);
    		}
    	else {
    		gwc.remove(gid);
			}
    	
    	//商品信息+数量 放入购物车
    	
    	//购物车放入session
    	request.getSession().setAttribute("gwc", gwc);
    	return "shopcar.jsp";
    }
    
    //增加数量
    public String addgoods(HttpServletRequest request,
 			HttpServletResponse response) {
    	//生成购物车
    	Map<Integer,GWL> gwc = (Map<Integer, GWL>) request.getSession().getAttribute("gwc");
	   if(gwc==null){
		   gwc = new HashMap();
	   }
    	//判断商品是否在购物车
    	GWL gwl =	gwc.get(gid);
    //判断是否第一次添加商品
    	if(gwl==null){
    		gwl = new GWL();
    		gwl.setGoods((Goods)gbiz.findByID(gid));
    		gwl.setCount(1);
    		
    	}
    	
    		gwl.add();
    		gwc.put(gid, gwl);
			
    	
    	//商品信息+数量 放入购物车
    	
    	//购物车放入session
    	request.getSession().setAttribute("gwc", gwc);
    	return "shopcar.jsp";
    }

    //删除某条记录
    public String removegoods(HttpServletRequest request,
 			HttpServletResponse response) {
    	//生成购物车
    	Map<Integer,GWL> gwc = (Map<Integer, GWL>) request.getSession().getAttribute("gwc");
	   if(gwc==null){
		   gwc = new HashMap();
	   }
	    gwc.remove(gid);
    	//购物车放入session
    	request.getSession().setAttribute("gwc", gwc);
    	return "shopcar.jsp";
    }
    
    //goods_addshopcar.action
	
    public String addshopcar(HttpServletRequest request,
			HttpServletResponse response) {
	//生成购物车
    	Map<Integer,GWL> gwc = (Map<Integer, GWL>) request.getSession().getAttribute("gwc");
   if(gwc==null){
	   gwc = new HashMap();
   }
    	//判断商品是否在购物车
    	GWL gwl =	gwc.get(gid);
    //判断是否第一次添加商品
    	if(gwl==null){
    		gwl = new GWL();
    		gwl.setGoods((Goods)gbiz.findByID(gid));
    		gwl.setCount(0);
    		
    	}
    		gwl.add();
    	//商品信息+数量 放入购物车
    	gwc.put(gid, gwl);
    	//购物车放入session
    	request.getSession().setAttribute("gwc", gwc);
    	//弹框数据
    	AjaxReturn ajaxReturn = new AjaxReturn();
    	ajaxReturn.setHead("ok");
    	ajaxReturn.setBody("添加购物车成功！！！"+((Goods)gbiz.findByID(gid)).getName());
    	
    	Gson gson = new Gson();
    	return "@json_"+gson.toJson(ajaxReturn);
    }
     
	//http://localhost:8080/Groups/goods_todetailgood.action?gid=2
	
    //商品详情
	public String todetailgood(HttpServletRequest request,
			HttpServletResponse response) {
				
		Goods goods = gbiz.findByID(gid);
		request.setAttribute("goods", goods);
		
		return "detailgood.jsp";
	}
	
	//跳到修改商品的界面
	public String toupdgood(HttpServletRequest request,
			HttpServletResponse response) {
		Goods goods = gbiz.findByID(gid);
		request.setAttribute("goods", goods);
		
		return "updgood.jsp";
		
	}
	
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		
		if(pageCurr<0){
			pageCurr=0;
		}
		lastpage =gbiz.pageCount(typeid);
		//System.out.println(lastpage);
		if(lastpage%pageSize==0){
			lastpage=lastpage/pageSize-1;
		}else {
			lastpage=lastpage/pageSize;
		}
		if(lastpage<0){
			lastpage=0;
		}
		if(pageCurr>=lastpage){
			pageCurr=lastpage;
		}
		//分页查询数据
		List<Goods> goods=gbiz.findByPage(pageCurr,typeid,pageSize);
		System.out.println(goods);
		//封装数据
		request.setAttribute("goods", goods);
		request.setAttribute("pageCurr", pageCurr);
		request.setAttribute("lastpage", lastpage);
		request.setAttribute("typeid", typeid);
		return "index.jsp";
	}
///////////////////////////////////////////////////////////
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageCurr() {
		return pageCurr;
	}
	public void setPageCurr(int pageCurr) {
		this.pageCurr = pageCurr;
	}
	public int getLastpage() {
		return lastpage;
	}
	public void setLastpage(int lastpage) {
		this.lastpage = lastpage;
	}
	public int getTypeid() {
		return typeid;
	}
	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	
}
