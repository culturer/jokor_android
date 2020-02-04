package vip.jokor.im.presenter;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import vip.jokor.im.base.Datas;

import static vip.jokor.im.base.Urls.CODE_GET_USERINFO;
import static vip.jokor.im.base.Urls.CODE_LIST;
import static vip.jokor.im.base.Urls.GROUP_PATH;
import static vip.jokor.im.base.Urls.HOST_DATA;
import static vip.jokor.im.base.Urls.USER_PATH;

public class GroupPresenter {
    private static final GroupPresenter ourInstance = new GroupPresenter();

    public static GroupPresenter getInstance() {
        return ourInstance;
    }

    private GroupPresenter() { }

    public void getGroupList(HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_LIST);
        params.put("userId",""+ Datas.getUserInfo().getId());
        new RxVolley.Builder()
                .url(HOST_DATA+GROUP_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }
}
