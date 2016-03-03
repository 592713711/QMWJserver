package dao;

import java.sql.*;
/**
 * 连接数据库
 * @author zzc
 *
 */
public class DataBase {

	/**
	 * 获取连接对象
	 * 
	 * @return
	 */
	public static Connection getConnect() {
		Connection conn = null;
		// 查找驱动
		try {
			String URL = "jdbc:sqlserver://127.0.0.1:1433; DatabaseName=AQJH"; 
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			// 建立连接
			conn = DriverManager.getConnection(URL,"sa","123");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}
	
	/**
	 * 关闭资源
	 * 
	 * @param conn
	 * @param st
	 * @param rs
	 */
	public static void close(Connection conn, Statement st, ResultSet rs) {

		try {
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws SQLException {
		
	}

}
