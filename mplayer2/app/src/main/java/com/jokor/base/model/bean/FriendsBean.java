package com.jokor.base.model.bean;

//@Entity
public class FriendsBean {
    /**
     * Id : 1
     * CategoryId : 1
     * Sort : 1
     * Msg : 狗子
     * Belong : 1
     * Session : {"Id":1,"Name":"宋志文","UserName":"管理员账户","Icon":"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=739214653,532861615&fm=26&gp=0.jpg","Tel":"18588263531","Email":"78901214","IdCard":"95274","Pwd":"78901214","Sign":"哈哈哈","City":1,"Path":"2019-09-07T00:00:00+08:00","Vip":"2019-09-07T08:27:06+08:00","VipGrad":1,"Grad":1,"CreateTime":"2019-09-07T08:27:06+08:00","LoginTime":"2019-09-07T08:27:06+08:00","BanTime":"3019-09-07T08:27:06+08:00","Family":{"Id":1,"Manager":1,"Name":"宋志文","Icon":"/logo","Pwd":"78901214","Sign":"滚吧，屎球球","Grad":100,"CreateTime":"2019-09-07T00:00:00+08:00","LoginTime":"2019-09-07T16:48:33+08:00","private":true,"Members":null,"Relatives":null,"Devices":null,"Vistitors":null}}
     */
//    @Id(assignable = true)
    private long Id;
    private long CategoryId;
    private int Sort;
    private String Msg;
    private long Belong;
    private UserBean Friend;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(long categoryId) {
        CategoryId = categoryId;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public long getBelong() {
        return Belong;
    }

    public void setBelong(long belong) {
        Belong = belong;
    }

    public UserBean getFriend() {
        return Friend;
    }

    public void setFriend(UserBean friend) {
        Friend = friend;
    }

}
