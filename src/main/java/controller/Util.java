package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Util {
	public static final String FFMPEG_PATH = "/opt/homebrew/bin/ffmpeg";
	public static final String VIDEO_PATH_FILE = "mvk.txt";

	public static boolean isAuthenticated(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getSession().getAttribute("account") == null) {
			String originalUrl = request.getRequestURI()
					+ (request.getQueryString() != null ? "?" + request.getQueryString() : "");
			request.getSession().setAttribute("redirectAfterLogin", originalUrl);
			response.sendRedirect("login");
			return false;
		}
		return true;
	}

	public static void forward(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response,
			String destination) throws ServletException, IOException {
		RequestDispatcher rd = servlet.getServletContext().getRequestDispatcher("/" + destination + ".jsp");
		rd.forward(request, response);
	}

	public static String getVideoDuration(String filePath) {
		String duration = null;
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(FFMPEG_PATH, "-i", filePath);
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains("Duration")) {
					duration = line.substring(line.indexOf("Duration:") + 10, line.indexOf(", start:")).trim();
					break;
				}
			}
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return duration.split("\\.")[0];
	}

	public static void getThumbnail(String thumbnailPath, String videoPath) {
		// Lệnh ffmpeg để trích xuất thumbnail từ video
		String[] command = { FFMPEG_PATH, // Lệnh ffmpeg
				"-i", videoPath, // Đầu vào video
				"-ss", "00:00:01", // Thời gian muốn lấy thumbnail (1 giây)
				"-vframes", "1", // Lấy 1 frame
				thumbnailPath // Đầu ra là file thumbnail
		};
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		try {
			// Bắt đầu quá trình
			Process process = processBuilder.start();
			// Đọc đầu ra của quá trình (nếu có)
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
				}
			}
			// Đọc lỗi nếu có
			try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
				String line;
				while ((line = errorReader.readLine()) != null) {
				}
			}
			// Chờ quá trình kết thúc
			process.waitFor();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void createDirIfNotExist(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	public static long convertDurationToSecond(String duration) {
		if (duration == null || duration.isEmpty()) {
			throw new IllegalArgumentException("Duration string cannot be null or empty");
		}

		String[] parts = duration.split(":");
		if (parts.length != 3) {
			throw new IllegalArgumentException("Invalid duration format. Expected format: hh:mm:ss");
		}

		try {
			int hours = Integer.parseInt(parts[0]);
			int minutes = Integer.parseInt(parts[1]);
			int seconds = Integer.parseInt(parts[2]);

			return hours * 3600L + minutes * 60L + seconds;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Duration contains invalid numbers: " + duration, e);
		}
	}

	public static void deleteDirectory(String fileName) {
		Path path = Paths.get(fileName);
		try {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void deleteFile(String path) {
		try {
			Files.delete(Paths.get(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
