package com.jokor.base.pages.main.main_page.square.actives;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jokor.base.R;

import java.util.List;

public class ActiveAdapter extends BaseQuickAdapter<ActiveItem,ActiverHolder> {

    public ActiveAdapter(int layoutResId, @Nullable List<ActiveItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull ActiverHolder helper, ActiveItem item) {
        Glide.with(mContext).load(item.getDefaulticon()).into((ImageView) helper.getView(R.id.img));

    }

}
