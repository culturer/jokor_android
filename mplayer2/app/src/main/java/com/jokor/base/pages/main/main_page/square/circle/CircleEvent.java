package com.jokor.base.pages.main.main_page.square.circle;

import com.jokor.base.pages.main.main_page.square.circle.circle_data.CircleData;

public class CircleEvent {

    public static final int OPTION_ADD = 0;
    public static final int OPTION_UPDATE = 1;

    String tag;
    int option;
    CircleData data;

    public CircleEvent(String tag, int option, CircleData data) {
        this.tag = tag;
        this.option = option;
        this.data = data;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public CircleData getData() {
        return data;
    }

    public void setData(CircleData data) {
        this.data = data;
    }
}
