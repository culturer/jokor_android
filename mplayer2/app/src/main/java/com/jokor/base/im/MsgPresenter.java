package com.jokor.base.im;

import android.util.Log;

import com.google.gson.Gson;
import com.jokor.base.base.Datas;
import com.jokor.base.model.bean.AppliesBean;
import com.jokor.base.model.bean.GroupBean;
import com.jokor.base.model.db.DBManager;
import com.jokor.base.model.db.Msg;
import com.jokor.base.model.db.Msg_;
import com.jokor.base.model.db.Session;
import com.jokor.base.model.db.Session_;
import com.jokor.base.pages.main.main_page.friends.ApplyEvent;
import com.jokor.base.pages.main.main_page.group.GroupPresenter;
import com.jokor.base.util.base.GsonUtil;
import com.jokor.base.util.base.TimeUtil;

import org.greenrobot.eventbus.EventBus;

import io.objectbox.Box;

import static com.jokor.base.model.db.Msg.MSG_FROM_FRIEND;
import static com.jokor.base.model.db.Msg.MSG_FROM_GROUP;
import static com.jokor.base.model.db.Msg.MSG_TYPE_AUDIO;
import static com.jokor.base.model.db.Msg.MSG_TYPE_IMG;
import static com.jokor.base.model.db.Msg.MSG_TYPE_TEXT;
import static com.jokor.base.model.db.Msg.MSG_TYPE_VIDEO;
import static com.jokor.base.model.db.Msg.MSG_USED_ADD_FRIEND;
import static com.jokor.base.model.db.Msg.MSG_USED_CHAT;

public class MsgPresenter {

    private static String TAG = "MsgPresenter" ;

    private static final MsgPresenter ourInstance = new MsgPresenter();

    public static MsgPresenter getInstance() {
        return ourInstance;
    }

    private MsgPresenter() { }

    public void handleMsg(MsgEvent event){
        Msg msg = event.getMsg();
        //如果是自己发送消息就把消息发送出去
        if (msg.getFromId() == Datas.getUserInfo().getId()){
            Gson gson = GsonUtil.getGson();
            String strMsg = gson.toJson(msg);
            Log.e(TAG, "handleMsg: 发送消息" +strMsg);
            switch (msg.getMsgFrom()){
                case Msg.MSG_FROM_FRIEND :MsgService.publish(strMsg,"u_"+msg.getToId(),2);break;
                case Msg.MSG_FROM_GROUP:MsgService.publish(strMsg,"g_"+msg.getToId(),2);break;
            }
        }
        Log.e(TAG, "handleMsg: 聊天消息保存到数据库 "+GsonUtil.getGson().toJson(msg));
        msg.setId(0);
        DBManager.getInstance().getMsgBox().put(msg);
        //处理聊天会话
        switch (msg.getMsgFrom()){
            case MSG_FROM_FRIEND:
                handleUserMsg(event.getMsg());
                break;
            case MSG_FROM_GROUP:
                handleGroupMsg(msg);
                break;
        }
    }

    private void handleUserMsg(Msg msg){
        //更新会话信息
        Box<Session> sessionBox = DBManager.getInstance().getSessionBox();
        Session session;
        session = sessionBox.query().equal(Session_.Belong,Datas.getUserInfo().getId()).equal(Session_.msgFrom,msg.getMsgFrom()).equal(Session_.toId,msg.getFromId()).or().equal(Session_.toId,msg.getToId()).build().findFirst();
        if (session==null) {
            Log.i(TAG, "update: 会话不存在,开始新建会话");
            session = new Session();
            session.setMsgFrom(msg.getMsgFrom());
            session.setBelong(Datas.getUserInfo().getId());
            session.setTmpTime(msg.getCreateTime());
            long userId = msg.getToId();
            if (userId == Datas.getUserInfo().getId()) userId = msg.getFromId();
            session.setId(userId);
            session.setToId(userId);
        }
        switch (msg.getMsgType()){
            case MSG_TYPE_TEXT:session.setTmpMsg(msg.getData());break;
            case MSG_TYPE_IMG:session.setTmpMsg("[图片]");break;
            case MSG_TYPE_AUDIO:session.setTmpMsg("[语音]");break;
            case MSG_TYPE_VIDEO:session.setTmpMsg("[视频]");break;
        }
        session.setTmpTime(msg.getCreateTime());
        if (msg.getFromId() == Datas.getUserInfo().getId()){
            //自己发的消息就不要设置未读消息了
            session.setTmpMsgCount(0);
        }else {
            if (session.getTmpMsgCount()<=0){ session.setTmpMsgCount(1); }
            else if (session.getTmpMsgCount()>=99){session.setTmpMsgCount(99);}
            else { session.setTmpMsgCount(session.getTmpMsgCount()+1);}
        }
        //存储session状态
        DBManager.getInstance().getSessionBox().put(session);
    }

    private void handleGroupMsg(Msg msg){
        //更新会话信息
        Box<Session> sessionBox = DBManager.getInstance().getSessionBox();
        Session session = null;
        session = sessionBox.query().equal(Session_.Belong,Datas.getUserInfo().getId()).equal(Session_.msgFrom,msg.getMsgFrom()).equal(Session_.toId,msg.getToId()).build().findFirst();
    }

    //接收到消息以后将消息打包成Event发送出去
    public void receiveMsg(String topic,String strMsg){
        //发给自己的单聊消息
        if (topic.equals(MsgConfig.topic) || topic.startsWith("g_")){
            Log.i(TAG, "receiveMsg: 接收到需要处理的消息！ "+strMsg);
            Gson gson = GsonUtil.getGson();
            Log.i(TAG, "receiveMsg: Gson初始化");
            Msg msg = gson.fromJson(strMsg,Msg.class);
            Log.i(TAG, "receiveMsg: 消息序列化"+gson.toJson(msg));
            switch (msg.getMsgUsed()){
                case MSG_USED_CHAT:handleChatMsg(msg);break;
                case MSG_USED_ADD_FRIEND:handleAddFriend(msg);break;
            }
        }
    }

    private void handleChatMsg(Msg msg){
        MsgEvent msgEvent = new MsgEvent(TAG,msg);
        Log.i(TAG, "receiveMsg: 对消息进行封装！"+GsonUtil.getGson().toJson(msgEvent));
        //消息去重
        Msg msg1 = DBManager.getInstance().getMsgBox().query().equal(Msg_.msgId,msg.getMsgId()).build().findFirst();
        //数据库里面已经存有该消息
        if ( msg1 != null ){
            Log.e(TAG, "handleMsg: 数据库已经存在该消息 "+GsonUtil.getGson().toJson(msg) );
        }else {
            EventBus.getDefault().postSticky(msgEvent);
        }
    }

    private void handleAddFriend(Msg msg){
        AppliesBean appliesBean = new AppliesBean();
        appliesBean.setType(msg.getMsgType());
        appliesBean.setStatus(msg.getStatus());
        appliesBean.setUserId(msg.getFromId());
        appliesBean.setToId(msg.getToId());
        appliesBean.setMsg(msg.getData());
        appliesBean.setCreateTime(TimeUtil.date2Str(msg.getCreateTime()));
        appliesBean.setFromUsername(msg.getUsername());
        appliesBean.setFromIcon(msg.getIcon());
        EventBus.getDefault().post(new ApplyEvent(appliesBean));
    }


}
