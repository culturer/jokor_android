package vip.jokor.im.base;


//服务器配置文件
public class Urls {

	public static String HOST_DATA = "http://192.168.1.11:10008";
//	public static String HOST_DATA = "http://106.12.98.18:10005";

	public static String QINIU_URL = "http://qiniu.jokor.vip/";
	public static String DEVICE_TOKEN = "";

	public static final String USER_PATH = "/user";
	// user code
	public static final int CODE_LOGIN = 0;
	public static final int CODE_DEL_USER = 9;
	public static final int CODE_ADD_USERLIST = 10;
	public static final int CODE_GET_FRIEND = 11;
	public static final int CODE_ADD_FRIEND = 12;
	public static final int CODE_GET_APPLIES = 13;
	public static final int CODE_GET_USERINFO = 14;
	public static final int CODE_GET_OPTAPPLY = 15;

	public static final String CONFIG_PATH = "/config";

	// config code
	public static final int CODE_GET_7NIUTOKEN = 0;
	public static final int CODE_UPDATE_DEVICETOKEN = 1;

	public static final String GROUP_PATH = "/group";
	// group code
	public static final int CODE_CREATE = 0;
	public static final int CODE_ADD = 1;
	public static final int CODE_SEARCH = 2;
	public static final int CODE_LIST = 6;

	public static final String ARTICLE_PATH = "/article";
	//article code
	public static final int CODE_FRIENDS_ARTICLE = 0;
	public static final int CODE_CREATE_ARTICLE = 1;
	public static final int CODE_ADD_COMMENT = 2;
	public static final int CODE_ADD_LIKE = 3;
	public static final int CODE_DEL_ARTICLE = 4;
	public static final int CODE_ADD_REPLY = 5;

    public static final String RECOMMEND_PATH = "/recommend";
    //recommend code
    public static final int CODE_GET_RECOMMENDS = 0;

	public static final String CIRCLE_PATH = "/circle";
	public static final int CODE_GET_CIRCLE_ARTICLES = 0;
	public static final int CODE_GET_CIRCLE_CLASS_ARTICLES = 1;
	public static final int CODE_GET_CIRCLE_SHOP_DATA = 2;
	public static final int CODE_GET_CIRCLE_INFO = 3;
	public static final int CODE_GET_CIRCLE_USER = 4;
	public static final int CODE_SEARCG_CIRCLE = 5;
	public static final int CODE_CHECK_CIRCLE_NAME = 7;
	public static final int CODE_CREATE_CIRCLE = 8;
    public static final int CODE_GET_CIRCLE_LIST = 9;
	public static final int CODE_SIGN_CIRCLE = 10;
	public static final int CODE_FOCUS_CIRCLE = 11;


}

