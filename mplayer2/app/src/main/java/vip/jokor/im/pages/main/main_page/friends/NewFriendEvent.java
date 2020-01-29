package vip.jokor.im.pages.main.main_page.friends;

public class NewFriendEvent {
    private String username;
    private long userId;

    public NewFriendEvent(String username, long userId) {
        this.username = username;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public long getUserId() {
        return userId;
    }
}
