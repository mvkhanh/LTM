package model.bo;

import java.util.ArrayList;

import model.bean.Video;
import model.dao.StudioDAO;
import model.dto.VideoDTO;

public class StudioBO {
	public static int addVideo(String title, String description, int user_id, String imgExtension, String videoExtension) {
		return StudioDAO.addVideo(title, description, user_id, imgExtension, videoExtension);
	}

	public static ArrayList<VideoDTO> getVideos(Integer id) {
		ArrayList<VideoDTO> dtos = new ArrayList<>();
		ArrayList<Video> entities = StudioDAO.getVideos(id);
		for(Video entity : entities) {
			dtos.add(convertEntityToDTO(entity));
		}
		return dtos;
	}

	public static void markDone(Integer id) {
		StudioDAO.markDone(id);
	}

	public static Video getVideoById(Integer id) {
		return StudioDAO.getVideoById(id);
	}

	public static VideoDTO convertEntityToDTO(Video entity) {
		return entity != null
				? new VideoDTO(entity.getId(), entity.getTitle(), entity.getDuration(), entity.getName(),
						entity.getCreated_at(), entity.getImg_extension(), entity.isStatus(), entity.getVideoExtension(), entity.getId_user())
				: null;
	}

	public static boolean deleteVideo(int id, String path) {
		return StudioDAO.deleteVideo(id, path);
	}
}
