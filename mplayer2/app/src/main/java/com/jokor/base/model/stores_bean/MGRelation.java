package com.jokor.base.model.stores_bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

//@Entity
public class MGRelation {
	//加入的群聊
	/**
	 * Id : 1
	 * UserId : 29
	 * GroupId : 7
	 * GroupName : 测试数据
	 * GroupIcon : img_34groupIcon1552618751050
	 * GroupCId : 6668446810240974850
	 * GroupCount : 1
	 * IsAllow : true
	 */
//	@Id(assignable = true)
	private long Id;
	private long UserId;
	private long GroupId;
	private String GroupName;
	private String GroupIcon;
	private long GroupCId;
	private int GroupCount;
	private boolean IsAllow;
	
	public long getId() {
		return Id;
	}
	
	public void setId(long Id) {
		this.Id = Id;
	}
	
	public long getUserId() {
		return UserId;
	}
	
	public void setUserId(long UserId) {
		this.UserId = UserId;
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
	
	public String getGroupIcon() {
		return GroupIcon;
	}
	
	public void setGroupIcon(String GroupIcon) {
		this.GroupIcon = GroupIcon;
	}
	
	public long getGroupCId() {
		return GroupCId;
	}
	
	public void setGroupCId(long GroupCId) {
		this.GroupCId = GroupCId;
	}
	
	public int getGroupCount() {
		return GroupCount;
	}
	
	public void setGroupCount(int GroupCount) {
		this.GroupCount = GroupCount;
	}
	
	public boolean isIsAllow() {
		return IsAllow;
	}
	
	public void setIsAllow(boolean IsAllow) {
		this.IsAllow = IsAllow;
	}
}
