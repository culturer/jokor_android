package vip.jokor.im.presenter;

import android.content.Context;

import vip.jokor.im.base.Datas;
import vip.jokor.im.model.bean.AppliesBean;
import vip.jokor.im.wedgit.util.ShowUtil;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import static vip.jokor.im.base.Urls.CODE_GET_FRIEND;
import static vip.jokor.im.base.Urls.CODE_GET_OPTAPPLY;
import static vip.jokor.im.base.Urls.HOST_DATA;
import static vip.jokor.im.base.Urls.USER_PATH;

public class FriendPresenter {

    private static final String TAG = "FriendPresenter" ;

    private static final FriendPresenter ourInstance = new FriendPresenter();

    public static FriendPresenter getInstance( ) {
        return ourInstance;
    }

    private FriendPresenter() {

    }

    public void through(Context context, AppliesBean data, int opt, HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_GET_OPTAPPLY);
        params.put("applyId",""+data.getId());
        params.put("do",opt);
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
