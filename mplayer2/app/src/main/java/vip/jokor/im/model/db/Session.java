package vip.jokor.im.model.db;

import vip.jokor.im.base.Datas;
import vip.jokor.im.model.bean.FriendsBean;
import vip.jokor.im.model.bean.GroupBean;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import vip.jokor.im.model.bean.GroupListBean;

import static vip.jokor.im.model.db.Msg.MSG_FROM_FRIEND;
import static vip.jokor.im.model.db.Msg.MSG_FROM_GROUP;

@Entity
public class Session {

    @Id(assignable = true)
    private long Id;            //编号
    private int Sort;           //排序
    private long Belong;        //用户编号
    private long toId;          //用户编号
    private String UserName;
    private String Icon;
    private int VipGrad;
    private int Grad;

    private int msgFrom ;      //会话类型

    //聊天消息信息
    private String tmpMsg;      //临时聊天消息 (哈哈哈，今天...)
    private int tmpMsgCount;    // 99
    private Date tmpTime;     //2019.09.13


    public void  setDataFromFriendsBean(FriendsBean friendsBean){
        this.msgFrom = MSG_FROM_FRIEND;
        this.toId = friendsBean.getFriend().getId();
        this.Id = friendsBean.getFriend().getId();
        this.Sort = friendsBean.getSort();
        this.Belong = friendsBean.getBelong();
        if (friendsBean.getMsg()==null && friendsBean.getMsg().equals("")){
            this.UserName = friendsBean.getMsg();
        }else {
            this.UserName = friendsBean.getFriend().getUserName();
        }
        this.Icon = friendsBean.getFriend().getIcon();
        this.VipGrad = friendsBean.getFriend().getVipGrad();
        this.Grad = friendsBean.getFriend().getGrad();
    }

    public void setDataFromGroupBean(GroupListBean.GroupsBean groupBean){
        this.msgFrom = MSG_FROM_GROUP;
        this.toId = groupBean.getId();
        this.Id = groupBean.getId();
        this.Sort = 0;
        this.Belong = Datas.getUserInfo().getId();
        if (groupBean.getMsg()!=null && !groupBean.getMsg().equals("")){
            this.UserName = groupBean.getGroup().getName();
        }else {
            this.UserName = groupBean.getMsg();
        }
        this.Icon = groupBean.getGroup().getIcon();
        this.Grad = groupBean.getGroup().getGrad();
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }


    public long getBelong() {
        return Belong;
    }

    public void setBelong(long belong) {
        Belong = belong;
    }

    public String getTmpMsg() {
        return tmpMsg;
    }

    public void setTmpMsg(String tmpMsg) {
        this.tmpMsg = tmpMsg;
    }

    public int getTmpMsgCount() {
        return tmpMsgCount;
    }

    public void setTmpMsgCount(int tmpMsgCount) {
        this.tmpMsgCount = tmpMsgCount;
    }

    public Date getTmpTime() {
        return tmpTime;
    }

    public void setTmpTime(Date tmpTime) {
        this.tmpTime = tmpTime;
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

    public int getMsgFrom() {
        return msgFrom;
    }

    public void setMsgFrom(int msgFrom) {
        this.msgFrom = msgFrom;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }
}
