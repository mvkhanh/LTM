package controller;

import java.io.File;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.bo.StudioBO;
import model.dto.UserDTO;

@WebServlet(urlPatterns = { "/studio", "/deleteVideo" })
@MultipartConfig
public class StudioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public StudioServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		switch(action) {
		case "/studio":
			getStudioView(request, response);
			break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	private void getStudioView(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (!Util.isAuthenticated(request, response))
			return;
		request.setAttribute("videos",
				StudioBO.getVideos(((UserDTO) request.getSession().getAttribute("account")).getId()));
		Util.forward(this, request, response, "studio");
	}
	
	@Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		String videoId = request.getParameter("id");
        if (videoId != null) {
            boolean isDeleted = StudioBO.deleteVideo(Integer.parseInt(videoId), getServletContext().getRealPath("/") + "data" + File.separator);
            if (isDeleted) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
