package vip.jokor.im.pages.main.main_page.friends;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import vip.jokor.im.R;
import vip.jokor.im.base.Datas;
import vip.jokor.im.im.MsgEvent;
import vip.jokor.im.model.bean.AppliesBean;
import vip.jokor.im.model.bean.FriendsBean;
import vip.jokor.im.model.db.Msg;
import vip.jokor.im.pages.util.userinfo.UserInfoActivity;
import vip.jokor.im.presenter.FriendPresenter;
import vip.jokor.im.presenter.MainPresenter;
import vip.jokor.im.presenter.UserPresenter;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.wedgit.util.ShowUtil;

import com.google.android.material.snackbar.Snackbar;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    String TAG = "ItemAdapter";

    private List<AppliesBean> mDataSet;
    private LayoutInflater mInflater;
    private DateFormat dateFormat;
    private Context context;

    public ItemAdapter(Context context, List<AppliesBean> mDataSet) {
        mInflater = LayoutInflater.from(context);
        dateFormat = SimpleDateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH);
        this.mDataSet = mDataSet == null? new ArrayList<>():mDataSet;
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.view_list_item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
        final AppliesBean data = mDataSet.get(i);
        Log.e(TAG, "onBindViewHolder: id "+ data.getId());
        Log.e(TAG, "onBindViewHolder: data --- "+ GsonUtil.getGson().toJson(data));
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(context.getColor(R.color.light_black));
        itemViewHolder.icon.setBackground(drawable);
        RequestOptions options1 = new RequestOptions()
                .error(R.mipmap.logo)//加载错误之后的错误图
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);//只缓存最终的图片
        if (data.getUserId() == Datas.getUserInfo().getId()){
            Log.i(TAG, "onBindViewHolder: mysend");
            Glide.with(context)
                    .load(data.getToIcon())
                    .apply(options1)
                    .into(itemViewHolder.icon);
            itemViewHolder.title.setText(data.getToUsername());
        }
        if (data.getToId() == Datas.getUserInfo().getId()){
            Log.i(TAG, "onBindViewHolder: myreceive");
            Glide.with(context)
                    .load(data.getFromIcon())
                    .apply(options1)
                    .into(itemViewHolder.icon);
            itemViewHolder.title.setText(data.getFromUsername());
        }

        itemViewHolder.subTitle.setText(data.getMsg());

        switch (data.getStatus()){
            case AppliesBean.StatusSend:
                HttpCallback callback = new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        Log.e(TAG, "修改好友申请状态 onSuccess: "+t );
                        try {
                            JSONObject jb = new JSONObject(t);
                            int status = jb.getInt("status");
                            if (status == 200){
                                int apply_status = jb.getInt("apply_status");
                                data.setStatus(apply_status);
                                notifyDataSetChanged();
                            }else{
                                ShowUtil.showToast(context,"该好友请求已处理过哦！");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override 
                    public void onFailure(VolleyError error) {
                        ShowUtil.showToast(context,"网络异常！");
                        Log.e(TAG, "onFailure: "+error.getMessage() );
                    }
                };
                itemViewHolder.status.setText("待处理");
                itemViewHolder.status.setTextColor(context.getColor(R.color.light_black));
                itemViewHolder.status.setOnClickListener(v -> {
                    ShowUtil.showPopupMenu(
                            context,
                            itemViewHolder.status,
                            R.menu.confirm_add,
                            item -> {
                                switch (item.getItemId()){
                                    case R.id.add:
                                        //这里要先验证该用户是否是好友
                                        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_agree_add_friend,null);
                                        Spinner type = contentView.findViewById(R.id.type);
                                        type.setVisibility(View.VISIBLE);
                                        String[] strSpinnerItems = new String[Datas.getFriendBean().getCategorys().size()];
                                        for (int j=0;j<5;j++){
                                            strSpinnerItems[j]=Datas.getFriendBean().getCategorys().get(j).getName();
                                        }
                                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,R.layout.appoint_select,strSpinnerItems);
                                        spinnerAdapter.setDropDownViewResource(R.layout.appoint_item);
                                        type.setAdapter(spinnerAdapter);
                                        type.setSelection(1);
                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                                        android.app.AlertDialog dialog = builder.setTitle("添加好友")
                                                .setView(contentView)
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        HttpCallback callback1 = new HttpCallback() {
                                                            @Override
                                                            public void onSuccess(String t) {
                                                                Log.e(TAG, "addFriend onSuccess: "+t );
                                                                try {
                                                                    JSONObject jb = new JSONObject(t);
                                                                    int status = jb.getInt("status");
                                                                    if (status == 200){
                                                                        int apply_status = jb.getInt("apply_status");
                                                                        data.setStatus(apply_status);
                                                                        notifyDataSetChanged();
                                                                        ShowUtil.showToast(context,"添加好友成功");
                                                                        //在这里需要更新UI界面
                                                                        FriendsBean friendsBean = GsonUtil.getGson().fromJson(jb.getString("new_friend"),FriendsBean.class);
                                                                        Datas.getFriendBean().getFriends().add(friendsBean);
                                                                        EventBus.getDefault().postSticky(new FriendEvent(TAG,friendsBean));
                                                                        //向对方推送添加好友请求
                                                                        Msg msg = new Msg();
                                                                        msg.setMsgUsed(Msg.MSG_USED_ADD_FRIEND);
                                                                        msg.setMsgFrom(Msg.MSG_FROM_FRIEND);
                                                                        msg.setFromId(Datas.getUserInfo().getId());
                                                                        msg.setToId(data.getUserId());
                                                                        msg.setUsername(Datas.getUserInfo().getUserName());
                                                                        MsgEvent msgEvent = new MsgEvent(TAG,msg);
                                                                        EventBus.getDefault().postSticky(msgEvent);
//                                                                        //打开会话
                                                                        MainPresenter.getInstance().openP2PSession(friendsBean,context,true);
                                                                    }else {
                                                                        ShowUtil.showToast(context,"该好友已经是您的好友了哦");
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(VolleyError error) {
                                                                Log.e(TAG, "onFailure: "+error.getMessage() );
                                                            }
                                                        };
                                                        FriendPresenter.getInstance().through(Datas.getFriendBean().getCategorys().get((int) type.getSelectedItemId()).getId(),data.getId(),AppliesBean.StatusAgree,callback1);
                                                    }
                                                })
                                                .create();
                                        dialog.show();
                                        break;
                                    case R.id.refuse:
                                        FriendPresenter.getInstance().through(0,data.getId(),AppliesBean.StatusRefuse,callback);
                                        break;
                                    case R.id.ignore:
                                        FriendPresenter.getInstance().through(0,data.getId(),AppliesBean.StatusIgnore,callback);
                                        break;
                                }
                                return true;
                            },
                          null
                    );
                });
                break;
            case AppliesBean.StatusAgree:
                itemViewHolder.status.setText("已同意");
                itemViewHolder.status.setTextColor(context.getColor(R.color.green));
                itemViewHolder.status.setOnClickListener(v -> ShowUtil.showToast(context,"您已经通过该好友的好友请求哦！"));
                break;
            case AppliesBean.StatusRefuse:
                itemViewHolder.status.setText("已拒绝");
                itemViewHolder.status.setTextColor(context.getColor(R.color.red));
                itemViewHolder.status.setOnClickListener(v -> ShowUtil.showToast(context,"您已经拒绝该好友的好友请求哦！"));
                break;
            case AppliesBean.StatusIgnore:
                itemViewHolder.status.setText("已忽略");
                itemViewHolder.status.setTextColor(context.getColor(R.color.square_tab_unselected));
                itemViewHolder.status.setOnClickListener(v -> ShowUtil.showToast(context,"您已经忽略该好友的好友请求哦！"));
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void update(AppliesBean appliesBean){

        for (int i=0;i< mDataSet.size();i++){
            if (mDataSet.get(i).getId() == appliesBean.getId()){
                mDataSet.remove(i);
            }
        }
        mDataSet.add(0,appliesBean);
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView status;
        TextView title;
        TextView subTitle;

        public ItemViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            status = itemView.findViewById(R.id.status);
            title = itemView.findViewById(R.id.title);
            subTitle = itemView.findViewById(R.id.subtitle);
        }
    }

}