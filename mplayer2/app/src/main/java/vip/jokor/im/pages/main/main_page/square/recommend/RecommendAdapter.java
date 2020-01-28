package vip.jokor.im.pages.main.main_page.square.recommend;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;

import vip.jokor.im.R;
import vip.jokor.im.base.Datas;
import vip.jokor.im.model.bean.FriendsArticlesBean;
import vip.jokor.im.model.bean.LikesBean;
import vip.jokor.im.pages.util.article.ArticleActivity;
import vip.jokor.im.presenter.ArticlePresenter;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.wedgit.likeWedgit.GoodView;
import vip.jokor.im.wedgit.util.ShowUtil;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecommendAdapter extends BaseQuickAdapter<FriendsArticlesBean.ArticlesBean, BaseViewHolder> {

    public RecommendAdapter(int layoutResId, @Nullable List<FriendsArticlesBean.ArticlesBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FriendsArticlesBean.ArticlesBean item) {

        ImageView icon = helper.getView(R.id.icon);
        ImageView close = helper.getView(R.id.close);
        TextView content = helper.getView(R.id.content);
        TextView name = helper.getView(R.id.name);
        ImageView img1 = helper.getView(R.id.img1);
        ImageView img2 = helper.getView(R.id.img2);
        ImageView img3 = helper.getView(R.id.img3);

        TextView reword_count = helper.getView(R.id.reword_count);
        TextView commend_count = helper.getView(R.id.commend_count);
        TextView praise_count = helper.getView(R.id.praise_count);

        ImageView like = helper.getView(R.id.priase);
        setLike(like,item,mContext);

        View.OnClickListener article_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ArticleActivity.class);
                intent.putExtra("data", GsonUtil.getGson().toJson(item));
                mContext.startActivity(intent);
            }
        };
        content.setOnClickListener(article_listener);
        img1.setOnClickListener(article_listener);
        img2.setOnClickListener(article_listener);
        img3.setOnClickListener(article_listener);

        if (item.getArticle().getBelongId() == Datas.getUserInfo().getId()){
            close.setVisibility(View.VISIBLE);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpCallback callback = new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            Log.e(TAG, "onSuccess: "+t );
                            try {
                                JSONObject jb = new JSONObject(t);
                                int status = jb.getInt("status");
                                if (status == 200){
                                    ShowUtil.showToast(mContext,"删除动态成功！");
                                    getData().remove(item);
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
                }
            });
        }else {
            close.setVisibility(View.GONE);
        }

        RequestOptions options1 = new RequestOptions()
                .error(R.mipmap.img_error)//加载错误之后的错误图
                .diskCacheStrategy(DiskCacheStrategy.ALL);//只缓存最终的图片
        RequestOptions options = new RequestOptions()
                .error(R.mipmap.img_error)//加载错误之后的错误图
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);//只缓存最终的图片
        Glide.with(mContext)
                .load(item.getArticle().getIcon())
                .apply(options)
                .into(icon);

        name.setText(item.getArticle().getUsername());
        content.setText(item.getArticle().getContent());
        List<String> images = GsonUtil.getGson().fromJson(item.getArticle().getImgs(),new TypeToken<List<String>>() {}.getType());
        if (images == null || images.size() == 0){
            img1.setVisibility(View.GONE);
            img2.setVisibility(View.GONE);
            img3.setVisibility(View.GONE);
        }
        if (images.size() == 1){
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.INVISIBLE);
            img3.setVisibility(View.INVISIBLE);
            Glide.with(mContext)
                    .load(images.get(0))
                    .apply(options1)
                    .into(img1);
        }
        if (images.size()==2){
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.INVISIBLE);
            Glide.with(mContext)
                    .load(images.get(0))
                    .apply(options1)
                    .into(img1);
            Glide.with(mContext)
                    .load(images.get(1))
                    .apply(options1)
                    .into(img2);
        }
        if (images.size()>2){
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(images.get(0))
                    .apply(options1)
                    .into(img1);
            Glide.with(mContext)
                    .load(images.get(1))
                    .apply(options1)
                    .into(img2);
            Glide.with(mContext)
                    .load(images.get(2))
                    .apply(options1)
                    .into(img3);
        }

        int count_comment = 0;
        if (item.getComments()!=null)count_comment+=item.getComments().size();
        if (item.getReplys()!=null)count_comment+=item.getReplys().size();
        commend_count.setText(""+count_comment);
        int count_likes=0;
        if (item.getLikes()!=null)count_likes+=item.getLikes().size();
        praise_count.setText(""+count_likes);
        int count_fire = count_comment*2+count_likes+16;
        reword_count.setText(""+count_fire);
    }

    private void setLike(ImageView like,FriendsArticlesBean.ArticlesBean item,Context context){
        GoodView mGoodView = new GoodView(context);
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
