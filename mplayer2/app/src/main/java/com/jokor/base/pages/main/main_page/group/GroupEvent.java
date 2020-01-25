package com.jokor.base.pages.main.main_page.group;

import com.jokor.base.model.bean.GroupBean;

public class GroupEvent {

    private String tag ;
    private GroupBean groupBean ;

    public GroupEvent(String tag, GroupBean groupBean) {
        this.tag = tag;
        this.groupBean = groupBean;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public GroupBean getGroupBean() {
        return groupBean;
    }

    public void setGroupBean(GroupBean groupBean) {
        this.groupBean = groupBean;
    }
}
