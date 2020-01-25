package com.jokor.base.model.stores_bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

//@Entity
public class Mmsg {
	//聊天消息
//	@Id(assignable = true)
	long id;
	int event;
	int relation;
	long group;
	long from;
	long to;
	String strMsg;
	
	public Mmsg() {
	}
	
	public Mmsg(int event, int relation, long group, long from, long to, String strMsg) {
		this.event = event;
		this.relation = relation;
		this.group = group;
		this.from = from;
		this.to = to;
		this.strMsg = strMsg;
	}
	
	public Mmsg(long id, int event, int relation, long group, long from, long to, String strMsg) {
		this.id = id;
		this.event = event;
		this.relation = relation;
		this.group = group;
		this.from = from;
		this.to = to;
		this.strMsg = strMsg;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public int getEvent() {
		return event;
	}
	
	public void setEvent(int event) {
		this.event = event;
	}
	
	public int getRelation() {
		return relation;
	}
	
	public void setRelation(int relation) {
		this.relation = relation;
	}
	
	public long getGroup() {
		return group;
	}
	
	public void setGroup(long group) {
		this.group = group;
	}
	
	public long getFrom() {
		return from;
	}
	
	public void setFrom(long from) {
		this.from = from;
	}
	
	public long getTo() {
		return to;
	}
	
	public void setTo(long to) {
		this.to = to;
	}
	
	public String getStrMsg() {
		return strMsg;
	}
	
	public void setStrMsg(String strMsg) {
		this.strMsg = strMsg;
	}
}
