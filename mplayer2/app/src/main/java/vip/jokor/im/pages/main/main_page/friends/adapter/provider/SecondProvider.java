package vip.jokor.im.pages.main.main_page.friends.adapter.provider;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vip.jokor.im.R;
import vip.jokor.im.model.bean.FriendsBean;
import vip.jokor.im.pages.main.main_page.friends.adapter.tree.SecondNode;
import vip.jokor.im.presenter.MainPresenter;

public class SecondProvider extends BaseNodeProvider {

    RequestOptions options = new RequestOptions()
//				.error(R.mipmap.logo)//加载错误之后的错误图
            .override(400,400)//指定图片的尺寸
//						.fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
//					.centerCrop()//指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
            .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
//　　                .skipMemoryCache(true)//跳过内存缓存
//　　                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存所有版本的图像
//　　                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
//　　                .diskCacheStrategy(DiskCacheStrategy.DATA)//只缓存原来分辨率的图片
//                .transform(new GlideRoundTransform(context, 8))
            .optionalCircleCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

    @Override
    public int getItemViewType() {
        return 2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.iphonetreeview_list_item_view;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable BaseNode data) {
        FriendsBean entity = (FriendsBean) data;

        String name = "";
        if (entity.getMsg()==null || entity.getMsg().equals("")){
            name = entity.getFriend().getUserName();
        }else {
            name = entity.getMsg();
        }
        helper.setText(R.id.contact_list_item_name, name);

        Glide.with(context)
                .load(entity.getFriend().getIcon())
                .apply(options)
                .into((ImageView) helper.getView(R.id.icon));

        helper.setText(R.id.cpntact_list_item_state,entity.getFriend().getSign());
        helper.setText(R.id.label,"vip");

    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        FriendsBean entity = (FriendsBean) data;
        MainPresenter.getInstance().openP2PSession(entity,getContext(),false);
    }
}
