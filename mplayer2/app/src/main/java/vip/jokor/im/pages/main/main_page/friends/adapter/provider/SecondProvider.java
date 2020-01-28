package vip.jokor.im.pages.main.main_page.friends.adapter.provider;

import android.view.View;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vip.jokor.im.R;
import vip.jokor.im.pages.main.main_page.friends.adapter.tree.SecondNode;

public class SecondProvider extends BaseNodeProvider {

    @Override
    public int getItemViewType() {
        return 2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_node_second;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable BaseNode data) {
        SecondNode entity = (SecondNode) data;
        helper.setText(R.id.title, entity.getTitle());

        if (entity.isExpanded()) {
            helper.setImageResource(R.id.iv, R.drawable.arrow_b);
        } else {
            helper.setImageResource(R.id.iv, R.drawable.arrow_r);
        }
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        SecondNode entity = (SecondNode) data;
        if (entity.isExpanded()) {
            getAdapter().collapse(position);
        } else {
            getAdapter().expandAndCollapseOther(position);
        }
    }
}
