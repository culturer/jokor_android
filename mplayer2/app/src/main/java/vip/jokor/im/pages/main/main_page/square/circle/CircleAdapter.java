package vip.jokor.im.pages.main.main_page.square.circle;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import vip.jokor.im.R;
import vip.jokor.im.pages.main.main_page.square.circle.circle_data.CircleData;
import vip.jokor.im.util.glide.GlideRoundTransform;

import java.util.List;

public class CircleAdapter extends BaseQuickAdapter<CircleData, BaseViewHolder> {


    public CircleAdapter(int layoutResId, @Nullable List<CircleData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CircleData item) {
        ImageView icon = helper.getView(R.id.icon);
        TextView name = helper.getView(R.id.name);
        TextView desc = helper.getView(R.id.desc);
        TextView grad = helper.getView(R.id.grad);
        RequestOptions options = new RequestOptions()
                .error(R.mipmap.img_error)//加载错误之后的错误图
                .transform(new GlideRoundTransform(getContext(), 4))
                .diskCacheStrategy(DiskCacheStrategy.ALL);//只缓存最终的图片
        Glide.with(getContext())
                .load(item.getCircle().getIcon())
                .apply(options)
                .into(icon);
        name.setText(item.getCircle().getName());
        desc.setText(item.getCircle().getMsg());
        grad.setText(""+item.getCircleUser().getCircleGrad()+"级");
    }


}
