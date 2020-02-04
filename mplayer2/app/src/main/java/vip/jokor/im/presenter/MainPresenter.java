package vip.jokor.im.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import vip.jokor.im.R;
import vip.jokor.im.base.Datas;
import vip.jokor.im.base.Urls;
import vip.jokor.im.im.MsgService;
import vip.jokor.im.model.bean.FriendsBean;
import vip.jokor.im.model.bean.GroupBean;
import vip.jokor.im.model.bean.GroupListBean;
import vip.jokor.im.model.db.DBManager;
import vip.jokor.im.model.db.Session;
import vip.jokor.im.model.db.Session_;
import vip.jokor.im.pages.login.LoginActivity;
import vip.jokor.im.pages.main.main_page.chat.chat_page.ChatActivity;
import vip.jokor.im.util.base.DrawUtil;
import vip.jokor.im.util.base.FileUtil;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.wedgit.util.QrCodeUtil;
import vip.jokor.im.util.base.ServiceUtil;
import vip.jokor.im.util.base.ShareUtil;
import vip.jokor.im.wedgit.util.ShowUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cn.bertsir.zbar.utils.QRUtils;
import io.objectbox.Box;
import vip.jokor.im.model.db.Msg;

import static vip.jokor.im.base.Urls.CODE_ADD_USERLIST;
import static vip.jokor.im.base.Urls.CODE_GET_7NIUTOKEN;
import static vip.jokor.im.base.Urls.CODE_GET_APPLIES;
import static vip.jokor.im.base.Urls.CODE_GET_USERINFO;

import static vip.jokor.im.base.Urls.CODE_UPDATE_DEVICETOKEN;
import static vip.jokor.im.base.Urls.CONFIG_PATH;
import static vip.jokor.im.base.Urls.HOST_DATA;
import static vip.jokor.im.base.Urls.USER_PATH;

public class MainPresenter {

    private static final String TAG = "MainPresenter";

    private static final MainPresenter ourInstance = new MainPresenter();

    public static MainPresenter getInstance() {
        return ourInstance;
    }
    private MainPresenter() { }

