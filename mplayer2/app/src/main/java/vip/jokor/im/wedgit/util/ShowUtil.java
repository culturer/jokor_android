package vip.jokor.im.wedgit.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;

import com.google.android.material.snackbar.Snackbar;

import vip.jokor.im.R;

public class ShowUtil {

    //显示菜单弹框
    public static void showPopupMenu(Context context, View view,int menu,PopupMenu.OnMenuItemClickListener itemClickListener,PopupMenu.OnDismissListener dismissListener) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(context, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(menu, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(itemClickListener);
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(dismissListener);
        popupMenu.show();
    }

    //显示底部弹框
    public static void showBottomPop(Context context,int popLayout,View showAt,int positon) {
        //要在布局中显示的布局
        View contentView = LayoutInflater.from(context).inflate(popLayout, null, false);
        //实例化PopupWindow并设置宽高
        PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //点击外部消失，这里因为PopupWindow填充了整个窗口，所以这句代码就没用了
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        //进入退出的动画
        popupWindow.setAnimationStyle(R.style.MyPopWindowAnim);
//        Gravity.BOTTOM
        popupWindow.showAtLocation(showAt,positon, 0, 180);
    }

    //Toast提示
    public static void showToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    //底部提示框
    public static void showSnack(View view,String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

}
