package model.bo;

import java.io.File;
import java.io.IOException;

import controller.UploadServlet;
import controller.Util;
import jakarta.servlet.http.Part;
import model.dao.StudioDAO;
import model.dao.UploadDAO;

public class UploadBO {
	public static void uploadVideoChunk(String videoPath, Part videoChunk) throws IOException {
		UploadDAO.uploadVideoChunk(videoPath, videoChunk);
	}

	public static void updateThumbnail(String thumbnailExtension, String videoId, String videoPath) {
		if (thumbnailExtension == null)
			thumbnailExtension = "png";
		String thumbnailPath = UploadServlet.uploadThumbnailPath + File.separator + videoId + "." + thumbnailExtension;
		if (!new File(thumbnailPath).exists()) {
			Util.getThumbnail(thumbnailPath, videoPath);
		}
	}

	public static String updateDuration(String videoPath, int videoId) {
		String duration = Util.getVideoDuration(videoPath);
		StudioDAO.uploadDuration(duration, videoId);
		return duration;
	}
}
