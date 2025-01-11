package model.dto;

import java.sql.Date;

public class VideoDTO {
	private int id;
	private String title;
	private String duration;
	private String authorName;
	private Date uploadDate;
	private String imgExtension;
	private String videoExtension;
	private boolean isPublic;
	private int userId;

	public VideoDTO(int id, String title, String duration, String authorName, Date uploadDate, String imgExtension, boolean isPublic, String videoExtension, int userId) {
		super();
		this.id = id;
		this.title = title;
		this.duration = duration;
		this.authorName = authorName;
		this.uploadDate = uploadDate;
		this.imgExtension = imgExtension;
		this.isPublic = isPublic;
		this.videoExtension = videoExtension;
		this.userId = userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getImgExtension() {
		return imgExtension;
	}

	public void setImgExtension(String imgExtension) {
		this.imgExtension = imgExtension;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public String getVideoExtension() {
		return videoExtension;
	}

	public void setVideoExtension(String videoExtension) {
		this.videoExtension = videoExtension;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
