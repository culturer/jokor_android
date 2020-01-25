package com.jokor.base.presenter;

import com.jokor.base.base.Datas;
import com.jokor.base.model.bean.UserBean;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import static com.jokor.base.base.Urls.CODE_GET_USERINFO;
import static com.jokor.base.base.Urls.HOST_DATA;
import static com.jokor.base.base.Urls.USER_PATH;

public class UserPresenter {
    private static final UserPresenter ourInstance = new UserPresenter();

    public static UserPresenter getInstance() {
        return ourInstance;
    }

    private UserPresenter() { }

    public UserBean loadUserInfo(long userId , HttpCallback callback){
        if (userId == Datas.getUserInfo().getId()){
            return Datas.getUserInfo();
        }
        //本地搜
        if (Datas.getFriendBean()!=null && Datas.getFriendBean().getFriends()!=null){
            for (int i=0;i<Datas.getFriendBean().getFriends().size();i++){
                if (Datas.getFriendBean().getFriends().get(i).getFriend().getId() == userId){
                    return Datas.getFriendBean().getFriends().get(i).getFriend();
                }
            }
        }
        //从网络搜
        loadUserInfoFromNet(userId,callback);
        return null;
    }

    public void loadUserInfoFromNet(long userId,HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_GET_USERINFO);
        params.put("userId",""+userId);
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
