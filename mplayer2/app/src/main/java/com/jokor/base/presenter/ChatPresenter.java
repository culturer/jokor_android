package com.jokor.base.presenter;

import android.util.Log;

import com.jokor.base.base.Datas;
import com.jokor.base.base.Urls;
import com.jokor.base.im.MsgEvent;
import com.jokor.base.model.db.DBManager;
import com.jokor.base.model.db.Msg_;
import com.jokor.base.model.db.Session;
import com.jokor.base.model.db.Msg;
import com.jokor.base.model.db.Session_;
import com.jokor.base.util.base.QiNiuUtil;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class ChatPresenter {

    private static final String TAG = "ChatPresenter" ;

    private static final ChatPresenter ourInstance = new ChatPresenter();

    public static ChatPresenter getInstance() {
        return ourInstance;
    }

    private ChatPresenter() { }

    public List<Msg> getMsgs(long userId,int msgFrom){
        return DBManager.getInstance().getMsgBox().query().equal(Msg_.msgFrom,msgFrom).equal(Msg_.fromId,userId).or().equal(Msg_.toId,userId).order(Msg_.id).build().find();
    }

    public Msg senTextdMsg(Session session, String strMsg){
        Msg msg = new Msg();
        msg.setMsgFrom(session.getMsgFrom());
        msg.setData(strMsg);
        msg.setMsgUsed(Msg.MSG_USED_CHAT);
        msg.setFromId(Datas.getUserInfo().getId());
        msg.setToId(session.getToId());
        msg.setIcon(Datas.getUserInfo().getIcon());
        msg.setMsgType(Msg.MSG_TYPE_TEXT);
        msg.setMsgId("u"+Datas.getUserInfo().getId()+"_"+System.currentTimeMillis());
        msg.setUsername(Datas.getUserInfo().getUserName());
        msg.setCreateTime(new Date());
        Log.i(TAG, "senTextdMsg: FromId [ "+Datas.getUserInfo().getId()+" ] ToId"+" [ "+session.getToId()+" ] ");
        EventBus.getDefault().postSticky(new MsgEvent(TAG+".senTextdMsg",msg));
        return null;
    }

    public void sendImgMsg(Session session,String imgPath){
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Log.i(TAG, "onSuccess: "+t);
                try {
                    JSONObject jb = new JSONObject(t);
                    int status = jb.getInt("status");
                    if (status == 200){
                        String token = jb.getString("token");
                        UpCompletionHandler handler1 = new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                Log.i(TAG, "complete: key "+key);
                                Log.i(TAG, "complete: info "+info);
                                Log.i(TAG, "complete: response "+response);
                                if (info.isOK()){
                                    try {
                                        String filename = response.getString("key");
                                        Msg msg = new Msg();
                                        msg.setData(Urls.QINIU_URL+filename);
                                        msg.setFromId(Datas.getUserInfo().getId());
                                        msg.setToId(session.getToId());
                                        msg.setIcon(Datas.getUserInfo().getIcon());
                                        msg.setMsgFrom(session.getMsgFrom());
                                        msg.setMsgType(Msg.MSG_TYPE_IMG);
                                        msg.setMsgId("u"+Datas.getUserInfo().getId()+"_"+System.currentTimeMillis());
                                        msg.setUsername(Datas.getUserInfo().getUserName());
                                        msg.setCreateTime(new Date());
                                        EventBus.getDefault().postSticky(new MsgEvent(TAG+".sendImgMsg",msg));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                        QiNiuUtil.getInstance().upload(token,imgPath,null,handler1,null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                Log.i(TAG, "onFailure: "+error);
            }
        };
        QiNiuUtil.getInstance().getToken(callback);
    }

    public void sendAudioMsg(Session session,String filePath){
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Log.i(TAG, "onSuccess: "+t);
                try {
                    JSONObject jb = new JSONObject(t);
                    int status = jb.getInt("status");
                    if (status == 200){
                        String token = jb.getString("token");
                        UpCompletionHandler handler1 = new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                Log.i(TAG, "complete: key "+key);
                                Log.i(TAG, "complete: info "+info);
                                Log.i(TAG, "complete: response "+response);
                                if (info.isOK()){
                                    try {
                                        String filename = response.getString("key");
                                        Msg msg = new Msg();
                                        msg.setData(Urls.QINIU_URL+filename);
                                        msg.setFromId(Datas.getUserInfo().getId());
                                        msg.setToId(session.getToId());
                                        msg.setIcon(Datas.getUserInfo().getIcon());
                                        msg.setMsgFrom(session.getMsgFrom());
                                        msg.setMsgType(Msg.MSG_TYPE_AUDIO);
                                        msg.setMsgId("u"+Datas.getUserInfo().getId()+"_"+System.currentTimeMillis());
                                        msg.setUsername(Datas.getUserInfo().getUserName());
                                        msg.setCreateTime(new Date());
                                        EventBus.getDefault().postSticky(new MsgEvent(TAG+".sendAudioMsg",msg));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                        QiNiuUtil.getInstance().upload(token,filePath,null,handler1,null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                Log.i(TAG, "onFailure: "+error);
            }
        };
        QiNiuUtil.getInstance().getToken(callback);
    }

    public void sendVideoMsg(Session session,String filePath){
        HttpCallback callback = new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Log.i(TAG, "onSuccess: "+t);
                try {
                    JSONObject jb = new JSONObject(t);
                    int status = jb.getInt("status");
                    if (status == 200){
                        String token = jb.getString("token");
                        UpCompletionHandler handler1 = new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                Log.i(TAG, "complete: key "+key);
                                Log.i(TAG, "complete: info "+info);
                                Log.i(TAG, "complete: response "+response);
                                if (info.isOK()){
                                    try {
                                        String filename = response.getString("key");
                                        Msg msg = new Msg();
                                        msg.setData(Urls.QINIU_URL+filename);
                                        msg.setFromId(Datas.getUserInfo().getId());
                                        msg.setToId(session.getToId());
                                        msg.setIcon(Datas.getUserInfo().getIcon());
                                        msg.setMsgFrom(session.getMsgFrom());
                                        msg.setMsgType(Msg.MSG_TYPE_VIDEO);
                                        msg.setMsgId("u"+Datas.getUserInfo().getId()+"_"+System.currentTimeMillis());
                                        msg.setUsername(Datas.getUserInfo().getUserName());
                                        msg.setCreateTime(new Date());
                                        EventBus.getDefault().postSticky(new MsgEvent(TAG+".sendVideoMsg",msg));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                        QiNiuUtil.getInstance().upload(token,filePath,null,handler1,null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                Log.i(TAG, "onFailure: "+error);
            }
        };
        QiNiuUtil.getInstance().getToken(callback);
    }

    public List<Session> getSessions(){
        return  DBManager.getInstance().getSessionBox().query().equal(Session_.Belong,Datas.getUserInfo().getId()).notEqual(Session_.tmpMsgCount,-1).orderDesc(Session_.tmpTime).build().find();
    }

    public void clearSessionCount(Session session){
        if (session.getTmpMsgCount()!=0)session.setTmpMsgCount(0);
        DBManager.getInstance().getSessionBox().put(session);

    }

}
