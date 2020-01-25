package com.jokor.base.pages.main.main_page.square.actives;

public class ActiveItem {

    private int defaulticon ;
    private String url;
    private String label;
    private String name;

    public ActiveItem(int defaulticon) {
        this.defaulticon = defaulticon;
    }

    public int getDefaulticon() {
        return defaulticon;
    }

    public void setDefaulticon(int defaulticon) {
        this.defaulticon = defaulticon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
