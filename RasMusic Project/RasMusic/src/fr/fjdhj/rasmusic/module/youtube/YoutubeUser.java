package fr.fjdhj.rasmusic.module.youtube;

public class YoutubeUser {
	
	private String id;
	private String password;
	
	public YoutubeUser(){
		setId("");
		password = "";
	}
	public YoutubeUser(String id, String password){
		this.setId(id);
		this.password = password;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
