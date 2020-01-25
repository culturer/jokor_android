package com.jokor.base.pages.util.article;

public class DelEvent {

    public static final int DEL_ARTICLE = 0;
    public static final int DEL_COMMENT = 1;
    public static final int DEL_REPLY = 2;

    int del_type = -1;
    long id;

    public DelEvent(int del_type, long id) {
        this.del_type = del_type;
        this.id = id;
    }

}
