package vip.jokor.im.pages.main.main_page.friends;

public class DelFriendEvent {
    private long userId;

    public DelFriendEvent(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
