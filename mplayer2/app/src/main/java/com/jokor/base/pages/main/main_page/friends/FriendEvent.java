package com.jokor.base.pages.main.main_page.friends;

import com.jokor.base.model.bean.FriendsBean;

public class FriendEvent {

    String tag ;
    FriendsBean friendsBean ;

    public FriendEvent(String tag, FriendsBean friendsBean) {
        this.tag = tag;
        this.friendsBean = friendsBean;
    }
}
