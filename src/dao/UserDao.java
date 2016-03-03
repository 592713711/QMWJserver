package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Monitor;
import model.User;

/**
 * ����Users��
 * 
 * @author zzc
 * 
 */
public class UserDao {
	Connection conn = null;
	ResultSet rs = null;

	public UserDao(Connection conn) {
		this.conn = conn;
	}

	// ���ݼ�� ��ҵ�½
	public ResultSet getLogin(String username, String pwd) {
		try {
			String sql = "select * from users where UserName=? and Password =?";
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setString(1, username);
			pstm.setString(2, pwd);
			rs = pstm.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * ͨ���û�id �õ���������û��б�
	 * 
	 * @param id
	 * @return
	 */
	public ArrayList<Monitor> getMonitorsById(int id) {
		ArrayList<Monitor> list = new ArrayList<Monitor>();
		try {
			String sql = "select * from relate where user_id=?";
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setInt(1, id);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				Monitor monitor = new Monitor();
				monitor.id = rs.getInt("monitor_id");
				User user = getUserById(monitor.id);
				monitor.username = user.username;
				monitor.status = user.status;
				monitor.remark_name = rs.getString("remark_name");
				list.add(monitor);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ͨ�� id�õ��û���
	 * 
	 * @param id
	 * @return
	 */
	public User getUserById(int id) {
		User user = new User();
		try {
			String sql = "select * from users where id=?";
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setInt(1, id);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				user.username = rs.getString("username");
				user.status = rs.getInt("status");
				user.id = id;
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;
	}
}
