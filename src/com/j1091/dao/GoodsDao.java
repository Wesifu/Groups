package com.j1091.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.j1091.pojo.Goods;

public class GoodsDao extends BaseC3p0PoolDao {
	
	public int findCount(int typeid) {
		int max = 0;
		String sql=null;
		try {
	   if(typeid==0){
		   sql ="select count(id) from t_goods";
	   }else{
		   sql ="select count(id) from t_goods where type ="+typeid;
	   }
		
		
		Connection con = super.getConn();
		Statement st=con.createStatement();
		ResultSet rs =st.executeQuery(sql);
		while(rs.next()){
			max =rs.getInt(1);
		}
		super.closeConn();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return max;
	}

	public List<Goods> findBypage(int i, int pageSize) {
		
		return super.findSome("select * from t_goods limit ?,?",i,pageSize);
	}

	public List<Goods> findBypage(int i, int pageSize, int typeid) {
		// TODO Auto-generated method stub
		return super.findSome("select * from t_goods where type = ? limit ?,?",typeid,i,pageSize);
	}



}
