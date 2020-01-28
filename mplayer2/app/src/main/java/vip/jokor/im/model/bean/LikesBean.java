package vip.jokor.im.model.bean;

public class LikesBean {
    /**
     * Id : 1
     * BelongId : 1
     * Username : 分为人
     * Icon :
     * ArticleId : 1
     * CreateTime : 2019-11-19 14:17:32
     */

    private long Id;
    private long BelongId;
    private String Username;
    private String Icon;
    private long ArticleId;
    private String CreateTime;

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public long getBelongId() {
        return BelongId;
    }

    public void setBelongId(long BelongId) {
        this.BelongId = BelongId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

    public long getArticleId() {
        return ArticleId;
    }

    public void setArticleId(long ArticleId) {
        this.ArticleId = ArticleId;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}