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
	//ÿҳ��ʾ������
	private int pageSize =8;
	//��ǰҳ
	private int pageCurr =0;
	//���һҳ������������
	private int lastpage =0;
	//��Ʒ����ID
	private int typeid =0; //Ĭ��ȫ��
	Goodsbiz gbiz = new Goodsbizimp();
    private int gid;
	
    private double sum;
    
    
  //���ո���ɹ�   goods_CallbackServlet.action
  	public String CallbackServlet(HttpServletRequest request,
  			HttpServletResponse response) {
  		// ��֤������Դ��������Ч��
  		// �Ķ�֧���������˵��
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
  		// ���ñ�����Կ�ͼ����㷨 ��������
  		String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";
  		boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
  				r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
  				r8_MP, r9_BType, keyValue);
  		if (isValid) {
  			// ��Ч
  			if (r9_BType.equals("1")) {
  				// ������ض���
  			  return  "@json_"+("֧���ɹ��������ţ�" + r6_Order + "��" + r3_Amt);
  			} else if (r9_BType.equals("2")) {
  				
  				// ��������Ե㣬�������ױ���֪ͨ
  				System.out.println("�յ��ױ�֪ͨ���޸Ķ���״̬��");//
  				// �ظ����ױ�success��������ظ����ױ���һֱ֪ͨ
  				return  "@json_"+("success");
  			}

  		} else {
  			throw new RuntimeException("���ݱ��۸ģ�");
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
		String p1_MerId = "10001126856";// ��ʵ
		String p2_Order = orderid; //Ψһ ����Ϊ��
		String p3_Amt = money;
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		String p8_Url = "http://localhost:8080/Groups/goods_CallbackServlet.action";//���ո���ɹ���Ļ؂�ֵ
		String p9_SAF = "";
		String pa_MP = "";
		String pd_FrpId = pd_Id; //���б��
		String pr_NeedResponse = "";

		String keyValue = "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl";

		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur,
				p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId,
				pr_NeedResponse, keyValue);
		
		// �����в��� ���͸� �ױ�ָ��URL
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
		//�������
		orders.setOrderid(use.getId()+""+new Date().getTime());
		//�û�id
		orders.setUserid(use.getId());
		orders.setRealname(use.getRealname());
		orders.setAddress(use.getAddress());
		orders.setPhone(use.getPhone());
		orders.setEmail(use.getEmail());
		orders.setTotal(sum);
		orders.setTime(new Date());
		orders.setStatus(0);//0����δ���� 1�����Ѿ����� 
		
		gbiz.insertOrder(orders);
		
		request.setAttribute("order", orders);
		return "order.jsp";
	}
    	return "login.jsp";
    }
    
   //��������
//window.location.href="/Groups/goods_subshopcar.action?gid="+this.id;
    public String subshopcar(HttpServletRequest request,
 			HttpServletResponse response) {
    	//���ɹ��ﳵ
    	Map<Integer,GWL> gwc = (Map<Integer, GWL>) request.getSession().getAttribute("gwc");
	   if(gwc==null){
		   gwc = new HashMap();
	   }
    	//�ж���Ʒ�Ƿ��ڹ��ﳵ
    	GWL gwl =	gwc.get(gid);
    //�ж��Ƿ��һ�������Ʒ
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
    	
    	//��Ʒ��Ϣ+���� ���빺�ﳵ
    	
    	//���ﳵ����session
    	request.getSession().setAttribute("gwc", gwc);
    	return "shopcar.jsp";
    }
    
    //��������
    public String addgoods(HttpServletRequest request,
 			HttpServletResponse response) {
    	//���ɹ��ﳵ
    	Map<Integer,GWL> gwc = (Map<Integer, GWL>) request.getSession().getAttribute("gwc");
	   if(gwc==null){
		   gwc = new HashMap();
	   }
    	//�ж���Ʒ�Ƿ��ڹ��ﳵ
    	GWL gwl =	gwc.get(gid);
    //�ж��Ƿ��һ�������Ʒ
    	if(gwl==null){
    		gwl = new GWL();
    		gwl.setGoods((Goods)gbiz.findByID(gid));
    		gwl.setCount(1);
    		
    	}
    	
    		gwl.add();
    		gwc.put(gid, gwl);
			
    	
    	//��Ʒ��Ϣ+���� ���빺�ﳵ
    	
    	//���ﳵ����session
    	request.getSession().setAttribute("gwc", gwc);
    	return "shopcar.jsp";
    }

    //ɾ��ĳ����¼
    public String removegoods(HttpServletRequest request,
 			HttpServletResponse response) {
    	//���ɹ��ﳵ
    	Map<Integer,GWL> gwc = (Map<Integer, GWL>) request.getSession().getAttribute("gwc");
	   if(gwc==null){
		   gwc = new HashMap();
	   }
	    gwc.remove(gid);
    	//���ﳵ����session
    	request.getSession().setAttribute("gwc", gwc);
    	return "shopcar.jsp";
    }
    
    //goods_addshopcar.action
	
    public String addshopcar(HttpServletRequest request,
			HttpServletResponse response) {
	//���ɹ��ﳵ
    	Map<Integer,GWL> gwc = (Map<Integer, GWL>) request.getSession().getAttribute("gwc");
   if(gwc==null){
	   gwc = new HashMap();
   }
    	//�ж���Ʒ�Ƿ��ڹ��ﳵ
    	GWL gwl =	gwc.get(gid);
    //�ж��Ƿ��һ�������Ʒ
    	if(gwl==null){
    		gwl = new GWL();
    		gwl.setGoods((Goods)gbiz.findByID(gid));
    		gwl.setCount(0);
    		
    	}
    		gwl.add();
    	//��Ʒ��Ϣ+���� ���빺�ﳵ
    	gwc.put(gid, gwl);
    	//���ﳵ����session
    	request.getSession().setAttribute("gwc", gwc);
    	//��������
    	AjaxReturn ajaxReturn = new AjaxReturn();
    	ajaxReturn.setHead("ok");
    	ajaxReturn.setBody("��ӹ��ﳵ�ɹ�������"+((Goods)gbiz.findByID(gid)).getName());
    	
    	Gson gson = new Gson();
    	return "@json_"+gson.toJson(ajaxReturn);
    }
     
	//http://localhost:8080/Groups/goods_todetailgood.action?gid=2
	
    //��Ʒ����
	public String todetailgood(HttpServletRequest request,
			HttpServletResponse response) {
				
		Goods goods = gbiz.findByID(gid);
		request.setAttribute("goods", goods);
		
		return "detailgood.jsp";
	}
	
	//�����޸���Ʒ�Ľ���
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
		//��ҳ��ѯ����
		List<Goods> goods=gbiz.findByPage(pageCurr,typeid,pageSize);
		System.out.println(goods);
		//��װ����
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
