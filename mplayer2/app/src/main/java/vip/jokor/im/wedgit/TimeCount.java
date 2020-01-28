package vip.jokor.im.wedgit;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/12/1 0001.
 */

//计时器，用作短信验证码倒计时
//timeCount = new TimeCount(60000, 1000, btnCode);
//
//		在
//
//public void onViewClicked(View view) 方法中调用
//		timeCount.start();
//		在点击登录的时候取消操作
////取消倒计时
//		timeCount.cancel();
//		timeCount.onFinish();
//
public class TimeCount extends CountDownTimer {

	private TextView btn_count;

	public TimeCount(long millisInFuture, long countDownInterval, TextView btn_count) {
		super(millisInFuture, countDownInterval);
		this.btn_count = btn_count;
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onTick(long millisUntilFinished) {
		btn_count.setEnabled(false);
		btn_count.setText(millisUntilFinished / 1000 + "秒");
	}

	@Override
	public void onFinish() {
		btn_count.setEnabled(true);
		btn_count.setText("获取");

	}
}
