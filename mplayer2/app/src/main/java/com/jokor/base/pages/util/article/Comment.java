package com.jokor.base.pages.util.article;

import com.jokor.base.model.bean.CommentsBean;
import com.jokor.base.model.bean.ReplysBean;

import java.util.ArrayList;
import java.util.List;

public class Comment {
    CommentsBean commentsBean;
    List<ReplysBean> replys = new ArrayList<>();

    public Comment(CommentsBean commentsBean, List<ReplysBean> replys) {
        this.commentsBean = commentsBean;
        this.replys = replys;
    }

    public CommentsBean getCommentsBean() {
        return commentsBean;
    }

    public void setCommentsBean(CommentsBean commentsBean) {
        this.commentsBean = commentsBean;
    }

    public List<ReplysBean> getReplys() {
        return replys;
    }

    public void setReplys(List<ReplysBean> replys) {
        this.replys = replys;
    }
}
