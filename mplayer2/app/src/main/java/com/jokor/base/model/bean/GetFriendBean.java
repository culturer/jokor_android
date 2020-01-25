package com.jokor.base.model.bean;

import java.util.List;

public class GetFriendBean {

    /**
     * categorys : [{"Id":1,"UserId":1,"Name":"亲人"},{"Id":2,"UserId":1,"Name":"朋友"},{"Id":3,"UserId":1,"Name":"同事"},{"Id":4,"UserId":1,"Name":"同学"},{"Id":5,"UserId":1,"Name":"网友"}]
     * friends : [{"Id":1,"CategoryId":1,"Sort":1,"Msg":"狗子","Belong":1,"Session":{"Id":1,"Name":"宋志文","UserName":"管理员账户","Icon":"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=739214653,532861615&fm=26&gp=0.jpg","Tel":"18588263531","Email":"78901214","IdCard":"95274","Pwd":"78901214","Sign":"哈哈哈","City":1,"Path":"2019-09-07T00:00:00+08:00","Vip":"2019-09-07T08:27:06+08:00","VipGrad":1,"Grad":1,"CreateTime":"2019-09-07T08:27:06+08:00","LoginTime":"2019-09-07T08:27:06+08:00","BanTime":"3019-09-07T08:27:06+08:00","Family":{"Id":1,"Manager":1,"Name":"宋志文","Icon":"/logo","Pwd":"78901214","Sign":"滚吧，屎球球","Grad":100,"CreateTime":"2019-09-07T00:00:00+08:00","LoginTime":"2019-09-07T16:48:33+08:00","IsRead":true,"Members":null,"Relatives":null,"Devices":null,"Vistitors":null}}},{"Id":2,"CategoryId":1,"Sort":1,"Msg":"八戒","Belong":1,"Session":{"Id":1,"Name":"宋志文","UserName":"管理员账户","Icon":"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=739214653,532861615&fm=26&gp=0.jpg","Tel":"18588263531","Email":"78901214","IdCard":"95274","Pwd":"78901214","Sign":"哈哈哈","City":1,"Path":"2019-09-07T00:00:00+08:00","Vip":"2019-09-07T08:27:06+08:00","VipGrad":1,"Grad":1,"CreateTime":"2019-09-07T08:27:06+08:00","LoginTime":"2019-09-07T08:27:06+08:00","BanTime":"3019-09-07T08:27:06+08:00","Family":{"Id":1,"Manager":1,"Name":"宋志文","Icon":"/logo","Pwd":"78901214","Sign":"滚吧，屎球球","Grad":100,"CreateTime":"2019-09-07T00:00:00+08:00","LoginTime":"2019-09-07T16:48:33+08:00","IsRead":true,"Members":null,"Relatives":null,"Devices":null,"Vistitors":null}}},{"Id":3,"CategoryId":2,"Sort":1,"Msg":"而我","Belong":1,"Session":{"Id":1,"Name":"宋志文","UserName":"管理员账户","Icon":"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=739214653,532861615&fm=26&gp=0.jpg","Tel":"18588263531","Email":"78901214","IdCard":"95274","Pwd":"78901214","Sign":"哈哈哈","City":1,"Path":"2019-09-07T00:00:00+08:00","Vip":"2019-09-07T08:27:06+08:00","VipGrad":1,"Grad":1,"CreateTime":"2019-09-07T08:27:06+08:00","LoginTime":"2019-09-07T08:27:06+08:00","BanTime":"3019-09-07T08:27:06+08:00","Family":{"Id":1,"Manager":1,"Name":"宋志文","Icon":"/logo","Pwd":"78901214","Sign":"滚吧，屎球球","Grad":100,"CreateTime":"2019-09-07T00:00:00+08:00","LoginTime":"2019-09-07T16:48:33+08:00","IsRead":true,"Members":null,"Relatives":null,"Devices":null,"Vistitors":null}}},{"Id":4,"CategoryId":3,"Sort":1,"Msg":"去额外","Belong":1,"Session":{"Id":1,"Name":"宋志文","UserName":"管理员账户","Icon":"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=739214653,532861615&fm=26&gp=0.jpg","Tel":"18588263531","Email":"78901214","IdCard":"95274","Pwd":"78901214","Sign":"哈哈哈","City":1,"Path":"2019-09-07T00:00:00+08:00","Vip":"2019-09-07T08:27:06+08:00","VipGrad":1,"Grad":1,"CreateTime":"2019-09-07T08:27:06+08:00","LoginTime":"2019-09-07T08:27:06+08:00","BanTime":"3019-09-07T08:27:06+08:00","Family":{"Id":1,"Manager":1,"Name":"宋志文","Icon":"/logo","Pwd":"78901214","Sign":"滚吧，屎球球","Grad":100,"CreateTime":"2019-09-07T00:00:00+08:00","LoginTime":"2019-09-07T16:48:33+08:00","IsRead":true,"Members":null,"Relatives":null,"Devices":null,"Vistitors":null}}}]
     * status : 200
     * time : 2019-09-08 01:58:45
     */

    private int status;
    private String time;
    private List<CategorysBean> categorys;
    private List<FriendsBean> friends;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<CategorysBean> getCategorys() {
        return categorys;
    }

    public void setCategorys(List<CategorysBean> categorys) {
        this.categorys = categorys;
    }

    public List<FriendsBean> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendsBean> friends) {
        this.friends = friends;
    }

}
