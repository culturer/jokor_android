package com.jokor.base.presenter;

import com.jokor.base.base.Datas;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import static com.jokor.base.base.Urls.CODE_GET_RECOMMENDS;
import static com.jokor.base.base.Urls.HOST_DATA;
import static com.jokor.base.base.Urls.RECOMMEND_PATH;

public class RecommendPresenter {
    private static final RecommendPresenter ourInstance = new RecommendPresenter();

    public static RecommendPresenter getInstance() {
        return ourInstance;
    }

    private RecommendPresenter() {
    }

    public void getRecommends(HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_GET_RECOMMENDS);
        params.put("userId", ""+Datas.getUserInfo().getId());
        params.put("sex",""+false);
        params.put("age",18);
        new RxVolley.Builder()
                .url(HOST_DATA+RECOMMEND_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }
}
