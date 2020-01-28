package vip.jokor.im.model.bean;

public class ArticleBean {

    /**
     * Id : 1
     * BelongId : 2
     * Icon : https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3276179142,1686381254&fm=26&gp=0.jpg
     * Username : 闪电侠
     * Imgs : wer
     * Video : qefwe
     * Content : 测试内容
     * CommentCount : 10
     * LikeCount : 100
     * IsInvalid : false
     * CreateTime : 2019-11-19 14:16:33
     */

    private long Id;
    private long BelongId;
    private String Icon;
    private String Username;
    private String Imgs;
    private String Video;
    private String Content;
    private int CommentCount;
    private int LikeCount;
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

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getImgs() {
        return Imgs;
    }

    public void setImgs(String Imgs) {
        this.Imgs = Imgs;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String Video) {
        this.Video = Video;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public int getCommentCount() {
        return CommentCount;
    }

    public void setCommentCount(int CommentCount) {
        this.CommentCount = CommentCount;
    }

    public int getLikeCount() {
        return LikeCount;
    }

    public void setLikeCount(int LikeCount) {
        this.LikeCount = LikeCount;
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
