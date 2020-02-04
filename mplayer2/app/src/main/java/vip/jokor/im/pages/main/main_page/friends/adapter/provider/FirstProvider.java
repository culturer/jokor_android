package vip.jokor.im.pages.main.main_page.friends.adapter.provider;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;

import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import vip.jokor.im.R;
import vip.jokor.im.pages.main.main_page.friends.adapter.NodeTreeAdapter;
import vip.jokor.im.pages.main.main_page.friends.adapter.tree.FirstNode;
import vip.jokor.im.pages.main.main_page.friends.adapter.tree.GroupNode;

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

        if (data instanceof FirstNode){
            FirstNode entity = (FirstNode) data;
            helper.setText(R.id.group_name,entity.categorysBean.getName());
        }
        if (data instanceof GroupNode){
            helper.setText(R.id.group_name,"群聊");
        }

        helper.setImageResource(R.id.group_indicator,R.drawable.ic_chevron_right_black_24dp);
        if (data.getChildNode()!=null){
            helper.setText(R.id.online_count,""+data.getChildNode().size());
        }else {
            helper.setText(R.id.online_count,""+0);
        }
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

        ImageView imageView = helper.getView(R.id.group_indicator);
        BaseExpandNode entity = (BaseExpandNode) data;

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
