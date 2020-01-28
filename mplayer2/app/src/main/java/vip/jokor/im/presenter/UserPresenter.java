package vip.jokor.im.presenter;

import android.util.Log;

import vip.jokor.im.base.Datas;
import vip.jokor.im.model.bean.UserBean;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import static vip.jokor.im.base.Urls.CODE_ADD_USERLIST;
import static vip.jokor.im.base.Urls.CODE_DEL_USER;
import static vip.jokor.im.base.Urls.CODE_GET_USERINFO;
import static vip.jokor.im.base.Urls.HOST_DATA;
import static vip.jokor.im.base.Urls.USER_PATH;

public class UserPresenter {

    private static final String TAG = "UserPresenter" ;

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

    public void deleteFriend(String tag,long friendId , HttpCallback callback){
        Log.e(TAG, "deleteFriend: TAG --- "+tag );
        HttpParams params = new HttpParams();
        params.put("options",CODE_DEL_USER);
        params.put("userId",""+Datas.getUserInfo().getId());
        params.put("friendId",""+friendId);
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

    public void addFriend(String tag , long friendId,long category,String msg ,HttpCallback callback){
        Log.e(TAG, "addFriend: TAG --- "+tag );
        Log.e(TAG, "addFriend: fromCategoryId "+category );
        HttpParams params = new HttpParams();
        params.put("options",CODE_ADD_USERLIST);
        params.put("friendId",""+friendId);
        params.put("userId",""+Datas.getUserInfo().getId());
        params.put("msg",msg);
        params.put("categoryId",""+category);
        params.put("fromUsername",Datas.getUserInfo().getUserName());
        params.put("fromIcon",Datas.getUserInfo().getIcon());

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
