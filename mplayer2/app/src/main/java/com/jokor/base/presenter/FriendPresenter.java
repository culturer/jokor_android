package com.jokor.base.presenter;

import android.content.Context;

import com.jokor.base.base.Datas;
import com.jokor.base.model.bean.AppliesBean;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import static com.jokor.base.base.Urls.CODE_GET_FRIEND;
import static com.jokor.base.base.Urls.CODE_GET_OPTAPPLY;
import static com.jokor.base.base.Urls.HOST_DATA;
import static com.jokor.base.base.Urls.USER_PATH;

public class FriendPresenter {

    private static final String TAG = "FriendPresenter" ;

    private static final FriendPresenter ourInstance = new FriendPresenter();

    public static FriendPresenter getInstance( ) {
        return ourInstance;
    }

    private FriendPresenter() {

    }

    public static final int OPT_SEND = 0 ;
    public static final int OPT_THROUGH = 1 ;
    public static final int OPT_REFUSE = 2 ;
    public static final int OPT_IGNORE = 3 ;
    public void through(Context context, AppliesBean data,int opt,HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_GET_OPTAPPLY);
        params.put("applyId",""+data.getId());
        params.put("do",opt);
//        HttpCallback callback = new HttpCallback() {
//            @Override
//            public void onSuccess(String t) {
//                Log.e(TAG, "onSuccess: "+t);
//            }
//
//            @Override
//            public void onFailure(VolleyError error) {
//                ShowUtil.showToast(context,"网络异常！");
//            }
//        };
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

    public void getFriemds(HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("userId",""+ Datas.getUserInfo().getId());
        params.put("options",CODE_GET_FRIEND);
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

}
