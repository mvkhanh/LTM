package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dto.UserDTO;

@WebServlet("/progress")
public class ProgressServlet extends HttpServlet {
	public static Map<Integer, Map<Integer, ProcessThread>> processThreads = new HashMap<>();
	public static Map<Integer, ArrayList<Integer>> done = new HashMap<>();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Cấu hình header cho SSE
		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		if(request.getSession().getAttribute("account") == null) return;
		Integer userId = ((UserDTO) request.getSession().getAttribute("account")).getId();
		Map<Integer, ProcessThread> videos = processThreads.get(userId);
		ArrayList<Integer> dones = done.get(userId);
		try (PrintWriter writer = response.getWriter()) {
			if (dones != null)
				for (int i = 0; i < dones.size(); i++) {
					writer.write("data: " + dones.get(i) + " tmp\n\n");
					dones.remove(i);
				}
			if (videos != null && videos.size() > 0) {
				for (Map.Entry<Integer, ProcessThread> video : videos.entrySet()) {
					// Gửi dữ liệu theo định dạng SSE
					writer.write("data: " + video.getKey() + " " + video.getValue().progress + " "
							+ video.getValue().remainingTime + "\n\n");
					writer.flush(); // Đảm bảo dữ liệu được gửi ngay lập tức
				}
			} else {
				// Gửi thông báo nếu không có video
				writer.write("data: no_video_progress\n\n");
				writer.flush();
			}
		}
	}
}