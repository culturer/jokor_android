package vip.jokor.im.im;

import vip.jokor.im.model.db.Msg;

public class MsgEvent {

    private String tag;
    Msg msg;

    public MsgEvent(String tag, Msg msg) {
        this.tag = tag;
        this.msg = msg;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
}
