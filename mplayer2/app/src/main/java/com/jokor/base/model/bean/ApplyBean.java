package com.jokor.base.model.bean;

import java.util.List;

public class ApplyBean {


    /**
     * applies : [{"Id":1,"Type":1,"Status":true,"UserId":1,"ToId":3,"Msg":"测试添加好友","FromUsername":"","ToUsername":"","FromIcon":"","ToIcon":"","FromCategoryId":0,"ToCategoryId":0,"CreateTime":"2019-09-21 12:05:04"},{"Id":2,"Type":1,"Status":false,"UserId":1,"ToId":5,"Msg":"啊哈哈哈","FromUsername":"","ToUsername":"","FromIcon":"","ToIcon":"","FromCategoryId":0,"ToCategoryId":0,"CreateTime":"2019-09-21 12:05:24"},{"Id":3,"Type":1,"Status":false,"UserId":1,"ToId":4,"Msg":"测试","FromUsername":"","ToUsername":"","FromIcon":"","ToIcon":"","FromCategoryId":0,"ToCategoryId":0,"CreateTime":"2019-09-21 12:05:37"},{"Id":4,"Type":1,"Status":true,"UserId":1,"ToId":6,"Msg":"9527","FromUsername":"","ToUsername":"","FromIcon":"","ToIcon":"","FromCategoryId":0,"ToCategoryId":0,"CreateTime":"2019-09-21 12:05:47"},{"Id":6,"Type":1,"Status":false,"UserId":1,"ToId":8,"Msg":"我是宋志文","FromUsername":"管理员账户","ToUsername":"","FromIcon":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1567921572660&di=fd7d5e447ccbf1d270962801e37c97ee&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201510%2F13%2F20151013234603_hiYnP.jpeg","ToIcon":"","FromCategoryId":1,"ToCategoryId":0,"CreateTime":"2019-10-18 00:51:35"}]
     * status : 200
     * time : 2019-10-18 09:50:28
     */

    private int status;
    private String time;
    private List<AppliesBean> applies;

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

    public List<AppliesBean> getApplies() {
        return applies;
    }

    public void setApplies(List<AppliesBean> applies) {
        this.applies = applies;
    }


}
