package vip.jokor.im.model.stores_bean;

//@Entity
public class MRelation {
	//好友信息
	/**
	 * Id : 1
	 * BelongId : 1
	 * GroupId : 1
	 * GroupName : 好友
	 * GroupOrder : 1
	 * UserId : 2
	 * UserName : 请问
	 * UserIcon : dsac
	 * UserTel : 15002706059
	 * UserCId : 15002706059
	 */
//	@Id(assignable = true)
	private long id;
	private long BelongId;
	private long GroupId;
	private String GroupName;
	private int GroupOrder;
	private long UserId;
	private String UserName;
	private String UserIcon;
	private String UserTel;
	private long UserCId;
	
	public MRelation() {
	}
	
	public MRelation(long id, long belongId, long groupId, String groupName, int groupOrder, long userId, String userName, String userIcon, String userTel, long userCId) {
		this.id = id;
		BelongId = belongId;
		GroupId = groupId;
		GroupName = groupName;
		GroupOrder = groupOrder;
		UserId = userId;
		UserName = userName;
		UserIcon = userIcon;
		UserTel = userTel;
		UserCId = userCId;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getBelongId() {
		return BelongId;
	}
	
	public void setBelongId(long BelongId) {
		this.BelongId = BelongId;
	}
	
	public long getGroupId() {
		return GroupId;
	}
	
	public void setGroupId(long GroupId) {
		this.GroupId = GroupId;
	}
	
	public String getGroupName() {
		return GroupName;
	}
	
	public void setGroupName(String GroupName) {
		this.GroupName = GroupName;
	}
	
	public int getGroupOrder() {
		return GroupOrder;
	}
	
	public void setGroupOrder(int GroupOrder) {
		this.GroupOrder = GroupOrder;
	}
	
	public long getUserId() {
		return UserId;
	}
	
	public void setUserId(long UserId) {
		this.UserId = UserId;
	}
	
	public String getUserName() {
		return UserName;
	}
	
	public void setUserName(String UserName) {
		this.UserName = UserName;
	}
	
	public String getUserIcon() {
		return UserIcon;
	}
	
	public void setUserIcon(String UserIcon) {
		this.UserIcon = UserIcon;
	}
	
	public String getUserTel() {
		return UserTel;
	}
	
	public void setUserTel(String UserTel) {
		this.UserTel = UserTel;
	}
	
	public long getUserCId() {
		return UserCId;
	}
	
	public void setUserCId(long UserCId) {
		this.UserCId = UserCId;
	}
}
