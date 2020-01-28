package vip.jokor.im.pages.util.article;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import vip.jokor.im.R;
import vip.jokor.im.util.base.SizeUtil;

import java.util.List;

public class PhotoAdapter extends BaseQuickAdapter<Uri, BaseViewHolder> {

    public PhotoAdapter(int layoutResId, @Nullable List<Uri> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Uri item) {
        ImageView img = (ImageView) helper.getView(R.id.img);

        if (item.getPath().equals("new")){
            img.setBackground(mContext.getDrawable(R.drawable.bg_circle_grey));
            helper.getView(R.id.del).setVisibility(View.GONE);
            RequestOptions options1 = new RequestOptions()
                    .override(SizeUtil.dip2px(mContext,30),SizeUtil.dip2px(mContext,30))
                    .error(R.mipmap.img_error)//加载错误之后的错误图
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);//只缓存最终的图片
            Glide.with(mContext).load(R.drawable.camera).apply(options1).into(img);
        }else {
            img.setBackground(mContext.getDrawable(R.drawable.bg_circle_grey));
            RequestOptions options1 = new RequestOptions()
                    .error(R.mipmap.img_error)//加载错误之后的错误图
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);//只缓存最终的图片
            Glide.with(mContext).load(item).apply(options1).into(img);
            helper.getView(R.id.del).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData().remove(item);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
