package com.j1091.dao;

import java.beans.PropertyVetoException;
import java.io.IOException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

//���ӳ�   --����
public class C3P0DBPool {
	private ComboPooledDataSource ds;

	private static C3P0DBPool pool = new C3P0DBPool();

	private java.util.Properties prop;
	private String DBURL;
	private String DBUSR;
	private String DBPWD;
	private String DBDRIVRE;
	private int InitialPoolSize;
	private int MaxPoolSize;
	private int MinPoolSize;
	private int MaxIdleTime;
	private int MaxStatements;

	private C3P0DBPool() {

		prop = new java.util.Properties();
		try {
			prop.load(this.getClass().getClassLoader()
					.getResourceAsStream("DBconfig.properties"));
			DBURL = prop.getProperty("DBURL");
			DBUSR = prop.getProperty("DBUSR");
			DBPWD = prop.getProperty("DBPWD");
			DBDRIVRE = prop.getProperty("DBDRIVRE");
			InitialPoolSize = Integer.parseInt(prop
					.getProperty("InitialPoolSize"));
			MaxPoolSize = Integer.parseInt(prop.getProperty("MaxPoolSize"));
			MinPoolSize = Integer.parseInt(prop.getProperty("MinPoolSize"));
			MaxIdleTime = Integer.parseInt(prop.getProperty("MaxIdleTime"));
			MaxStatements = Integer.parseInt(prop.getProperty("MaxStatements"));

			// ���ݿ����ӳ���Ϣ
			ds = new ComboPooledDataSource();
			ds.setDriverClass(DBDRIVRE);
			ds.setUser(DBUSR);
			ds.setPassword(DBPWD);
			ds.setJdbcUrl(DBURL);

			// ���ӳز���
			// <!--��ʼ��ʱ��ȡ�������ӣ�ȡֵӦ��minPoolSize��maxPoolSize֮�䡣Default: 3 -
			ds.setInitialPoolSize(InitialPoolSize);
			// <!-- ��������� -->
			ds.setMaxPoolSize(MaxPoolSize);
			// !-- ��С������ -->
			ds.setMinPoolSize(MinPoolSize);
			// <!-- ����PreparedStatement������ -->
			ds.setMaxStatements(MaxStatements);
			// <!-- ÿ��120�������ӳ���Ŀ������� ����λ����-->
			ds.setMaxIdleTime(MaxIdleTime);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static C3P0DBPool getPool() {
		return pool;
	}

	public DataSource getDataSource() {
		return ds;
	}
}
