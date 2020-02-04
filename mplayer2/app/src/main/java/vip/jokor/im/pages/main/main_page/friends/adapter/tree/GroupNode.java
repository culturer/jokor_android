package vip.jokor.im.pages.main.main_page.friends.adapter.tree;

import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GroupNode extends BaseExpandNode {

    public List<BaseNode> groups;

    public List<BaseNode> getGroups() {
        return groups;
    }

    public void setGroups(List<BaseNode> groups) {
        this.groups = groups;
        setExpanded(false);
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return groups;
    }
}
