package vip.jokor.im.model.db;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Msg {

    public static final int MSG_FROM_FRIEND = 0;
    public static final int MSG_FROM_STRANGER = 1;
    public static final int MSG_FROM_FAMILY = 2;
    public static final int MSG_FROM_GROUP = 3;
    public static final int MSG_FROM_RELATION = 4;

    public static final int MSG_TYPE_AUDIO = 0;
    public static final int MSG_TYPE_VIDEO = 1;
    public static final int MSG_TYPE_TEXT = 2;
    public static final int MSG_TYPE_CONTROL = 3;
    public static final int MSG_TYPE_IMG = 4;

    public static final int MSG_USED_CHAT = 0;
    public static final int MSG_USED_ADD_FRIEND = 1;

    @Id
    private long id;     //为了兼容objectbox
    private int msgUsed ;//消息用途
    private String msgId; //真正的Id
    private int msgFrom;    //消息类型，群聊，单聊等
    private int msgType;    //消息形式，文字，图片，语音，视频
    private long fromId;
    private long toId;
    private String data;
    private Date createTime;
    private int status;
    private String username;
    private String icon;


    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public int getMsgFrom() {
        return msgFrom;
    }

    public void setMsgFrom(int msgFrom) {
        this.msgFrom = msgFrom;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getMsgUsed() {
        return msgUsed;
    }

    public void setMsgUsed(int msgUsed) {
        this.msgUsed = msgUsed;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
