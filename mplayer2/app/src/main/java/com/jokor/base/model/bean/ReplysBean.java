package com.jokor.base.model.bean;

public class ReplysBean {

    /**
     * Id : 1
     * BelongId : 1
     * Username : 管理员账户
     * Icon : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1567921572660&di=fd7d5e447ccbf1d270962801e37c97ee&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201510%2F13%2F20151013234603_hiYnP.jpeg
     * ArticleId : 17
     * CommentId : 1
     * Content : 测试回复
     * IsInvalid : false
     * CreateTime : 2019-11-25 11:36:00
     */

    private long Id;
    private long BelongId;
    private String Username;
    private String Icon;
    private long ArticleId;
    private long CommentId;
    private String Content;
    private boolean IsInvalid;
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

    public long getCommentId() {
        return CommentId;
    }

    public void setCommentId(long CommentId) {
        this.CommentId = CommentId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public boolean isIsInvalid() {
        return IsInvalid;
    }

    public void setIsInvalid(boolean IsInvalid) {
        this.IsInvalid = IsInvalid;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}
