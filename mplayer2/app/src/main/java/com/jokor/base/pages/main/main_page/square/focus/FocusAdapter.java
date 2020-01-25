package com.jokor.base.pages.main.main_page.square.focus;

import android.app.Activity;

import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.reflect.TypeToken;
import com.jokor.base.R;
import com.jokor.base.base.Datas;
import com.jokor.base.model.bean.CommentsBean;
import com.jokor.base.model.bean.FriendsArticlesBean;
import com.jokor.base.model.bean.LikesBean;
import com.jokor.base.model.bean.ReplysBean;
import com.jokor.base.pages.util.userinfo.UserInfoActivity;
import com.jokor.base.presenter.ArticlePresenter;
import com.jokor.base.pages.util.floateditor.EditorCallback;
import com.jokor.base.pages.util.floateditor.EditorHolder;
import com.jokor.base.pages.util.floateditor.FloatEditorActivity;
import com.jokor.base.util.base.GsonUtil;
import com.jokor.base.util.base.TimeUtil;
import com.jokor.base.util.glide.MediaLoader;
import com.jokor.base.wedgit.util.ShowUtil;
import com.kymjs.rxvolley.client.HttpCallback;
import com.wx.goodview.GoodView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FocusAdapter extends BaseAdapter {

    private static final String TAG = "FocusAdapter" ;

    private  List<FriendsArticlesBean.ArticlesBean> datas;
    private Activity activity;
    private FocusFragment focusFragment;

    public FocusAdapter(FocusFragment focusFragment,List<FriendsArticlesBean.ArticlesBean> datas, Activity activity) {
        this.datas = datas;
        if (datas == null)datas = new ArrayList<>();
        this.activity = activity;
        this.focusFragment = focusFragment;
    }

    public void update(List<FriendsArticlesBean.ArticlesBean> datas){
        if (datas!=null)this.datas = datas;
        notifyDataSetChanged();
    }

    public void update(FriendsArticlesBean.ArticlesBean articlesBean){
        boolean flag = false;
        for ( int i=0;i<datas.size();i++) {
            FriendsArticlesBean.ArticlesBean item = datas.get(i);
            if (articlesBean.getArticle().getId() == item.getArticle().getId()){
                flag = true;
                datas.remove(i);
                datas.add(i,articlesBean);
                notifyDataSetChanged();
                break;
            }
        }
        if (!flag){
            datas.add(0,articlesBean);
            notifyDataSetChanged();
        }
    }

    public void update(LikesBean likesBean ){
        for (int i=0;i<datas.size();i++){
            if (datas.get(i).getArticle().getId() == likesBean.getArticleId()){
                datas.get(i).getLikes().add(0,likesBean);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void update1(List<CommentsBean> commentsBeans){
        if (datas == null) datas = new ArrayList<>();
        for (int i=0;i<datas.size();i++){
            for (int j=0;j<commentsBeans.size();j++){
                if (datas.get(i).getArticle().getId() == commentsBeans.get(j).getArticleId()){
                    datas.get(i).getComments().add(0,commentsBeans.get(j));
                }
            }
        }
        notifyDataSetChanged();
    }

    public void update2(List<ReplysBean> replysBeans ){
        for (int i=0;i<datas.size();i++){
            for (int j=0;j<replysBeans.size();j++){
                if (datas.get(i).getArticle().getId() == replysBeans.get(j).getArticleId()){
                    if (datas.get(i).getReplys() == null)datas.get(i).setReplys(new ArrayList<>());
                    datas.get(i).getReplys().add(0,replysBeans.get(j));
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public FriendsArticlesBean.ArticlesBean getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View contentView = LayoutInflater.from(activity).inflate(R.layout.focus_item,null);
        contentView.setOnClickListener(null);
        ImageView icon = contentView.findViewById(R.id.icon);
        TextView username = contentView.findViewById(R.id.username);
        TextView time = contentView.findViewById(R.id.time);
        ImageView menu = contentView.findViewById(R.id.menu);

        Banner banner = contentView.findViewById(R.id.banner);
        TextView content = contentView.findViewById(R.id.content);
        ImageView user1 = contentView.findViewById(R.id.user1);
        ImageView user2 = contentView.findViewById(R.id.user2);
        ImageView user3 = contentView.findViewById(R.id.user3);
        TextView like_count = contentView.findViewById(R.id.like_count);
        ImageView like = contentView.findViewById(R.id.like);
        TextView comment = contentView.findViewById(R.id.comment);
        TextView comment_count = contentView.findViewById(R.id.comment_count);
        TextView comment1 = contentView.findViewById(R.id.comment1);
        TextView comment2 = contentView.findViewById(R.id.comment2);
        TextView comment3 = contentView.findViewById(R.id.comment3);

        FriendsArticlesBean.ArticlesBean item = getItem(position);

        Log.e(TAG, "getView: item "+GsonUtil.getGson().toJson(item) );
        Log.e(TAG, "getView: item username "+item.getArticle().getUsername() );
        Log.e(TAG, "getView: item icon "+item.getArticle().getIcon() );

        setBanner(banner,item);
        setLike(like,item);
        RequestOptions options1 = new RequestOptions()
                .error(R.mipmap.logo)//加载错误之后的错误图
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);//只缓存最终的图片
        Glide.with(activity)
                .load(item.getArticle().getIcon())
                .apply(options1)
                .into(icon);
        icon.setOnClickListener(v -> {
            //进入个人名片界面
            Intent intent = new Intent(activity, UserInfoActivity.class);
            activity.startActivity(intent);
        });
        username.setText(""+item.getArticle().getUsername());
        time.setText(TimeUtil.date2Str(TimeUtil.String2Date(item.getArticle().getCreateTime())));

        if (item.getArticle().getBelongId() == Datas.getUserInfo().getId()){
            menu.setVisibility(View.VISIBLE);
            menu.setOnClickListener(v -> {
                HttpCallback callback = new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        Log.e(TAG, "onSuccess: "+t );
                        try {
                            JSONObject jb = new JSONObject(t);
                            int status = jb.getInt("status");
                            if (status == 200){
                                ShowUtil.showToast(activity,"删除动态成功！");
                                datas.remove(position);
                                notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        Log.e(TAG, "onFailure: "+errorNo+","+strMsg );
                    }
                };
                ArticlePresenter.getInstance().delFriendsArticles(item.getArticle().getId(),callback);
            });
        }else {
            menu.setVisibility(View.GONE);
            menu.setOnClickListener(null);
       }

        comment.setOnClickListener(v -> {
            //点击进入评论
            EditorCallback editorCallback = new EditorCallback() {
                @Override
                public void onCancel() {
                    ShowUtil.showToast(activity,"取消");
                }

                @Override
                public void onSubmit(String content) {

                    HttpCallback callback = new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            Log.e(TAG, "onSuccess: "+t );
                            try {
                                JSONObject jb = new JSONObject(t);
                                int status = jb.getInt("status");
                                if (status == 200){
                                    CommentsBean commentsBean = GsonUtil.getGson().fromJson(jb.getString("comment"), CommentsBean.class);
                                    if (commentsBean!=null){
                                        if (item.getComments() == null)item.setComments(new ArrayList<>());
                                        item.getComments().add(0,commentsBean);
                                        notifyDataSetChanged();
                                    }
                                }else{
                                    ShowUtil.showToast(activity,"评论失败，请稍后再试");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int errorNo, String strMsg) {
                            Log.e(TAG, "onFailure: "+errorNo+","+strMsg );
                        }
                    };
                    ArticlePresenter.getInstance().addComment(item.getArticle().getId(),content,callback);
                }

                @Override
                public void onAttached(ViewGroup rootView) {

                }
            };

            String subtitle;
            if (item.getArticle().getContent().length()>5){
                subtitle = "回复 - "+ item.getArticle().getContent().substring(0,5)+"...";
            }else {
                subtitle = "回复 - "+ item.getArticle().getContent();
            }
            FloatEditorActivity.openEditor(activity,subtitle,editorCallback, new EditorHolder(R.layout.fast_reply_floating_layout, R.id.tv_cancel, R.id.tv_submit, R.id.et_content,R.id.tv_title));

        });

        content.setOnClickListener(v -> {
            //点击进入详情
//            Intent intent = new Intent(activity, ArticleActivity.class);
//            intent.putExtra("data", GsonUtil.getGson().toJson(item));
//            activity.startActivityForResult(intent,1);
            focusFragment.startArticlePage(GsonUtil.getGson().toJson(item));
        });

        comment_count.setOnClickListener(v -> {
            //进入详情
//            Intent intent = new Intent(activity, ArticleActivity.class);
//            intent.putExtra("data", GsonUtil.getGson().toJson(item));
//            activity.startActivityForResult(intent,1);
            focusFragment.startArticlePage(GsonUtil.getGson().toJson(item));
        });
        content.setText(item.getArticle().getContent());
        if (item.getLikes() == null || item.getLikes().size() ==0){
            user1.setVisibility(View.GONE);
            user2.setVisibility(View.GONE);
            user3.setVisibility(View.GONE);
            like_count.setText("");
        }else {
            String strLikeCount = "等"+item.getLikes().size()+"人觉得很赞";
            like_count.setText(strLikeCount);
            switch (item.getLikes().size()){
                case 1:
                    user1.setVisibility(View.VISIBLE);
                    user2.setVisibility(View.GONE);
                    user3.setVisibility(View.GONE);
                    like_count.setVisibility(View.VISIBLE);
                    Glide.with(activity)
                            .load(item.getLikes().get(0).getIcon())
                            .apply(options1)
                            .into(user1);
                    break;
                case 2:
                    user1.setVisibility(View.VISIBLE);
                    user2.setVisibility(View.VISIBLE);
                    user3.setVisibility(View.GONE);
                    like_count.setVisibility(View.VISIBLE);
                    Glide.with(activity)
                            .load(item.getLikes().get(0).getIcon())
                            .apply(options1)
                            .into(user1);
                    Glide.with(activity)
                            .load(item.getLikes().get(1).getIcon())
                            .apply(options1)
                            .into(user2);
                    break;
                case 3:
                    user1.setVisibility(View.VISIBLE);
                    user2.setVisibility(View.VISIBLE);
                    user3.setVisibility(View.VISIBLE);
                    like_count.setVisibility(View.VISIBLE);
                    Glide.with(activity)
                            .load(item.getLikes().get(0).getIcon())
                            .apply(options1)
                            .into(user1);
                    Glide.with(activity)
                            .load(item.getLikes().get(1).getIcon())
                            .apply(options1)
                            .into(user2);
                    Glide.with(activity)
                            .load(item.getLikes().get(2).getIcon())
                            .apply(options1)
                            .into(user3);
                    break;
            }
        }

        if (item.getComments() ==null || item.getComments().size() == 0){
            Log.e(TAG, "getView: comment is null !" );
            comment1.setVisibility(View.GONE);
            comment2.setVisibility(View.GONE);
            comment3.setVisibility(View.GONE);
            comment_count.setVisibility(View.GONE);
        }else {
            String strCommentCount ="共" + item.getComments().size()+"条评论";
            comment_count.setText(strCommentCount);
            switch (item.getComments().size()){
                case 1:
                    String comm1 = item.getComments().get(0).getUsername() +" :  "+ item.getComments().get(0).getContent();
                    SpannableString spcom1 = new SpannableString(comm1);
                    spcom1.setSpan(new ForegroundColorSpan(activity.getColor(R.color.red)),0,item.getComments().get(0).getUsername().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment1.setText(spcom1);
                    comment1.setVisibility(View.VISIBLE);

                    comment2.setVisibility(View.GONE);
                    comment3.setVisibility(View.GONE);
                    comment_count.setVisibility(View.GONE);
                    break;
                case 2:
                    String comm21 = item.getComments().get(0).getUsername() +" :  "+ item.getComments().get(0).getContent();
                    SpannableString spcom21 = new SpannableString(comm21);
                    spcom21.setSpan(new ForegroundColorSpan(activity.getColor(R.color.red)),0,item.getComments().get(0).getUsername().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment1.setVisibility(View.VISIBLE);
                    comment1.setText(spcom21);

                    String comm22 = item.getComments().get(1).getUsername() +" :  "+ item.getComments().get(1).getContent();
                    SpannableString spcom22 = new SpannableString(comm22);
                    spcom22.setSpan(new ForegroundColorSpan(activity.getColor(R.color.red)),0,item.getComments().get(1).getUsername().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment2.setVisibility(View.VISIBLE);
                    comment2.setText(spcom22);

                    comment3.setVisibility(View.GONE);
                    comment_count.setVisibility(View.GONE);
                    break;
                default:
                    String comm31 = item.getComments().get(0).getUsername() +" :  "+ item.getComments().get(0).getContent();
                    SpannableString spcom31 = new SpannableString(comm31);
                    spcom31.setSpan(new ForegroundColorSpan(activity.getColor(R.color.red)),0,item.getComments().get(0).getUsername().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment1.setVisibility(View.VISIBLE);
                    comment1.setText(spcom31);

                    String comm32 = item.getComments().get(1).getUsername() +" :  "+ item.getComments().get(1).getContent();
                    SpannableString spcom32 = new SpannableString(comm32);
                    spcom32.setSpan(new ForegroundColorSpan(activity.getColor(R.color.red)),0,item.getComments().get(1).getUsername().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment2.setVisibility(View.VISIBLE);
                    comment2.setText(spcom32);

                    String comm33 = item.getComments().get(2).getUsername() +" :  "+ item.getComments().get(2).getContent();
                    SpannableString spcom33 = new SpannableString(comm33);
                    spcom33.setSpan(new ForegroundColorSpan(activity.getColor(R.color.red)),0,item.getComments().get(2).getUsername().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment3.setVisibility(View.VISIBLE);
                    comment3.setText(spcom33);
                    if (item.getComments().size() == 3){
                        comment_count.setVisibility(View.GONE);
                    }else {
                        comment_count.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
        return contentView;
    }

    private void setBanner(Banner banner,FriendsArticlesBean.ArticlesBean item){
        List<String> images = GsonUtil.getGson().fromJson(item.getArticle().getImgs(),new TypeToken<List<String>>() {}.getType());
        if (images == null || images.size() == 0){
            banner.setVisibility(View.GONE);
        }else {
            //设置banner样式
            banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
            //设置banner动画效果
            banner.setBannerAnimation(Transformer.DepthPage);
            //设置图片加载器
            banner.setImageLoader(new MediaLoader());
            //设置图片集合
            banner.setImages(images);
            //设置自动轮播，默认为true`
            banner.isAutoPlay(true);
            //设置轮播时间
            banner.setDelayTime(3*1000);
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.RIGHT);
            banner.setOnBannerListener(position -> {
//                Intent intent = new Intent(activity, ArticleActivity.class);
//                intent.putExtra("data", GsonUtil.getGson().toJson(item));
//                activity.startActivityForResult(intent,1);
                focusFragment.startArticlePage(GsonUtil.getGson().toJson(item));
            });
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }
    }

    private void setLike(ImageView like,FriendsArticlesBean.ArticlesBean item){
        com.wx.goodview.GoodView mGoodView = new GoodView(activity);
        boolean flag = false;
        if (item.getLikes()!=null &&item.getLikes().size()>0){
            for (int i=0;i<item.getLikes().size();i++){
                if (item.getLikes().get(i).getBelongId() == Datas.getUserInfo().getId()){
                    like.setImageResource(R.drawable.like_checked);
                    like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            like.setImageResource(R.drawable.like_checked);
                            mGoodView.setText("+1");
                            mGoodView.show(like);
                        }
                    });
                    flag = true;
                }
            }
        }
        if (!flag){
            like.setOnClickListener(v -> {
                like.setImageResource(R.drawable.like_checked);
                mGoodView.setText("+1");
                mGoodView.show(like);
                HttpCallback callback = new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        Log.e(TAG, "onSuccess: "+t );
                        try {
                            JSONObject jb = new JSONObject(t);
                            int status = jb.getInt("status");
                            if (status == 200){
                                LikesBean likesBean = GsonUtil.getGson().fromJson(jb.getString("like"),LikesBean.class);
                                if (item.getLikes() == null)item.setLikes(new ArrayList<>());
                                item.getLikes().add(0,likesBean);
                                notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        Log.e(TAG, "onFailure: "+errorNo+","+strMsg );
                    }
                };
                ArticlePresenter.getInstance().addLike(item.getArticle().getId(),callback);
            });
        }
    }

}
