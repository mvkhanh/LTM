package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.bean.Video;
import model.bo.StudioBO;

@WebServlet("/stream")
public class StreamServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String videoId = request.getParameter("id");
		if (videoId == null) {
			response.getWriter().write("{\"error\":\"Missing video ID\"}");
			return;
		}
		try {
			Video vid = StudioBO.getVideoById(Integer.parseInt(videoId));
			if (vid != null) {
				String jsonResponse = "{"
					    + "\"id\":" + vid.getId() + ","
					    + "\"title\":\"" + vid.getTitle().replaceAll("\"", "\\\"") + "\","
					    + "\"img_extension\":\"" + vid.getImg_extension() + "\","
					    + "\"description\":\"" + vid.getDescription().replaceAll("\"", "\\\"") + "\","
					    + "\"author\":\"" + vid.getName().replaceAll("\"", "\\\"") + "\","
					    + "\"duration\":\"" + vid.getDuration() + "\","
					    + "\"uploadDate\":\"" + vid.getCreated_at() + "\""
					    + "}";
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");

				response.getWriter().write(jsonResponse);
			} else {
				response.getWriter().write("{\"error\":\"Video not found\"}");
			}
		} catch (NumberFormatException e) {
			response.getWriter().write("{\"error\":\"Invalid video ID\"}");
		}
	}
}