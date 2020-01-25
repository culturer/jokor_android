package com.jokor.base.model.stores_bean;

public class MMGroup {
	//群聊
	/**
	 * id : 3
	 * UserId : 29
	 * Icon : img_29groupIcon1552616428051
	 * Name : 测试群！
	 * Count : 1
	 * MaxCount : 100
	 * Msg : 测试介绍
	 * CId : 6668436802967175170
	 * IsVIP : false
	 * VIPStart : 0
	 * VIPEnd : 0
	 * IsHome : false
	 * HomeUrl :
	 * IsShop : false
	 * ShopId : 0
	 * IsChange : false
	 */
	private long id;
	private long UserId;
	private String Icon;
	private String Name;
	private int Count;
	private int MaxCount;
	private String Msg;
	private long CId;
	private boolean IsVIP;
	private long VIPStart;
	private long VIPEnd;
	private boolean IsHome;
	private String HomeUrl;
	private boolean IsShop;
	private long ShopId;
	private boolean IsChange;
	
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
	
	public String getIcon() {
		return Icon;
	}
	
	public void setIcon(String Icon) {
		this.Icon = Icon;
	}
	
	public String getName() {
		return Name;
	}
	
	public void setName(String Name) {
		this.Name = Name;
	}
	
	public int getCount() {
		return Count;
	}
	
	public void setCount(int Count) {
		this.Count = Count;
	}
	
	public int getMaxCount() {
		return MaxCount;
	}
	
	public void setMaxCount(int MaxCount) {
		this.MaxCount = MaxCount;
	}
	
	public String getMsg() {
		return Msg;
	}
	
	public void setMsg(String Msg) {
		this.Msg = Msg;
	}
	
	public long getCId() {
		return CId;
	}
	
	public void setCId(long CId) {
		this.CId = CId;
	}
	
	public boolean isIsVIP() {
		return IsVIP;
	}
	
	public void setIsVIP(boolean IsVIP) {
		this.IsVIP = IsVIP;
	}
	
	public long getVIPStart() {
		return VIPStart;
	}
	
	public void setVIPStart(long VIPStart) {
		this.VIPStart = VIPStart;
	}
	
	public long getVIPEnd() {
		return VIPEnd;
	}
	
	public void setVIPEnd(long VIPEnd) {
		this.VIPEnd = VIPEnd;
	}
	
	public boolean isIsHome() {
		return IsHome;
	}
	
	public void setIsHome(boolean IsHome) {
		this.IsHome = IsHome;
	}
	
	public String getHomeUrl() {
		return HomeUrl;
	}
	
	public void setHomeUrl(String HomeUrl) {
		this.HomeUrl = HomeUrl;
	}
	
	public boolean isIsShop() {
		return IsShop;
	}
	
	public void setIsShop(boolean IsShop) {
		this.IsShop = IsShop;
	}
	
	public long getShopId() {
		return ShopId;
	}
	
	public void setShopId(long ShopId) {
		this.ShopId = ShopId;
	}
	
	public boolean isIsChange() {
		return IsChange;
	}
	
	public void setIsChange(boolean IsChange) {
		this.IsChange = IsChange;
	}
}
