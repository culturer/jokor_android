package vip.jokor.im.base;


import android.util.Log;

import com.google.gson.Gson;
import vip.jokor.im.model.bean.FriendsArticlesBean;
import vip.jokor.im.model.bean.GetFriendBean;
import vip.jokor.im.model.bean.GroupBean;
import vip.jokor.im.model.bean.UserBean;
import vip.jokor.im.pages.main.main_page.square.circle.circle_data.CircleData;
import vip.jokor.im.util.base.FileUtil;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.util.base.PreferenceUtil;
import com.kymjs.rxvolley.client.ProgressListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//全局数据缓存
public class Datas {

	private static String TAG = "Datas";

	private static final String USERINFO_KEY = "userinfo";
	private static final String IS_AUTOLOGIN = "is_autologin";
	//用户自己的信息
	private static UserBean user = null;
	//是否自动登录
	private static boolean autoLogin = false;
	//好友列表缓存
	private static GetFriendBean friendBean ;

	private static List<GroupBean> groups = new ArrayList<>();

	private static List<FriendsArticlesBean.ArticlesBean> friends_articles;

	private static List<FriendsArticlesBean.ArticlesBean> self_articles;

	private static List<FriendsArticlesBean.ArticlesBean> recommends;

	private static List<CircleData> circles = new ArrayList<>();

	public static boolean isAutoLogin() {
		autoLogin = PreferenceUtil.getBoolean(IS_AUTOLOGIN,false);
		return autoLogin;
	}


	public static UserBean getUserInfo(){
		if (user == null) {
			Gson gson = GsonUtil.getGson();
			String str = PreferenceUtil.getString(USERINFO_KEY,"");
			Log.i(TAG, "getUserInfo: "+str);
			if (!str.equals("")){
				user = gson.fromJson(str, UserBean.class);
			}
		}
		return user;
	}

	public static void saveUserInfo(String userinfo){
		Log.i(TAG, "saveUserInfo: "+userinfo);
		Gson gson = GsonUtil.getGson();
		user = gson.fromJson(userinfo, UserBean.class);
		PreferenceUtil.commitString(USERINFO_KEY,userinfo);
		setAutoLogin(true);
	}

	public static void setAutoLogin(boolean is){
		autoLogin = is;
		PreferenceUtil.commitBoolean(IS_AUTOLOGIN ,is);
	}

	public static String getLogo(){
		String url = Datas.getUserInfo().getIcon();
		String[] slice = url.split("/");
		String name = slice[slice.length-1];
		Log.i(TAG, "downLogo: "+name);
		File file = new File(FileUtil.ICON_PATH+"/"+name);
		if (file.exists()){
			Log.i(TAG, "getLogo: "+file.getAbsolutePath());
			return file.getAbsolutePath();
		}else {
			ProgressListener progressListener = new ProgressListener() {
				@Override
				public void onProgress(long transferredBytes, long totalSize) {
					Log.i(TAG, "downLogo: "+transferredBytes+"/"+totalSize);
				}
			};
			FileUtil.downLogo(url,progressListener,null);
			return url;
		}
	}

	public static GetFriendBean getFriendBean() {
		return friendBean;
	}

	public static void setFriendBean(GetFriendBean friendBean) {
		Datas.friendBean = friendBean;
	}

	public static List<GroupBean> getGroups() {
		return groups;
	}

	public static void setGroups(List<GroupBean> groups) {
		Datas.groups = groups;
	}

	public static List<FriendsArticlesBean.ArticlesBean> getFriends_articles() {
		return friends_articles;
	}

	public static void setFriends_articles(List<FriendsArticlesBean.ArticlesBean> friends_articles) {
		Datas.friends_articles = friends_articles;
	}

	public static List<FriendsArticlesBean.ArticlesBean> getRecommends() {
		return recommends;
	}

	public static void setRecommends(List<FriendsArticlesBean.ArticlesBean> recommends) {
		Datas.recommends = recommends;
	}

	public static List<FriendsArticlesBean.ArticlesBean> getSelf_articles() {
		return self_articles;
	}

	public static void setSelf_articles(List<FriendsArticlesBean.ArticlesBean> self_articles) {
		Datas.self_articles = self_articles;
	}

	public static List<CircleData> getCircles() {
		return circles;
	}

	public static void setCircles(List<CircleData> circles) {
		Datas.circles = circles;
	}
}
