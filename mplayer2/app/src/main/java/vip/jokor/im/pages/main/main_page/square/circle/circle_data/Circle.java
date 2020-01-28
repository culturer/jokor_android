package vip.jokor.im.pages.main.main_page.square.circle.circle_data;

public class Circle {

    /**
     * Id : 1
     * Name : 悠悠球俱乐部
     * Icon : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1577202090314&di=ca1519eedb57205779d848821386c45d&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201412%2F13%2F20141213220212_rLVdL.jpeg
     * Msg : 会玩悠悠球的小猪崽子
     * ShopId : 0
     * Belong1 : 1
     * Belong2 : 2
     * CreateTime : 2019-12-24T20:53:57+08:00
     */

    private long Id;
    private String Name;
    private String Icon;
    private String Msg;
    private long ShopId;
    private long Belong1;
    private long Belong2;
    private String CreateTime;

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public long getShopId() {
        return ShopId;
    }

    public void setShopId(long ShopId) {
        this.ShopId = ShopId;
    }

    public long getBelong1() {
        return Belong1;
    }

    public void setBelong1(long Belong1) {
        this.Belong1 = Belong1;
    }

    public long getBelong2() {
        return Belong2;
    }

    public void setBelong2(long Belong2) {
        this.Belong2 = Belong2;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}
