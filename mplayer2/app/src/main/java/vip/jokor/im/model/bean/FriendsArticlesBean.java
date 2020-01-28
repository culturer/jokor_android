package vip.jokor.im.model.bean;

import java.util.List;

public class FriendsArticlesBean {


    /**
     * articles : [{"Article":{"Id":1,"BelongId":2,"Imgs":"wer","Video":"qefwe","Content":"测试内容","CommentCount":10,"LikeCount":100,"IsInvalid":false,"CreateTime":"2019-11-19 14:16:33"},"Comments":[{"Id":1,"BelongId":1,"Username":"测试用户","Icon":"jfo","ArticleId":1,"Content":"待会发","CreateTime":"2019-11-19 14:17:22"}],"Likes":[{"Id":1,"BelongId":1,"Username":"分为人","Icon":"","ArticleId":1,"CreateTime":"2019-11-19 14:17:32"}]}]
     * status : 200
     * time : 2019-11-19 19:12:55
     */

    private int status;
    private String time;
    private List<ArticlesBean> articles;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<ArticlesBean> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticlesBean> articles) {
        this.articles = articles;
    }

    public static class ArticlesBean {

        private ArticleBean Article;
        private List<CommentsBean> Comments;
        private List<LikesBean> Likes;
        private List<ReplysBean> Replys;

        public ArticleBean getArticle() {
            return Article;
        }

        public void setArticle(ArticleBean Article) {
            this.Article = Article;
        }

        public List<CommentsBean> getComments() {
            return Comments;
        }

        public void setComments(List<CommentsBean> Comments) {
            this.Comments = Comments;
        }

        public List<LikesBean> getLikes() {
            return Likes;
        }

        public void setLikes(List<LikesBean> Likes) {
            this.Likes = Likes;
        }

        public List<ReplysBean> getReplys() {
            return Replys;
        }

        public void setReplys(List<ReplysBean> replys) {
            Replys = replys;
        }
    }
}
