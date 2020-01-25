package com.jokor.base.model.bean;

public class GroupBean {

    /**
     * Id : 10
     * Icon : http://img4.imgtn.bdimg.com/it/u=483218887,1570250175&fm=26&gp=0.jpg
     * Name : 测试1群
     * Sign : 交友群
     * Msg :
     * Pwd :
     * City : 0
     * IsCity : false
     * Administers :
     * Belong : 1
     * MemCount : 0
     * MaxCount : 100
     * IsPay : false
     * PayNum : 0
     * Pri : 0
     * Grad : 0
     * CreateTime : 2019-11-14 14:21:43
     */

    private long Id;
    private String Icon;
    private String Name;
    private String Sign;
    private String Msg;
    private String Pwd;
    private int City;
    private boolean IsCity;
    private String Administers;
    private long Belong;
    private int MemCount;
    private int MaxCount;
    private boolean IsPay;
    private int PayNum;
    private int Pri;
    private int Grad;
    private String CreateTime;

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
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

    public String getSign() {
        return Sign;
    }

    public void setSign(String Sign) {
        this.Sign = Sign;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public String getPwd() {
        return Pwd;
    }

    public void setPwd(String Pwd) {
        this.Pwd = Pwd;
    }

    public int getCity() {
        return City;
    }

    public void setCity(int City) {
        this.City = City;
    }

    public boolean isIsCity() {
        return IsCity;
    }

    public void setIsCity(boolean IsCity) {
        this.IsCity = IsCity;
    }

    public String getAdministers() {
        return Administers;
    }

    public void setAdministers(String Administers) {
        this.Administers = Administers;
    }

    public long getBelong() {
        return Belong;
    }

    public void setBelong(long Belong) {
        this.Belong = Belong;
    }

    public int getMemCount() {
        return MemCount;
    }

    public void setMemCount(int MemCount) {
        this.MemCount = MemCount;
    }

    public int getMaxCount() {
        return MaxCount;
    }

    public void setMaxCount(int MaxCount) {
        this.MaxCount = MaxCount;
    }

    public boolean isIsPay() {
        return IsPay;
    }

    public void setIsPay(boolean IsPay) {
        this.IsPay = IsPay;
    }

    public int getPayNum() {
        return PayNum;
    }

    public void setPayNum(int PayNum) {
        this.PayNum = PayNum;
    }

    public int getPri() {
        return Pri;
    }

    public void setPri(int Pri) {
        this.Pri = Pri;
    }

    public int getGrad() {
        return Grad;
    }

    public void setGrad(int Grad) {
        this.Grad = Grad;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}
