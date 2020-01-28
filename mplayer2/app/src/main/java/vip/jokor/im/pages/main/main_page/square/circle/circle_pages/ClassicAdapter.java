package vip.jokor.im.pages.main.main_page.square.circle.circle_pages;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import vip.jokor.im.R;
import vip.jokor.im.model.bean.FriendsArticlesBean;
import vip.jokor.im.util.glide.MediaLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

public class ClassicAdapter extends BaseQuickAdapter<FriendsArticlesBean.ArticlesBean, BaseViewHolder> {

    public ClassicAdapter(int layoutResId, @Nullable List<FriendsArticlesBean.ArticlesBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FriendsArticlesBean.ArticlesBean item) {
        Banner banner = helper.getView(R.id.banner);
        setBanner(banner);
    }

    private void setBanner(Banner banner){
        List<Integer> images = new ArrayList<Integer>(){
            {
                this.add(R.mipmap.p1);
                this.add(R.mipmap.p10);
                this.add(R.mipmap.p11);
            }
        };
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

            });
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }
    }

}
