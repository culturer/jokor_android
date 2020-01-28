package vip.jokor.im.pages.main.main_page.friends.adapter;

import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import vip.jokor.im.pages.main.main_page.friends.adapter.provider.FirstProvider;
import vip.jokor.im.pages.main.main_page.friends.adapter.provider.SecondProvider;
import vip.jokor.im.pages.main.main_page.friends.adapter.tree.FirstNode;
import vip.jokor.im.pages.main.main_page.friends.adapter.tree.SecondNode;

public class NodeTreeAdapter extends BaseNodeAdapter {

    public NodeTreeAdapter() {
        super();
        addNodeProvider(new FirstProvider());
        addNodeProvider(new SecondProvider());
    }

    @Override
    protected int getItemType(@NotNull List<? extends BaseNode> data, int position) {
        BaseNode node = data.get(position);
        if (node instanceof FirstNode) {
            return 1;
        } else if (node instanceof SecondNode) {
            return 2;
        }
        return -1;
    }

    public static final int EXPAND_COLLAPSE_PAYLOAD = 110;
}
