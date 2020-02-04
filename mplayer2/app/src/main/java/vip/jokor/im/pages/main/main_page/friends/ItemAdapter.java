package vip.jokor.im.pages.main.main_page.friends;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import vip.jokor.im.R;
import vip.jokor.im.base.Datas;
import vip.jokor.im.model.bean.AppliesBean;
import vip.jokor.im.presenter.FriendPresenter;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.wedgit.util.ShowUtil;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

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
                                //这里需要发送推送消息
                                ShowUtil.showToast(context,"该好友请求已处理过哦！");
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
                                    case R.id.add: FriendPresenter.getInstance().through(context,data,AppliesBean.StatusAgree,callback);
                                    case R.id.refuse:FriendPresenter.getInstance().through(context,data,AppliesBean.StatusRefuse,callback);
                                    case R.id.ignore:FriendPresenter.getInstance().through(context,data,AppliesBean.StatusIgnore,callback);
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