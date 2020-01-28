package vip.jokor.im.model.bean;

//@Entity
public class UserBean {

    /**
     * Id : 1
     * Name : 宋志文
     * UserName : 管理员账户
     * Icon : /logo
     * Tel : 18588263531
     * Email : 78901214
     * IdCard : 95274
     * Pwd : 78901214
     * Sign : 哈哈哈
     * City : 1
     * Path : 2019-09-07T00:00:00+08:00
     * Vip : 2019-09-07T08:27:06+08:00
     * VipGrad : 1
     * Grad : 1
     * CreateTime : 2019-09-07T08:27:06+08:00
     * LoginTime : 2019-09-07T08:27:06+08:00
     * BanTime : 3019-09-07T08:27:06+08:00
     * Family : {"Id":1,"Manager":1,"Name":"宋志文","Icon":"/logo","Pwd":"78901214","Sign":"滚吧，屎球球","Grad":100,"CreateTime":"2019-09-07T00:00:00+08:00","LoginTime":"2019-09-07T16:48:33+08:00","IsRead":true}
     */
//    @Id(assignable = true)
    private long Id;
    private String Name;
    private String UserName;
    private String Icon;
    private String Tel;
    private String Email;
    private String IdCard;
    private boolean Sex;
    private String Pwd;
    private String Sign;
    private int City;
    private String Path;
    private String Vip;
    private int VipGrad;
    private int Grad;
    private String CreateTime;
    private String LoginTime;
    private String BanTime;
    private FamilyBean Family;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getIdCard() {
        return IdCard;
    }

    public void setIdCard(String idCard) {
        IdCard = idCard;
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

    public int getCity() {
        return City;
    }

    public void setCity(int city) {
        City = city;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getVip() {
        return Vip;
    }

    public void setVip(String vip) {
        Vip = vip;
    }

    public int getVipGrad() {
        return VipGrad;
    }

    public void setVipGrad(int vipGrad) {
        VipGrad = vipGrad;
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

    public String getBanTime() {
        return BanTime;
    }

    public void setBanTime(String banTime) {
        BanTime = banTime;
    }

    public FamilyBean getFamily() {
        return Family;
    }

    public void setFamily(FamilyBean family) {
        Family = family;
    }

    public boolean isSex() {
        return Sex;
    }

    public void setSex(boolean sex) {
        Sex = sex;
    }
}
