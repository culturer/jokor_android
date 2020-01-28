package vip.jokor.im.pages.util.article;

import android.content.Context;
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

import vip.jokor.im.R;
import vip.jokor.im.model.bean.ReplysBean;
import vip.jokor.im.presenter.ArticlePresenter;
import vip.jokor.im.util.base.TimeUtil;
import vip.jokor.im.wedgit.util.ShowUtil;
import com.kymjs.rxvolley.client.HttpCallback;

import java.util.ArrayList;
import java.util.List;

public class ReplyAdapter extends BaseAdapter {

    private String TAG = "ReplyAdapter";

    private Context context;
    private List<ReplysBean> replys;

    private RequestOptions options1 = new RequestOptions()
            .error(R.mipmap.logo)//加载错误之后的错误图
            .override(400,400)//指定图片的尺寸
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片

    public ReplyAdapter(Context context, List<ReplysBean> replys) {
        this.context = context;
        if (replys!=null) {
            this.replys = replys;
        }else {
            this.replys = new ArrayList<>();
        }
    }

    public void update(ReplysBean replysBean){
        boolean flag = false;
        ReplysBean tmpItem = null;
        for (int i=0;i<replys.size();i++){
            if (replys.get(i).getId() == replysBean.getId()){
                flag = true;
                tmpItem = replys.get(i);
            }
        }
        if (flag){
            if (tmpItem!=null) replys.remove(tmpItem);
        }else {
            replys.add(replysBean);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        if (replys == null) replys = new ArrayList<>();
        return replys.size();
    }

    @Override
    public ReplysBean getItem(int position) {
        if (replys == null) replys = new ArrayList<>();
        if (replys.size()!=0) return replys.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (replys == null) replys = new ArrayList<>();
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.article_reply_item,null);
        contentView.setOnClickListener(null);
        TextView comment = contentView.findViewById(R.id.comment);
        TextView username = contentView.findViewById(R.id.username);
        TextView time = contentView.findViewById(R.id.time);
        ImageView icon = contentView.findViewById(R.id.icon);
        ImageView menu = contentView.findViewById(R.id.menu);

        comment.setText(getItem(position).getContent());
        username.setText(getItem(position).getUsername());
        time.setText(TimeUtil.date2Str(TimeUtil.String2Date(getItem(position).getCreateTime())));
        Glide.with(context)
                .load(getItem(position).getIcon())
                .apply(options1)
                .into(icon);

        menu.setOnClickListener(v -> {
            ShowUtil.showToast(context, "删除");
            HttpCallback callback = new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    Log.e(TAG, "onSuccess: "+t );
                }

                @Override
                public void onFailure(int errorNo, String strMsg) {
                    Log.e(TAG, "onFailure: "+errorNo+","+strMsg );
                }
            };
            ArticlePresenter.getInstance().delFriendsArticles(getItem(position).getArticleId(),callback);
        });

        return contentView;
    }
}
