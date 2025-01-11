package model.dao;

import java.time.LocalDate;

import model.bean.User;

public class AuthenticationDAO {
	private static final GenericDAO<User> genericDAO = new GenericDAO<>(
			rs -> new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("name")));

	public static User login(String username, String password) {
		String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
		return genericDAO.find(sql, username, password);
	}

	public static User register(String username, String password, String name) {
		String sql = "SELECT * FROM user WHERE username = ?";
		User usr = genericDAO.find(sql, username);
		if (usr != null)
			return null;
		sql = "INSERT INTO user (username, password, name, created_at) VALUES (?, ?, ?, ?)";
		int id = genericDAO.add(sql, username ,password, name, LocalDate.now());
		return new User(id, username, password, name);
	}
}
