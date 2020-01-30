package vip.jokor.im.base;

import vip.jokor.im.util.base.PreferenceUtil;

public class Config {

    //聊天消息隔多久显示时间
    public static final int chat_time_dur_show = 60*1000;
    public static final int page_size = 50;

    public static boolean isShowTab0(){
        return PreferenceUtil.getBoolean("show_tab_0",false);
    }

    public static boolean isShowTab1(){
        return PreferenceUtil.getBoolean("show_tab_1",false);
    }

    public static boolean isShowTab2(){
        return PreferenceUtil.getBoolean("show_tab_2",false);
    }

    public static void setTab0(boolean isShow){
        PreferenceUtil.commitBoolean("show_tab_0",isShow);
    }

    public static void setTab1(boolean isShow){
        PreferenceUtil.commitBoolean("show_tab_1",isShow);
    }

    public static void setTab2(boolean isShow){
        PreferenceUtil.commitBoolean("show_tab_2",isShow);
    }
}
