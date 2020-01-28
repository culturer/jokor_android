package vip.jokor.im.pages.main.main_page.group;

import android.util.Log;

import vip.jokor.im.base.Datas;
import vip.jokor.im.model.bean.GroupBean;
import vip.jokor.im.model.db.Msg;
import vip.jokor.im.util.base.GsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import static vip.jokor.im.base.Urls.CODE_ADD;
import static vip.jokor.im.base.Urls.CODE_CREATE;
import static vip.jokor.im.base.Urls.CODE_LIST;
import static vip.jokor.im.base.Urls.CODE_SEARCH;
import static vip.jokor.im.base.Urls.GROUP_PATH;
import static vip.jokor.im.base.Urls.HOST_DATA;

public class GroupPresenter {

    private static final String TAG = "GroupPresenter" ;

    private static final GroupPresenter ourInstance = new GroupPresenter();

    public static GroupPresenter getInstance() {
        return ourInstance;
    }

    private GroupPresenter() { }

    public void createGroup(String icon, String name, int count, HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_CREATE);
        params.put("icon",icon);
        params.put("name",name);
        params.put("count",count);
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

    public void getGroups(HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_LIST);
        params.put("userId",""+Datas.getUserInfo().getId());
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

    public void search_group(String groupId,HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_SEARCH);
        params.put("groupId",groupId);
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

    public void add_group(String groupId,HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_ADD);
        params.put("groupId",groupId);
        params.put("userId",""+Datas.getUserInfo().getId());
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

    public GroupBean loadGroupInfo(Msg msg){
        Log.i(TAG, "loadGroupInfo: 开始加载群名称和头像");
        long groupId = msg.getToId();
        Log.i(TAG, "loadGroupInfo: groupId "+groupId);
        Log.i(TAG, "loadGroupInfo: groupList "+ GsonUtil.getGson().toJson(Datas.getGroups()));
        for (GroupBean item : Datas.getGroups()){
            if (item.getId() == groupId){
                return item;
            }
        }
        return null;
    }
}
