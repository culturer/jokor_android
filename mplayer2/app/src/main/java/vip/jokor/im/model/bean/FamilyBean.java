package vip.jokor.im.model.bean;


//@Entity
public class FamilyBean {
    /**
     * Id : 1
     * Manager : 1
     * Name : 宋志文
     * Icon : /logo
     * Pwd : 78901214
     * Sign : 滚吧，屎球球
     * Grad : 100
     * CreateTime : 2019-09-07T00:00:00+08:00
     * LoginTime : 2019-09-07T16:48:33+08:00
     * IsRead : true
     */
//    @Id(assignable = true)
    private long Id;
    private long Manager;
    private String Name;
    private String Icon;
    private String Pwd;
    private String Sign;
    private int Grad;
    private String CreateTime;
    private String LoginTime;
    private boolean IsRead;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getManager() {
        return Manager;
    }

    public void setManager(long manager) {
        Manager = manager;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getPwd() {
        return Pwd;
    }

    public void setPwd(String pwd) {
        Pwd = pwd;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public int getGrad() {
        return Grad;
    }

    public void setGrad(int grad) {
        Grad = grad;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getLoginTime() {
        return LoginTime;
    }

    public void setLoginTime(String loginTime) {
        LoginTime = loginTime;
    }

    public boolean isRead() {
        return IsRead;
    }

    public void setRead(boolean read) {
        IsRead = read;
    }
}
