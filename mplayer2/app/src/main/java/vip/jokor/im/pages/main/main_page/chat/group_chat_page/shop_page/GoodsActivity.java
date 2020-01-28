package vip.jokor.im.pages.main.main_page.chat.group_chat_page.shop_page;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import vip.jokor.im.R;


public class GoodsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        initToolBar();
    }

    private void initToolBar(){
        LinearLayout container = findViewById(R.id.container);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
//		toolbar_title.setText(mmGroup.getName()+"("+mmGroup.getCount()+")");
        toolbar_title.setText("测试商品");
//        container.setPadding(0, EncryptUtil.SizeUtil.getStatusBarHeight(this),0,0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodsActivity.this.finish();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}
