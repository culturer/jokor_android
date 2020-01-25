package com.jokor.base.model.stores_bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

//@Entity
public class MGroup {
	//好友分组
	/**
	 * Id : 1
	 * UserId : 1
	 * Name : 好友
	 * Order : 1
	 */
//	@Id(assignable = true)
	private long id;
	private long UserId;
	private String Name;
	private int Order;
	
	public MGroup() {
	}
	
	public MGroup(long id, long userId, String name, int order) {
		this.id = id;
		UserId = userId;
		Name = name;
		Order = order;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getUserId() {
		return UserId;
	}
	
	public void setUserId(long UserId) {
		this.UserId = UserId;
	}
	
	public String getName() {
		return Name;
	}
	
	public void setName(String Name) {
		this.Name = Name;
	}
	
	public int getOrder() {
		return Order;
	}
	
	public void setOrder(int Order) {
		this.Order = Order;
	}
}
