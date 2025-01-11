package model.dao;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import controller.Util;
import model.bean.Video;
import model.dto.VideoDTO;

public class StudioDAO {
	private static final GenericDAO<Video> genericDAO = new GenericDAO<>(rs -> new Video(rs.getInt("video_id"),
			rs.getString("title"), rs.getString("description"), rs.getDate("created_at"), rs.getString("duration"),
			rs.getInt("user_id"), rs.getString("name"), rs.getString("img_extension"), rs.getBoolean("status"), rs.getString("video_extension")));

	public static int addVideo(String title, String description, int user_id, String imgExtension, String videoExtension) {
		String sql = "INSERT INTO video (title, description, created_at, id_user, img_extension, status, video_extension) VALUES (?, ?, ?, ?, ?, ?, ?)";
		return genericDAO.add(sql, title, description, LocalDate.now(), user_id, imgExtension, false, videoExtension);
	}

	public static void uploadDuration(String duration, int videoId) {
		String sql = "UPDATE video SET duration = ? WHERE id = ?";
		genericDAO.updateOrDelete(sql, duration, videoId);
	}

	public static ArrayList<Video> getVideos(Integer id) {
		String sql = "SELECT v.id AS video_id, title, description, v.created_at, duration, u.id AS user_id, name, img_extension, status, video_extension FROM video v JOIN user u ON v.id_user = u.id";
		if (id != null)
			sql += " WHERE u.id = ?";
		sql += " ORDER BY v.id DESC";
		return (ArrayList<Video>) genericDAO.list(sql, id);
	}

	public static void markDone(Integer id) {
		String sql = "UPDATE video SET status = 1 WHERE id = ?";
		genericDAO.updateOrDelete(sql, id);
	}

	public static Video getVideoById(Integer id) {
		String sql = "SELECT v.id AS video_id, title, description, v.created_at, duration, u.id AS user_id, name, img_extension, status, video_extension FROM video v JOIN user u ON v.id_user = u.id WHERE v.id = ?";
		return genericDAO.find(sql, id);
	}

	public static boolean deleteVideo(int id, String path) {
		Video vid = getVideoById(id);
		if (vid == null)
			return false;
		Util.deleteFile(path + "thumbnail" + File.separator + id + "." + vid.getImg_extension());
		Util.deleteFile(path + "video" + File.separator + id + "." + vid.getVideoExtension());
		Util.deleteDirectory(path + "video" + File.separator + id);
		String sql = "DELETE FROM video WHERE id = ?";
		genericDAO.updateOrDelete(sql, id);
		return true;
	}
}
