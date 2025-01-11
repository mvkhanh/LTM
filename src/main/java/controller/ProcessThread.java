package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.bo.StudioBO;

public class ProcessThread implements Runnable {
	private String storagePath;
	private String videoPath;
	private int userId;
	private int videoId;
	private long duration;
	private long start;
	public int progress = 0;
	public long remainingTime = 0;

	public static String[] quality = { "144p", "240p", "360p", "480p", "720p", "1080p" };
	public static String[] scale = { "256:144", "426:240", "640:360", "854:480", "1280:720", "1920:1080" };
	public static String[] bvideo = { "150k", "300k", "800k", "1500k", "2500k", "5000k" };
	public static String[] maxrate = { "175k", "350k", "1000k", "1800k", "3000k", "5500k" };
	public static String[] bufsize = { "350k", "700k", "2000k", "3600k", "6000k", "11000k" };
	public static String[] baudio = { "64k", "96k", "128k", "128k", "128k", "192k" };
	public static long logInterval = 10;

	public ProcessThread(String videoPath, int videoId, long duration, int userId) {
		this.duration = duration;
		this.videoPath = videoPath;
		this.videoId = videoId;
		this.userId = userId;
		this.start = System.currentTimeMillis();
		this.storagePath = UploadServlet.uploadVideoPath + File.separator + videoId + File.separator;
		Util.createDirIfNotExist(storagePath);
		for (int i = 0; i < quality.length; i++)
			Util.createDirIfNotExist(storagePath + quality[i]);
		ProgressServlet.processThreads.putIfAbsent(userId, new HashMap<>());
		ProgressServlet.processThreads.get(userId).put(videoId, this);
	}

	public void run() {
		// Tao cac phien ban co chat luong khac nhau, tao HLS cho moi chat luong
		String masterPlaylistPath = storagePath + "master_playlist.m3u8";
		String masterPlaylistContent = "#EXTM3U\n";

		String ffmpegCommand = Util.FFMPEG_PATH + " -i " + videoPath + " -preset fast -g 50 -sc_threshold 0 ";

		for (int i = 0; i < quality.length; i++) {
			String outputQualityDir = storagePath + quality[i] + File.separator;
			String outputSegmentPattern = outputQualityDir + "output%05d.ts";
			String outputPlaylistPath = outputQualityDir + "playlist.m3u8";

			// Xây dựng lệnh FFmpeg
			ffmpegCommand += "-vf scale=" + scale[i] + " -b:v " + bvideo[i] + " -maxrate " + maxrate[i] + " -bufsize "
					+ bufsize[i] + " -b:a " + baudio[i] + " -ac 2 -ar 44100 "
					+ "-f hls -hls_time 10 -hls_playlist_type vod " + "-hls_segment_filename " + outputSegmentPattern
					+ " " + outputPlaylistPath + " ";

			// Xây dựng nội dung Master Playlist
			masterPlaylistContent += "#EXT-X-STREAM-INF:BANDWIDTH=" + maxrate[i].replace("k", "000") + ",RESOLUTION="
					+ scale[i].replace(":", "x") + "\n" + quality[i] + "/playlist.m3u8\n";
		}

		// Thêm file master playlist vào lệnh
		ffmpegCommand += "-master_pl_name " + masterPlaylistPath;

		executeFFmpegCommand(ffmpegCommand);

		ProgressServlet.processThreads.get(userId).remove(videoId);
		try {
			Files.writeString(Paths.get(masterPlaylistPath), masterPlaylistContent);
		} catch (IOException e) {
			return;
		}
		StudioBO.markDone(videoId);
		ProgressServlet.done.putIfAbsent(userId, new ArrayList<>());
		ProgressServlet.done.get(userId).add(videoId);
		System.out.println("Create master playlist: " + masterPlaylistPath);
	}

	private void executeFFmpegCommand(String command) {
		try {
			// Sử dụng ProcessBuilder để gọi FFmpeg
			ProcessBuilder builder = new ProcessBuilder(command.split(" "));
			builder.redirectErrorStream(true);
			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			Pattern timePattern = Pattern.compile("time=(\\d{2}):(\\d{2}):(\\d{2})\\.(\\d{2})");

			while ((line = reader.readLine()) != null) {
				Matcher matcher = timePattern.matcher(line);
				if (matcher.find()) {
					// Lấy thời gian đã xử lý từ log
					int hours = Integer.parseInt(matcher.group(1));
					int minutes = Integer.parseInt(matcher.group(2));
					int seconds = Integer.parseInt(matcher.group(3));
					int centiseconds = Integer.parseInt(matcher.group(4));

					long processedTimeInSeconds = hours * 3600 + minutes * 60 + seconds;
					processedTimeInSeconds += centiseconds / 100.0;
					// Tính % tiến độ
					progress = Math.max((int) ((processedTimeInSeconds / (double) duration) * 100), 1);
					remainingTime = (System.currentTimeMillis() - start) / progress * (100 - progress) / 1000;
//	                System.out.println("Progress: " + progress + "%");
				}
			}
			process.waitFor(); // Chờ cho quá trình kết thúc
			System.out.println("FFmpeg command executed successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
