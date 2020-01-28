package vip.jokor.im.model.stores_bean;

public class Msg {

	long sessionId;
	int msg_type; // 0 --- 文本 , 1 --- 图片 ， 2 --- 语音 ， 3 --- 短视频
	String msg_content;
	String msg_username;
	String msg_usericon;
	long   msg_userId;
	long msg_time;
	
	public Msg() {
	}
	
	public Msg(long sessionId, int msg_type, String msg_content, String msg_username, String msg_usericon, long msg_userId, long msg_time) {
		this.sessionId = sessionId;
		this.msg_type = msg_type;
		this.msg_content = msg_content;
		this.msg_username = msg_username;
		this.msg_usericon = msg_usericon;
		this.msg_userId = msg_userId;
		this.msg_time = msg_time;
	}
	
	public long getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
	
	public int getMsg_type() {
		return msg_type;
	}
	
	public void setMsg_type(int msg_type) {
		this.msg_type = msg_type;
	}
	
	public String getMsg_content() {
		return msg_content;
	}
	
	public void setMsg_content(String msg_content) {
		this.msg_content = msg_content;
	}
	
	public String getMsg_username() {
		return msg_username;
	}
	
	public void setMsg_username(String msg_username) {
		this.msg_username = msg_username;
	}
	
	public String getMsg_usericon() {
		return msg_usericon;
	}
	
	public void setMsg_usericon(String msg_usericon) {
		this.msg_usericon = msg_usericon;
	}
	
	public long getMsg_userId() {
		return msg_userId;
	}
	
	public void setMsg_userId(long msg_userId) {
		this.msg_userId = msg_userId;
	}
	
	public long getMsg_time() {
		return msg_time;
	}
	
	public void setMsg_time(long msg_time) {
		this.msg_time = msg_time;
	}
	
	public static class AudioData{
		public float audio_time;
		public String audio_url;
		
		public AudioData(float audio_time, String audio_url) {
			this.audio_time = audio_time;
			this.audio_url = audio_url;
		}
	}
}
