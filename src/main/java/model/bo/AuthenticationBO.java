package model.bo;

import model.bean.User;
import model.dao.AuthenticationDAO;
import model.dto.UserDTO;

public class AuthenticationBO {
	public static UserDTO login(String username, String password) {
		return entityToDTO(AuthenticationDAO.login(username, password));
	}
	
	public static UserDTO register(String username, String password, String name) {
		return entityToDTO(AuthenticationDAO.register(username, password, name));
	}
	
	public static UserDTO entityToDTO(User entity) {
		return entity != null ? new UserDTO(entity.getId(), entity.getName()) : null;
	}
}
