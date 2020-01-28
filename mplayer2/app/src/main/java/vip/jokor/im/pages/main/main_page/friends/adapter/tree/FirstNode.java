package vip.jokor.im.pages.main.main_page.friends.adapter.tree;

import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import vip.jokor.im.model.bean.CategorysBean;
import vip.jokor.im.model.bean.FriendsBean;

public class FirstNode extends BaseExpandNode {

    public CategorysBean categorysBean;
    public List<BaseNode> friends;

    public FirstNode(List<BaseNode> friends,CategorysBean categorysBean) {

        this.categorysBean = categorysBean;
        this.friends = friends;

        setExpanded(false);
    }

    public String getTitle() {
        return categorysBean.getName();
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return friends;
    }

}
