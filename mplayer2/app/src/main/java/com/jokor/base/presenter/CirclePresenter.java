package com.jokor.base.presenter;

import android.util.Log;

import com.jokor.base.base.Config;
import com.jokor.base.base.Datas;
import com.jokor.base.wedgit.util.ShowUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import static com.jokor.base.base.Urls.CIRCLE_PATH;
import static com.jokor.base.base.Urls.CODE_CHECK_CIRCLE_NAME;
import static com.jokor.base.base.Urls.CODE_CREATE_CIRCLE;
import static com.jokor.base.base.Urls.CODE_FOCUS_CIRCLE;
import static com.jokor.base.base.Urls.CODE_GET_CIRCLE_ARTICLES;
import static com.jokor.base.base.Urls.CODE_GET_CIRCLE_INFO;
import static com.jokor.base.base.Urls.CODE_GET_CIRCLE_LIST;
import static com.jokor.base.base.Urls.CODE_GET_CIRCLE_USER;
import static com.jokor.base.base.Urls.CODE_GET_OPTAPPLY;
import static com.jokor.base.base.Urls.CODE_SEARCG_CIRCLE;
import static com.jokor.base.base.Urls.CODE_SIGN_CIRCLE;
import static com.jokor.base.base.Urls.HOST_DATA;
import static com.jokor.base.base.Urls.USER_PATH;

public class CirclePresenter {

    static String TAG = "CirclePresenter" ;

    private static final CirclePresenter ourInstance = new CirclePresenter();

    public static CirclePresenter getInstance() {
        return ourInstance;
    }

    private CirclePresenter() { }

    public void getCircleInfo(long circleId, HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_GET_CIRCLE_INFO);
        params.put("circleId",""+circleId);
        new RxVolley.Builder()
                .url(HOST_DATA+CIRCLE_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }

    public void getCircleUserInfo(long circleId,long userId,HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_GET_CIRCLE_USER);
        params.put("circleId",""+circleId);
        params.put("userId",""+userId);
        new RxVolley.Builder()
                .url(HOST_DATA+CIRCLE_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }

    public void searchCircles(String match , HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_SEARCG_CIRCLE);
        params.put("match",""+match);
        new RxVolley.Builder()
                .url(HOST_DATA+CIRCLE_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }

    public void createCircle(String iconUrl,String name,String msg,long belong2,HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_CREATE_CIRCLE);
        params.put("name",""+name);
        params.put("icon",""+iconUrl);
        params.put("msg",msg);
        params.put("belong1",""+ Datas.getUserInfo().getId());
        params.put("belong2",""+belong2);
        new RxVolley.Builder()
                .url(HOST_DATA+CIRCLE_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }

    public void checkName(String name,HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_CHECK_CIRCLE_NAME);
        params.put("name",""+name);
        new RxVolley.Builder()
                .url(HOST_DATA+CIRCLE_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }

    public void getCircles(HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_GET_CIRCLE_LIST);
        params.put("userId",""+Datas.getUserInfo().getId());
        new RxVolley.Builder()
                .url(HOST_DATA+CIRCLE_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }

    public void sign(long circleId,HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_SIGN_CIRCLE);
        params.put("userId",""+Datas.getUserInfo().getId());
        params.put("circleId",""+circleId);
        new RxVolley.Builder()
                .url(HOST_DATA+CIRCLE_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }

    public void getCircleArticles(long circleId,HttpCallback callback,int page_count){
        HttpParams params = new HttpParams();
        params.put("options",CODE_GET_CIRCLE_ARTICLES);
        params.put("circleId",""+circleId);
        params.put("page_size", Config.page_size);
        params.put("page_count",page_count);
        new RxVolley.Builder()
                .url(HOST_DATA+CIRCLE_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }

    public void focus(long circleId,HttpCallback callback){
        Log.e(TAG, "focus: 开始关注"+circleId );
        HttpParams params = new HttpParams();
        params.put("options",CODE_FOCUS_CIRCLE);
        params.put("circleId",""+circleId);
        params.put("userId",""+Datas.getUserInfo().getId());
        new RxVolley.Builder()
                .url(HOST_DATA+CIRCLE_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8")
                .doTask();
    }
}
