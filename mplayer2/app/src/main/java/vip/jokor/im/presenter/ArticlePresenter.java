package vip.jokor.im.presenter;

import android.util.Log;

import vip.jokor.im.base.Datas;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import static vip.jokor.im.base.Urls.ARTICLE_PATH;
import static vip.jokor.im.base.Urls.CODE_ADD_COMMENT;
import static vip.jokor.im.base.Urls.CODE_ADD_LIKE;
import static vip.jokor.im.base.Urls.CODE_ADD_REPLY;
import static vip.jokor.im.base.Urls.CODE_CREATE_ARTICLE;
import static vip.jokor.im.base.Urls.CODE_DEL_ARTICLE;
import static vip.jokor.im.base.Urls.CODE_FRIENDS_ARTICLE;
import static vip.jokor.im.base.Urls.HOST_DATA;

public class ArticlePresenter {

    private String TAG = "ArticlePresenter";

    private static final ArticlePresenter ourInstance = new ArticlePresenter();

    public static ArticlePresenter getInstance() {
        return ourInstance;
    }

    private ArticlePresenter() { }

    public void publishArticle(String imgs,String video,String content,HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_CREATE_ARTICLE);
        params.put("belongId",""+Datas.getUserInfo().getId());
        params.put("username",Datas.getUserInfo().getUserName());
        params.put("images",imgs);
        params.put("icon",Datas.getUserInfo().getIcon());
        params.put("video",video);
        params.put("content",content);
        new RxVolley.Builder()
                .url(HOST_DATA+ARTICLE_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }

    public void getFriendsArticles(int page,HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_FRIENDS_ARTICLE);
        params.put("userId",""+ Datas.getUserInfo().getId());
        params.put("page",page);
        new RxVolley.Builder()
                .url(HOST_DATA+ARTICLE_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }

    public void addComment(long articleId,String content,HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_ADD_COMMENT);
        params.put("belongId", ""+Datas.getUserInfo().getId());
        params.put("articleId",""+articleId);
        params.put("username",Datas.getUserInfo().getUserName());
        params.put("icon",Datas.getUserInfo().getIcon());
        params.put("content",content);
        new RxVolley.Builder()
                .url(HOST_DATA+ARTICLE_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }

    public void addLike(long articleId,HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_ADD_LIKE);
        params.put("belongId", ""+Datas.getUserInfo().getId());
        params.put("articleId",""+articleId);
        params.put("username",Datas.getUserInfo().getUserName());
        params.put("icon",Datas.getUserInfo().getIcon());
        new RxVolley.Builder()
                .url(HOST_DATA+ARTICLE_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }

    public void addReply(long articleId,long commentId,String content,HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_ADD_REPLY);
        params.put("belongId", ""+Datas.getUserInfo().getId());
        params.put("articleId",""+articleId);
        params.put("commentId",""+commentId);
        params.put("content",content);
        params.put("username",Datas.getUserInfo().getUserName());
        params.put("icon",Datas.getUserInfo().getIcon());
        Log.e(TAG, "addReply: http url is"+HOST_DATA+CODE_ADD_REPLY );
        new RxVolley.Builder()
                .url(HOST_DATA+ARTICLE_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }

    public void delReply(long replyId){

    }

    public void delComment(long commentId){

    }

    public void delFriendsArticles(long articleId,HttpCallback callback){
        HttpParams params = new HttpParams();
        params.put("options",CODE_DEL_ARTICLE);
        params.put("belongId", ""+Datas.getUserInfo().getId());
        params.put("articleId",""+articleId);
        new RxVolley.Builder()
                .url(HOST_DATA+ARTICLE_PATH)
                .httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
                .contentType(RxVolley.ContentType.FORM)//default FORM or JSON
                .params(params)
                .shouldCache(false) //default: get true, post false
                .callback(callback)
                .encoding("UTF-8") //default
                .doTask();
    }

    public void feedbackArticle(long articleId ,String content){

    }

    public void feedbackComment(long commentId ,String content){

    }

    public void feedbackReply(long replyId , String content){

    }

}
