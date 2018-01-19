package com.mwq.dao;

import java.sql.*;

public class JDBC {

	private static final String DRIVERCLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	private static final String URL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=db_DrinkeryManage";
			//"jdbc:microsoft:sqlserver://127.0.0.1:1433;DatabaseName=db_DrinkeryManage";

	private static final String USERNAME = "sa";

	private static final String PASSWORD = "123456zz";

	private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

	static {// 通过静态方法加载数据库驱动
		try {
			Class.forName(DRIVERCLASS).newInstance();// 加载数据库驱动
			System.out.println("数据库驱动加载成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {// 创建数据库连接的方法
		Connection conn = threadLocal.get();// 从线程中获得数据库连接
		if (conn == null) {// 没有可用的数据库连接
			try {
				conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);// 创建新的数据库连接
				threadLocal.set(conn);// 将数据库连接保存到线程中
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}

	public static boolean closeConnection() {// 关闭数据库连接的方法
		boolean isClosed = true;
		Connection conn = threadLocal.get();// 从线程中获得数据库连接
		threadLocal.set(null);// 清空线程中的数据库连接
		if (conn != null) {// 数据库连接可用
			try {
				conn.close();// 关闭数据库连接
			} catch (SQLException e) {
				isClosed = false;
				e.printStackTrace();
			}
		}
		return isClosed;
	}

}
