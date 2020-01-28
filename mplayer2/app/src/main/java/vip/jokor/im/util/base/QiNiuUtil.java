package vip.jokor.im.util.base;

import android.util.Log;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import java.io.File;

import static vip.jokor.im.base.Urls.CODE_GET_7NIUTOKEN;
import static vip.jokor.im.base.Urls.CONFIG_PATH;
import static vip.jokor.im.base.Urls.HOST_DATA;

public class QiNiuUtil {

	private static final String TAG = "QiNiuUtil" ;

	private static QiNiuUtil instance;
	UploadManager uploadManager;

	private volatile boolean isCancelled = false;


	private QiNiuUtil() {

	}

	public static QiNiuUtil getInstance(){
		if (instance == null){
			instance = new QiNiuUtil();
		}
		if (instance.uploadManager == null){
			instance.init();
		}
		return instance;
	}

	private void init(){
		Configuration config = new Configuration.Builder()
				.chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
				.putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
				.connectTimeout(10)           // 链接超时。默认10秒
				.useHttps(false)               // 是否使用https上传域名
				.responseTimeout(60)          // 服务器响应超时。默认60秒
//				.recorder(recorder)           // recorder分片上传时，已上传片记录器。默认null
//				.recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
				.zone(FixedZone.zone2)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
				.build();
// 重用uploadManager。一般地，只需要创建一个uploadManager对象
		uploadManager = new UploadManager(config);
	}

	public void  upload(String token,String path, String key, UpCompletionHandler handler, UploadOptions options){
		UpCompletionHandler handler1;
		if (handler == null){
			 handler1 = (key1, info, res) -> {
				 //res包含hash、key等信息，具体字段取决于上传策略的设置
				 if(info.isOK()) {
					 Log.i(TAG, "Upload Success");
				 } else {
					 Log.i(TAG, "Upload Fail");
					 //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
				 }
				 Log.i(TAG, key1 + ",\r\n " + info + ",\r\n " + res);
			 };
		}else {
			handler1 = handler;
		}
		UploadOptions uploadOptions;
		UpCancellationSignal cancelSingal = new UpCancellationSignal(){
			public boolean isCancelled(){
				return isCancelled;
			}
		};
		if (options == null){
			uploadOptions = new UploadOptions(null, null, false, new UpProgressHandler(){
						public void progress(String key, double percent){
							Log.i(TAG, key + ": " + percent);
						}}, cancelSingal);
		}else {
			uploadOptions = options;
		}
		uploadManager.put(path, key, token , handler1, uploadOptions);
	}

	public void  upload(String token, File file, String key, UpCompletionHandler handler, UploadOptions options){
		UpCompletionHandler handler1;
		if (handler == null){
			handler1 = (key1, info, res) -> {
				//res包含hash、key等信息，具体字段取决于上传策略的设置
				if(info.isOK()) {
					Log.i(TAG, "Upload Success");
				} else {
					Log.i(TAG, "Upload Fail");
					//如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
				}
				Log.i(TAG, key1 + ",\r\n " + info + ",\r\n " + res);
			};
		}else {
			handler1 = handler;
		}
		UploadOptions uploadOptions;
		UpCancellationSignal cancelSingal = new UpCancellationSignal(){
			public boolean isCancelled(){
				return isCancelled;
			}
		};
		if (options == null){
			uploadOptions = new UploadOptions(null, null, false, new UpProgressHandler(){
				public void progress(String key, double percent){
					Log.i(TAG, key + ": " + percent);
				}}, cancelSingal);
		}else {
			uploadOptions = options;
		}
		uploadManager.put(file, key, token , handler1, uploadOptions);
	}

	//取消上传
	public void cancel() {
		isCancelled = true;
	}


	public void getToken(HttpCallback callback){
		HttpParams params = new HttpParams();
		params.put("options",CODE_GET_7NIUTOKEN);
		new RxVolley.Builder()
				.url(HOST_DATA+CONFIG_PATH)
				.httpMethod(RxVolley.Method.POST) //default GET or POST/PUT/DELETE/HEAD/OPTIONS/TRACE/PATCH
				.contentType(RxVolley.ContentType.FORM)//default FORM or JSON
				.params(params)
				.shouldCache(false) //default: get true, post false
				.callback(callback)
				.encoding("UTF-8") //default
				.doTask();
	}

}
