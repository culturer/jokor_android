package vip.jokor.im.pages.util.video;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import vip.jokor.im.R;
import vip.jokor.im.base.Datas;

import java.util.List;

public class ListVideoAdapter extends BaseRecAdapter<String, ListVideoAdapter.VideoViewHolder> {


    public ListVideoAdapter(List<String> list) {
        super(list);
    }

    @Override
    public void onHolder(VideoViewHolder holder, String bean, int position) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

        holder.mp_video.setUp(bean, "", MyVideoPlayer.STATE_NORMAL);
        if (position == 0) {
            holder.mp_video.startVideo();
        }
        Glide.with(context).load(bean).into(holder.mp_video.thumbImageView);
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);//只缓存最终的图片
        Glide.with(context)
                .load(R.drawable.cd)
                .apply(options)
                .into(holder.gif);
        RequestOptions options1 = new RequestOptions()
                .error(R.mipmap.logo)//加载错误之后的错误图
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);//只缓存最终的图片
        Glide.with(context)
                .load(Datas.getUserInfo().getIcon())
                .apply(options1)
                .into(holder.icon);
    }

    @Override
    public VideoViewHolder onCreateHolder() {
        return new VideoViewHolder(getViewByRes(R.layout.item_page2));
    }

    public static class VideoViewHolder extends BaseRecViewHolder {

        public View rootView;
        public MyVideoPlayer mp_video;
        public ImageView icon;
        public ImageView like;
        public ImageView comment;
        public ImageView share;
        public ImageView gif;

        public TextView like_count;
        public TextView comment_count;
        public TextView share_count;

        public VideoViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.mp_video = rootView.findViewById(R.id.mp_video);
            this.icon = rootView.findViewById(R.id.icon);
            this.like = rootView.findViewById(R.id.like);
            this.comment = rootView.findViewById(R.id.comment);
            this.share = rootView.findViewById(R.id.share);
            this.gif = rootView.findViewById(R.id.gif);
            this.like_count = rootView.findViewById(R.id.like_count);
            this.comment_count = rootView.findViewById(R.id.comment_count);
            this.share_count = rootView.findViewById(R.id.share_count);
        }

    }
}