    private Context context;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (!ServiceUtil.isServiceRunning(context, "MsgService") && ServiceUtil.isOnForground(context)){
                //在这里要监控APP是否前台运行，在前台运行就启动service
                 context.startService(new Intent(context, MsgService.class));
            }else {Log.e(TAG, "run: MsgService 正在运行" );}
        }
    };

    public void startIM(Context context){
        this.context = context;
        //启动IM服务
        if (!ServiceUtil.isServiceRunning(context, "MsgService") && ServiceUtil.isOnForground(context)){
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessageAtTime(0,0);
                }
            };
            timer.schedule(task,0,60*1000);
        }
    }

    //是否自动登录
    public boolean isAutoLogin(){
        return Datas.isAutoLogin() && Datas.getUserInfo()!=null ;
    }

    //APP分享
    public void appShare(Context context){
        String text = context.getResources().getString(R.string.share_app_text);
        String title = context.getResources().getString(R.string.share_app_title);
        ShareUtil.shareText(context,text,title);
    }

    //退出登录
    public void logout(Activity activity){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        android.app.AlertDialog dialog = builder
                .setTitle("确定退出登录")
                .setPositiveButton("退出", (dialogInterface, i) -> {
                    //清除用户数据
                    Datas.setAutoLogin(false);
                    activity.stopService(new Intent(activity,MsgService.class));
                    //退回到登录页面
                    Intent intent = new Intent(activity, LoginActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                })
                .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss())
                .create();
        dialog.show();
    }

    //显示二维码
    public void showQRCode(int qrInfoType, String qrInfo,String qrName, Context context){
        RequestOptions options = new RequestOptions()
//                .override(300,300)//指定图片的尺寸
                .fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
        Glide.with(context)
                .load(Datas.getUserInfo().getIcon())
                .apply(options)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Bitmap bitmap = DrawUtil.drawable2Bitmap(resource);
                        //生成二维码
                        QrCodeUtil.QRInfo info = new QrCodeUtil.QRInfo(qrInfoType,qrInfo);
                        Bitmap qrCode = QRUtils.getInstance().createQRCodeAddLogo(GsonUtil.getGson().toJson(info),1024,1024,bitmap);
                        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_show_qrcode,null);
                        ImageView img = contentView.findViewById(R.id.img);
                        img.setImageBitmap(qrCode);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        final AlertDialog dialog = builder
                                .setTitle("扫码加好友")
                                .setView(contentView)
                                .setPositiveButton("分享", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String filePath = FileUtil.saveBitmap(qrCode,qrName,context);
                                        Log.i(TAG, "filePath: "+filePath);
                                        ShareUtil.shareImage(context,filePath);
                                    }
                                })
                                .setNegativeButton("保存", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FileUtil.saveBitmap(qrCode,qrName,context);
                                        ShowUtil.showToast(context,"二维码已经保存到相册了哦！");
                                    }
                                })
                                .create();
                        dialog.show(); }});
    }

    //创建单聊会话
    public void openP2PSession(FriendsBean friendsBean, Context context,boolean isNewFriend){
        Log.e(TAG, "openP2PSession: 打开会话页面" );
        //1.查询数据库，如果有则直接打开
        //2.如果没有就创建后打开
        Box<Session> sessionBox =  DBManager.getInstance().getSessionBox();
        Session session = sessionBox.query().equal(Session_.msgFrom, Msg.MSG_FROM_FRIEND).equal(Session_.Belong,Datas.getUserInfo().getId()).equal(Session_.Id,friendsBean.getFriend().getId()).build().findUnique();
        if ( session == null ){
            session = new Session() ;
            session.setDataFromFriendsBean(friendsBean) ;
            session.setTmpMsg("");
            session.setTmpMsgCount(-1);
            session.setTmpTime(new Date(System.currentTimeMillis()));
            Log.e(TAG, "openP2PSession: 保存会话"+GsonUtil.getGson().toJson(session) );
            sessionBox.put(session);
        }
        if (isNewFriend){
            ChatPresenter.getInstance().senTextdMsg(session,"你好");
        }
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(ChatActivity.CHAT_TYPE, Msg.MSG_FROM_FRIEND);
        intent.putExtra("session",GsonUtil.getGson().toJson(session));
        context.startActivity(intent);
    }

    public void openGroupSession(GroupListBean.GroupsBean groupBean, Context context){
        //1.查询数据库，如果有则直接打开
        //2.如果没有就创建后打开
        Box<Session> sessionBox =  DBManager.getInstance().getSessionBox();
        Session session = sessionBox.query().equal(Session_.msgFrom, Msg.MSG_FROM_GROUP).equal(Session_.Belong,Datas.getUserInfo().getId()).equal(Session_.Id,groupBean.getId()).build().findUnique();
        if ( session == null ){
            session = new Session() ;
            session.setDataFromGroupBean(groupBean);
            session.setTmpMsg("");
            session.setTmpMsgCount(-1);
            session.setTmpTime(new Date(System.currentTimeMillis()));
            sessionBox.put(session);
        }
//        Log.e(TAG, "openSession: friend --- "+GsonUtil.getGson().toJson(friendsBean) );
//        Log.e(TAG, "openSession: session --- "+GsonUtil.getGson().toJson(session) );
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(ChatActivity.CHAT_TYPE, Msg.MSG_FROM_GROUP);
        intent.putExtra("session",GsonUtil.getGson().toJson(session));
        context.startActivity(intent);
    }

    public void getApplies(HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_GET_APPLIES);
        params.put("userId",""+Datas.getUserInfo().getId());
        new RxVolley.Builder()
                .url(HOST_DATA+USER_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }

    public void getUserInfo(String tel,HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_GET_USERINFO);
        params.put("tel",tel);
        new RxVolley.Builder()
                .url(HOST_DATA+USER_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }



    public void updateDeviceToken(){
        if (Datas.getUserInfo()!=null && Datas.getUserInfo().getId()!=0 && !Urls.DEVICE_TOKEN.equals("")){
            HttpCallback callback = new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    Log.e(TAG, "updateDeviceToken onSuccess: "+t );
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.e(TAG, "updateDeviceToken onFailure: "+error.getMessage() );
                }
            };
            Log.e(TAG, "updateDeviceToken: token "+Urls.DEVICE_TOKEN );
            Log.e(TAG, "updateDeviceToken: channel "+android.os.Build.BRAND );
            HttpParams params = new HttpParams();
            params.put("options",CODE_UPDATE_DEVICETOKEN);
            params.put("userId",""+Datas.getUserInfo().getId());
            params.put("deviceToken",Urls.DEVICE_TOKEN);
            params.put("channel", Build.MANUFACTURER);
            params.put("plat","android");
            new RxVolley.Builder()
                    .url(HOST_DATA+CONFIG_PATH)
                    .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                    .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                    .params(params)
                    .shouldCache(false) //default: get true, post false
                    .callback(callback)
                    .encoding("UTF-8") //default
                    .doTask();
        }
    }

}
