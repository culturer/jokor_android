package com.jokor.base.pages.util.userinfo;

import android.content.Context;
import android.content.Intent;
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
import com.jokor.base.model.bean.FriendsArticlesBean;
import com.jokor.base.pages.util.article.ArticleActivity;
import com.jokor.base.util.base.GsonUtil;
import com.jokor.base.util.base.TimeUtil;
import com.jokor.base.wedgit.util.ShowUtil;

import java.util.List;

public class UserInfoAdapter extends BaseAdapter {

    private Context context;
    private List<FriendsArticlesBean.ArticlesBean> datas;

    public UserInfoAdapter(Context context, List<FriendsArticlesBean.ArticlesBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        if (datas!=null)return datas.size();
        return 0;
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
        View contentView = LayoutInflater.from(context).inflate(R.layout.userinfo_list_item,null);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendsArticlesBean.ArticlesBean item = getItem(position);
                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("data", GsonUtil.getGson().toJson(item));
                context.startActivity(intent);
            }
        });

        TextView name = contentView.findViewById(R.id.name);
        ImageView close = contentView.findViewById(R.id.close);
        TextView content = contentView.findViewById(R.id.content);
        ImageView img1 = contentView.findViewById(R.id.img1);
        ImageView img2 = contentView.findViewById(R.id.img2);
        ImageView img3 = contentView.findViewById(R.id.img3);

        TextView reword_count = contentView.findViewById(R.id.reword_count);
        TextView commend_count = contentView.findViewById(R.id.commend_count);
        TextView praise_count = contentView.findViewById(R.id.praise_count);

        FriendsArticlesBean.ArticlesBean item = getItem(position);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowUtil.showToast(context,"删除");
            }
        });

        RequestOptions options1 = new RequestOptions()
                .error(R.mipmap.logo)//加载错误之后的错误图
                .diskCacheStrategy(DiskCacheStrategy.ALL);//只缓存最终的图片
        name.setText(item.getArticle().getCreateTime());
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
            Glide.with(context)
                    .load(images.get(0))
                    .apply(options1)
                    .into(img1);
        }
        if (images.size()==2){
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.INVISIBLE);
            Glide.with(context)
                    .load(images.get(0))
                    .apply(options1)
                    .into(img1);
            Glide.with(context)
                    .load(images.get(1))
                    .apply(options1)
                    .into(img2);
        }
        if (images.size()>2){
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(images.get(0))
                    .apply(options1)
                    .into(img1);
            Glide.with(context)
                    .load(images.get(1))
                    .apply(options1)
                    .into(img2);
            Glide.with(context)
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
        return contentView;
    }
}
