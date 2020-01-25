package com.jokor.base.util.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//解决时间解析的问题
public class GsonUtil {
    public static Gson getGson(){
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }
}
