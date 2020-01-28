package vip.jokor.im.pages.main.main_page.group;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import vip.jokor.im.R;
import vip.jokor.im.model.bean.GroupBean;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.util.base.SizeUtil;
import vip.jokor.im.util.base.StatusBarUtil;
import vip.jokor.im.wedgit.util.ShowUtil;
import vip.jokor.im.wedgit.util.UcropUtil;
import com.kymjs.rxvolley.client.HttpCallback;
import com.yalantis.ucrop.UCrop;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CreateGroupActivity extends AppCompatActivity {

	private static final String TAG = "CreateGroupActivity";

	private String icon_url = "";
	private int count = 100;
	private EditText group_name;


	RequestOptions options = new RequestOptions()
//			.placeholder(R.mipmap.img_error)//加载成功之前占位图
			.error(R.mipmap.img_error)//加载错误之后的错误图
			.override(400,400)//指定图片的尺寸
//			.fitCenter()//指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
//			.centerCrop()//指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
			.circleCrop()//指定图片的缩放类型为centerCrop （圆形）
//　　                .skipMemoryCache(true)//跳过内存缓存
//　　                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存所有版本的图像
//　　                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
//　　                .diskCacheStrategy(DiskCacheStrategy.DATA)//只缓存原来分辨率的图片
//			.transform(new GlideRoundTransform(this, 8))
			.diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group);
		initToolBar();
		initBaseView();
		initImg();
	}
	
	private void initBaseView(){
		group_name = findViewById(R.id.group_name);
		Spinner group_max_num = findViewById(R.id.group_max_num);
		String[] strSpinnerItems = new String[]{
				"100人","500人(VIP)","10000+"
		};
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,R.layout.appoint_select,strSpinnerItems);
		spinnerAdapter.setDropDownViewResource(R.layout.appoint_item);
		group_max_num.setAdapter(spinnerAdapter);
		group_max_num.setSelection(0);
		group_max_num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position){
					case 0:
						count = 100;
						break;
					case 1:
						count = 500;
						ShowUtil.showToast(CreateGroupActivity.this,"内测阶段非VIP也可以创建哦！");
						break;
					case 2:
						ShowUtil.showToast(CreateGroupActivity.this,"敬请期待！");
						break;
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				count = 100;
			}
		});
	}
	
	private void initImg(){
		Widget widget = Widget.newDarkBuilder(CreateGroupActivity.this)
				.title("选择头像") // Title.
				.statusBarColor(getResources().getColor(R.color.black,null)) // StatusBar color.
				.toolBarColor(getResources().getColor(R.color.black,null)) // Toolbar color.
				.navigationBarColor(getResources().getColor(R.color.black,null)) // Virtual NavigationBar color of Android5.0+.
				.mediaItemCheckSelector(getResources().getColor(R.color.grey_dark,null), getResources().getColor(R.color.black,null)) // Image or video selection box.
				.bucketItemCheckSelector(Color.RED, Color.YELLOW) // Select the folder selection box.
				.buttonStyle (// Used to configure the style of button when the image/video is not found.
						Widget.ButtonStyle.newLightBuilder(CreateGroupActivity.this) // With Widget's Builder model.
								.setButtonSelector(Color.WHITE, Color.WHITE) // Button selector.
								.build()
				)
				.build();
		ImageView group_icon = findViewById(R.id.group_icon);
		group_icon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Album.image(CreateGroupActivity.this) // 选择图片。
						.multipleChoice()
						.widget(widget)
						.camera(true)
						.columnCount(2)
						.selectCount(1)
						.checkedList(new ArrayList<AlbumFile>())
						.filterSize(null)
						.filterMimeType(null)
						.afterFilterVisibility(false) // 显示被过滤掉的文件，但它们是不可用的。
						.onResult(new Action<ArrayList<AlbumFile>>() {
							@Override
							public void onAction(@NonNull ArrayList<AlbumFile> result) {
								String ImgPth = UcropUtil.startUCrop(
										CreateGroupActivity.this,
										result.get(0).getPath(),
										0x01,
										1,
										1);
								Glide.with(CreateGroupActivity.this)
										.load(ImgPth)
										.apply(options)
										.into(group_icon);

							}
						})
						.onCancel(new Action<String>() {
							@Override
							public void onAction(@NonNull String result) {
							
							}
						})
						.start();
			}
		});
	}
	
	private void initToolBar(){
		View toolbar = findViewById(R.id.toolbar);
        toolbar.setPadding(0, SizeUtil.getStatusBarHeight(this),0,0);
		StatusBarUtil.setAndroidNativeLightStatusBar(this,true);
		findViewById(R.id.left_menu).setOnClickListener(v -> finish());
		findViewById(R.id.right_menu).setOnClickListener(v -> ok());
	}

	private void ok(){
		HttpCallback callback = new HttpCallback() {
			@Override
			public void onSuccess(String t) {
				Log.i(TAG, "onSuccess: "+t);
                try {
                    JSONObject jb = new JSONObject(t);
                    int status = jb.getInt("status");
                    if (status == 200){
                        GroupBean groupBean = GsonUtil.getGson().fromJson(jb.getString("group"),GroupBean.class);
						Log.i(TAG, "send create group msg: "+GsonUtil.getGson().toJson(groupBean));
                        GroupEvent event = new GroupEvent(TAG,groupBean);
                        EventBus.getDefault().postSticky(event);
                        ShowUtil.showToast(getApplicationContext(),"群聊 [ "+group_name.getText().toString()+" ] 创建成功");
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

			@Override
			public void onFailure(int errorNo, String strMsg) {
				Log.i(TAG, "onFailure: "+errorNo+","+strMsg);
			}
		};
		GroupPresenter.getInstance().createGroup(icon_url,group_name.getText().toString(),count,callback);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
			final Uri resultUri = UCrop.getOutput(data);
		} else if (resultCode == UCrop.RESULT_ERROR) {
			final Throwable cropError = UCrop.getError(data);
		}
	}
}
