package com.jokor.base.model.bean;

public class AppliesBean {

    public static final int  StatusSend = 0 ;
    public static final int  StatusAgree = 1 ;
    public static final int  StatusRefuse = 2 ;
    public static final int  StatusIgnore = 3 ;

    public static final int  TypeRelations = 0 ;
    public static final int  TypeFriends = 1 ;
    public static final int  TypeGroups = 2 ;
    public static final int  TypeHomes = 3 ;

    /**
     * Id : 1
     * Type : 1
     * Status : true
     * UserId : 1
     * ToId : 3
     * Msg : 测试添加好友
     * FromUsername :
     * ToUsername :
     * FromIcon :
     * ToIcon :
     * FromCategoryId : 0
     * ToCategoryId : 0
     * CreateTime : 2019-09-21 12:05:04
     */

    private long Id;
    private int Type;
    private int Status;
    private long UserId;
    private long ToId;
    private String Msg;
    private String FromUsername;
    private String ToUsername;
    private String FromIcon;
    private String ToIcon;
    private long FromCategoryId;
    private long ToCategoryId;
    private String CreateTime;

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long UserId) {
        this.UserId = UserId;
    }

    public long getToId() {
        return ToId;
    }

    public void setToId(long ToId) {
        this.ToId = ToId;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public String getFromUsername() {
        return FromUsername;
    }

    public void setFromUsername(String FromUsername) {
        this.FromUsername = FromUsername;
    }

    public String getToUsername() {
        return ToUsername;
    }

    public void setToUsername(String ToUsername) {
        this.ToUsername = ToUsername;
    }

    public String getFromIcon() {
        return FromIcon;
    }

    public void setFromIcon(String FromIcon) {
        this.FromIcon = FromIcon;
    }

    public String getToIcon() {
        return ToIcon;
    }

    public void setToIcon(String ToIcon) {
        this.ToIcon = ToIcon;
    }

    public long getFromCategoryId() {
        return FromCategoryId;
    }

    public void setFromCategoryId(long FromCategoryId) {
        this.FromCategoryId = FromCategoryId;
    }

    public long getToCategoryId() {
        return ToCategoryId;
    }

    public void setToCategoryId(long ToCategoryId) {
        this.ToCategoryId = ToCategoryId;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}