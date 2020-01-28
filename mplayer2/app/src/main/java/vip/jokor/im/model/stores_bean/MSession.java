package vip.jokor.im.model.stores_bean;

//@Entity
public class MSession {
	//会话信息
//	@Id
	long id;
	boolean sessionType;        //false --- 单聊 ， true --- 群聊
	long belongsUser;
	long userId;
	String userIcon;
	long sessionTime;
	String sessionMsg;
	int sessionCount;
	String name;
	long cId;
	
	public MSession() { }
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public boolean isSessionType() {
		return sessionType;
	}
	
	public void setSessionType(boolean sessionType) {
		this.sessionType = sessionType;
	}
	
	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public long getSessionTime() {
		return sessionTime;
	}
	
	public void setSessionTime(long sessionTime) {
		this.sessionTime = sessionTime;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getcId() {
		return cId;
	}
	
	public void setcId(long cId) {
		this.cId = cId;
	}
	
	public long getBelongsUser() {
		return belongsUser;
	}
	
	public void setBelongsUser(long belongsUser) {
		this.belongsUser = belongsUser;
	}
	
	public String getSessionMsg() {
		return sessionMsg;
	}
	
	public void setSessionMsg(String sessionMsg) {
		this.sessionMsg = sessionMsg;
	}
	
	public int getSessionCount() {
		return sessionCount;
	}
	
	public void setSessionCount(int sessionCount) {
		this.sessionCount = sessionCount;
	}
	
	public String getUserIcon() {
		return userIcon;
	}
	
	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}
	
}
