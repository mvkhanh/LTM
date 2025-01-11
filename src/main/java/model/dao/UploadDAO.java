package model.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jakarta.servlet.http.Part;

public class UploadDAO {
	public static void uploadVideoChunk(String videoPath, Part videoChunk) throws IOException {
		File videoFile = new File(videoPath);
		try (InputStream input = videoChunk.getInputStream();
				OutputStream output = new FileOutputStream(videoFile, true)) {
			byte[] buffer = new byte[8192];
			int bytesRead;
			while ((bytesRead = input.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
		}
	}
}
