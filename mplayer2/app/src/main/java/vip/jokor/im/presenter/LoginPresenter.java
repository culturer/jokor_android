package vip.jokor.im.presenter;

import android.util.Log;

import vip.jokor.im.base.Datas;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import static vip.jokor.im.base.Urls.CODE_LOGIN;
import static vip.jokor.im.base.Urls.HOST_DATA;
import static vip.jokor.im.base.Urls.USER_PATH;

public class LoginPresenter {

    private String TAG = "LoginPresenter";

    private static final LoginPresenter ourInstance = new LoginPresenter();

    public static LoginPresenter getInstance() {
        return ourInstance;
    }

    private LoginPresenter() {

    }

    public void login(String tel ,String pwd , Callback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_LOGIN);
        params.put("tel",tel);
        params.put("pwd",pwd);
        HttpCallback callback1 = new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Log.i(TAG, "login: "+t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    if (jsonObject!=null){
                        int status = jsonObject.getInt("status");
                        if (status == 200){
                            if (!jsonObject.getString("user").equals("")){
                                Datas.saveUserInfo(jsonObject.getString("user"));
                                Datas.setAutoLogin(true);
                                callback.callback(true,"");
                            }else {
                                callback.callback(false,"登录失败！请检查网络后再次尝试！");
                            }
                        }else {
                            callback.callback(false,jsonObject.getString("msg"));
                        }
                    }else {
                        callback.callback(false,"登录失败！请检查网络后再次尝试！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                Log.i(TAG, "login: "+error.getMessage());
                Log.i(TAG, "onFailure: networkResponse "+error.networkResponse);
                Log.i(TAG, "onFailure: getLocalizedMessage "+error.getLocalizedMessage());
                Log.i(TAG, "onFailure: "+error.getCause().getMessage());
                callback.callback(false,"登录失败！请检查网络后再次尝试！");
            }
        };
        new RxVolley.Builder()
                .url(HOST_DATA+USER_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback1)
                .encoding("UTF-8") //default
                .doTask();
    }

    public void register(){

    }

    public interface Callback{
        void callback(boolean isSuccess, String msg);
    }

}
