package controller;

import java.io.IOException;
import java.net.InetAddress;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.bo.AuthenticationBO;
import model.dto.UserDTO;

@WebServlet(urlPatterns = { "/login", "/register", "/logout" })
public class AuthenticateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AuthenticateServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		switch (action) {
		case "/login":
			getLoginView(request, response);
			break;
		case "/logout":
			logout(request, response);
			break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		switch (action) {
		case "/login":
			login(request, response);
			break;
		case "/register":
			register(request, response);
			break;
		}
	}
	
	private void getLoginView(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if(request.getSession().getAttribute("account") != null) {
			response.sendRedirect(request.getContextPath());
			return;
		}
		Util.forward(this, request, response, "login");
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UserDTO usr = AuthenticationBO.login(username, password);
		if (usr != null) {
			request.getSession().setAttribute("account", usr);
			String redirectUrl = (String) request.getSession().getAttribute("redirectAfterLogin");

		    if (redirectUrl != null) {
		        request.getSession().removeAttribute("redirectAfterLogin");
		        response.sendRedirect(redirectUrl);
		    } else {
		        response.sendRedirect(request.getContextPath());
		    }
		} else
			Util.forward(this, request, response, "login");
	}

	private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("reg-username");
		String password = request.getParameter("reg-password");
		String name = request.getParameter("reg-name");
		UserDTO usr = AuthenticationBO.register(username, password, name);
		if (usr != null) {
			request.getSession().setAttribute("account", usr);
			response.sendRedirect(request.getContextPath());
		} else
			Util.forward(this, request, response, "login");
	}

	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().invalidate();
		response.sendRedirect(request.getContextPath());
	}
}
