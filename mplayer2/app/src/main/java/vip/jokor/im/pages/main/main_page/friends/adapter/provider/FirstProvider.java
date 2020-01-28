package vip.jokor.im.pages.main.main_page.friends.adapter.provider;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import vip.jokor.im.R;
import vip.jokor.im.pages.main.main_page.friends.adapter.NodeTreeAdapter;
import vip.jokor.im.pages.main.main_page.friends.adapter.tree.FirstNode;

public class FirstProvider extends BaseNodeProvider {

    @Override
    public int getItemViewType() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.iphonetreeview_list_head_view;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable BaseNode data) {

//        FirstNode entity = (FirstNode) data;
//        helper.setText(R.id.title, entity.getTitle());
//        helper.setImageResource(R.id.iv, R.drawable.arrow_r);
//
        helper.setImageResource(R.id.group_indicator,R.drawable.ic_chevron_right_black_24dp);
        helper.setText(R.id.group_name,"分类标题");
        helper.setText(R.id.online_count,"5");

        setArrowSpin(helper, data, false);

    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable BaseNode data, @NotNull List<?> payloads) {
        for (Object payload : payloads) {
            if (payload instanceof Integer && (int) payload == NodeTreeAdapter.EXPAND_COLLAPSE_PAYLOAD) {
                // 增量刷新，使用动画变化箭头
                setArrowSpin(helper, data, true);
            }
        }
    }

    private void setArrowSpin(BaseViewHolder helper, BaseNode data, boolean isAnimate) {
        FirstNode entity = (FirstNode) data;
        ImageView imageView = helper.getView(R.id.group_indicator);

        if (entity.isExpanded()) {
            if (isAnimate) {
                ViewCompat.animate(imageView).setDuration(200)
                        .setInterpolator(new DecelerateInterpolator())
                        .rotation(90f)
                        .start();
            } else {
                imageView.setRotation(90f);
            }
        } else {
            if (isAnimate) {
                ViewCompat.animate(imageView).setDuration(200)
                        .setInterpolator(new DecelerateInterpolator())
                        .rotation(0f)
                        .start();
            } else {
                imageView.setRotation(0f);
            }
        }
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        // 这里使用payload进行增量刷新（避免整个item刷新导致的闪烁，不自然）
        getAdapter().expandOrCollapse(position, true, true, NodeTreeAdapter.EXPAND_COLLAPSE_PAYLOAD);
    }
}
