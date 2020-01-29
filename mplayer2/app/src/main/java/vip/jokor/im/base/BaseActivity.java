package vip.jokor.im.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.umeng.message.PushAgent;

import vip.jokor.im.im.MsgService;
import vip.jokor.im.util.base.ServiceUtil;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!ServiceUtil.isServiceRunning(this, "MsgService") && ServiceUtil.isOnForground(this)) {
            startService(new Intent(this, MsgService.class));
        }
    }
}


