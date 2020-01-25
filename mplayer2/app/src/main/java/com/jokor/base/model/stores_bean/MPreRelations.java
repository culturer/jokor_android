package com.jokor.base.model.stores_bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

//@Entity
public class MPreRelations {
	//已发送好友申请
	public static final int CONFIRM_TYPE_SEND = 0;
	//等待验证好友申请
	public static final int CONFIRM_TYPE_WAITE = 1;
	//已成功添加好友
	public static final int CONFIRM_TYPE_SUCCESS = 2;
	//已拒绝添加好友
	public static final int CONFIRM_TYPE_REFUSED = 3;
	
	//好友申请
//	@Id(assignable = true)
	private long id;
	private int confirm_type;
	private String msg; //验证消息
	private boolean isGroup;
	
	private long userId;
	private long userCId;
	private String icon;
	private String tel;
	private String userName;
	private long belong;
	private long time;
	
	public MPreRelations() { }
	
	public MPreRelations(boolean isGroup,long id, int confirm_type, String msg, long userId, long userCId, String icon, String tel, String userName, long belong, long time) {
		this.isGroup = isGroup;
		this.id = id;
		this.confirm_type = confirm_type;
		this.msg = msg;
		this.userId = userId;
		this.userCId = userCId;
		this.icon = icon;
		this.tel = tel;
		this.userName = userName;
		this.belong = belong;
		this.time = time;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public int getConfirm_type() {
		return confirm_type;
	}
	
	public void setConfirm_type(int confirm_type) {
		this.confirm_type = confirm_type;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public long getUserCId() {
		return userCId;
	}
	
	public void setUserCId(long userCId) {
		this.userCId = userCId;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public String getTel() {
		return tel;
	}
	
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public long getBelong() {
		return belong;
	}
	
	public void setBelong(long belong) {
		this.belong = belong;
	}
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public boolean isGroup() {
		return isGroup;
	}
	
	public void setGroup(boolean group) {
		isGroup = group;
	}
}
