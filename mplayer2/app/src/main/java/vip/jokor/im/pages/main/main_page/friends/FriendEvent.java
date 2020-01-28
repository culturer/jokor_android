package vip.jokor.im.pages.main.main_page.friends;

import vip.jokor.im.model.bean.FriendsBean;

public class FriendEvent {

    String tag ;
    FriendsBean friendsBean ;

    public FriendEvent(String tag, FriendsBean friendsBean) {
        this.tag = tag;
        this.friendsBean = friendsBean;
    }
}
