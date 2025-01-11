package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.bo.StudioBO;
import model.bo.UploadBO;
import model.dto.UserDTO;

@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String uploadThumbnailPath;
	public static String uploadVideoPath;

	public UploadServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		uploadThumbnailPath = getServletContext().getRealPath("/") + "data" + File.separator + "thumbnail";
		uploadVideoPath = getServletContext().getRealPath("/") + "data" + File.separator + "video";
		Util.createDirIfNotExist(uploadThumbnailPath);
		Util.createDirIfNotExist(uploadVideoPath);
		if (request.getParameter("title") != null) {
			int res = createVideo(request, response);
			response.getWriter().write(String.valueOf(res));
		} else
			uploadVideo(request, response);
	}

	private int createVideo(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		int user_id = ((UserDTO) request.getSession().getAttribute("account")).getId();
		String imgExtension = request.getParameter("thumbnailExtension");
		if(imgExtension == null) imgExtension = "png";
		String videoExtension = request.getParameter("videoExtension");
		int videoId = StudioBO.addVideo(title, description, user_id, imgExtension, videoExtension);
		Part thumbnailPart = request.getPart("thumbnail");
		if (thumbnailPart != null && thumbnailPart.getSize() > 0)
			thumbnailPart.write(uploadThumbnailPath + File.separator + videoId + "." + imgExtension);
		return videoId;
	}

	private void uploadVideo(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String videoId = request.getParameter("id");
		String videoExtension = request.getParameter("videoExtension");
		String videoPath = uploadVideoPath + File.separator + videoId + "." + videoExtension;
		int chunkIndex = Integer.parseInt(request.getParameter("chunkIndex"));
		int totalChunks = Integer.parseInt(request.getParameter("totalChunks"));
		Part videoChunk = request.getPart("videoChunk");
		UploadBO.uploadVideoChunk(videoPath, videoChunk);
		
		if (chunkIndex == totalChunks - 1) {
			String thumbnailExtension = request.getParameter("thumbnailExtension");
			UploadBO.updateThumbnail(thumbnailExtension, videoId, videoPath);
			String duration = UploadBO.updateDuration(videoPath, Integer.parseInt(videoId));
			startProcess(videoPath, videoId, Util.convertDurationToSecond(duration), ((UserDTO) request.getSession().getAttribute("account")).getId());
		}
		response.getWriter().write("Chunk " + chunkIndex + " uploaded.");
	}

	private void startProcess(String videoPath, String videoId, long duration, int userId) {
		ExecutorService executor = (ExecutorService) getServletContext().getAttribute("executor");
		executor.submit(new ProcessThread(videoPath, Integer.valueOf(videoId), duration, userId));
	}
}