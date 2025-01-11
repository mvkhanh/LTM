package model.bean;

import java.sql.Date;
import java.sql.Time;

public class Video {
	private int id;
	private String title;
	private String description;
	private Date created_at;
	private String duration;
	private int id_user;
	private String name;
	private String img_extension;
	private boolean status;
	private String videoExtension;

	public Video(int id, String title, String description, Date created_at, String duration, int id_user, String name, String img_extension, boolean status, String videoExtension) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.created_at = created_at;
		this.duration = duration;
		this.id_user = id_user;
		this.name = name;
		this.status = status;
		this.img_extension = img_extension;
		this.videoExtension = videoExtension;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getImg_extension() {
		return img_extension;
	}

	public void setImg_extension(String img_extension) {
		this.img_extension = img_extension;
	}

	public String getVideoExtension() {
		return videoExtension;
	}

	public void setVideoExtension(String videoExtension) {
		this.videoExtension = videoExtension;
	}
	
}
