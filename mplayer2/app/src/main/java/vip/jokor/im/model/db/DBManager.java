package vip.jokor.im.model.db;

import vip.jokor.im.base.BaseApplication;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class DBManager {


    private static final DBManager ourInstance = new DBManager();
    private DBManager() { }
    public static DBManager getInstance() { return ourInstance; }

    private BoxStore boxStore;
    private Box<Session> sessionBox;
    private Box<Msg> msgBox;

    public void init(BaseApplication application){
        ourInstance.boxStore =  application.getBoxStore();
    }

    public Box<Session> getSessionBox() {
        if (sessionBox == null)sessionBox = boxStore.boxFor(Session.class);
        return sessionBox;
    }

    public Box<Msg> getMsgBox() {
        if (msgBox == null)msgBox = boxStore.boxFor(Msg.class);
        return msgBox;
    }
}
