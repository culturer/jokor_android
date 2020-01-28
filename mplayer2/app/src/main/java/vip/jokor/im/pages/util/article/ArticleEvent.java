package vip.jokor.im.pages.util.article;

import vip.jokor.im.model.bean.FriendsArticlesBean;

public class ArticleEvent {

    public static final int ADD = 0;

    private String tag ;
    private int option ;
    private long circleId;
    private FriendsArticlesBean.ArticlesBean data;

    public ArticleEvent(String tag, int option,long circleId, FriendsArticlesBean.ArticlesBean data) {
        this.tag = tag;
        this.option = option;
        this.circleId = circleId;
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

    public FriendsArticlesBean.ArticlesBean getData() {
        return data;
    }

    public void setData(FriendsArticlesBean.ArticlesBean data) {
        this.data = data;
    }

    public long getCircleIds() {
        return circleId;
    }

    public void setCircleIds(long circleId) {
        this.circleId = circleId;
    }
}
