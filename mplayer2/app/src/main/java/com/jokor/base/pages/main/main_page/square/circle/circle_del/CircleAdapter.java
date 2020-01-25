package com.jokor.base.pages.main.main_page.square.circle.circle_del;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.jokor.base.R;
import com.jokor.base.util.base.SizeUtil;
import com.jokor.base.wedgit.recycleview.OnItemClick;
import com.jokor.base.wedgit.recycleview.OnLongClick;

import java.util.ArrayList;

public class CircleAdapter  extends RecyclerView.Adapter<CircleAdapter.BaseViewHolder> {

    private ArrayList<Integer> dataList = new ArrayList<>();
    private OnItemClick onitemClick;   //定义点击事件接口
    private OnLongClick onLongClick;  //定义长按事件接口

    public void setOnitemClick(OnItemClick onitemClick) {
        this.onitemClick = onitemClick;
    }

    public void setOnLongClick(OnLongClick onLongClick) {
        this.onLongClick = onLongClick;
    }

    public void replaceAll(ArrayList<Integer> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 插入数据使用notifyItemInserted，如果要使用插入动画，必须使用notifyItemInserted
     * 才会有效果。即便不需要使用插入动画，也建议使用notifyItemInserted方式添加数据，
     * 不然容易出现闪动和间距错乱的问题
     * */
    public void addData(int position,ArrayList<Integer> list) {
        dataList.addAll(position,list);
        notifyItemInserted(position);
    }

    //移除数据使用notifyItemRemoved
    public void removeData(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public CircleAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_water_fall, parent, false));
    }

    @Override
    public void onBindViewHolder(CircleAdapter.BaseViewHolder holder, int position) {
        holder.setData(dataList.get(position),position);
        if (onitemClick!=null){
            holder.itemView.setOnClickListener(v -> onitemClick.onItemClick(position));
        }
        if (onLongClick!=null){
            holder.itemView.setOnClickListener(v -> onLongClick.onLongClick(position));
        }
    }


    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        void setData(Object data,int position) {

        }
    }

    private class OneViewHolder extends BaseViewHolder {
        private ImageView ivImage;

        public OneViewHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.img);
        }


        @Override
        void setData(Object data,int position) {
            if (data != null) {
                int id = (int) data;
                ivImage.setImageResource(id);
                //需要Item高度不同才能出现瀑布流的效果，此处简单粗暴地设置一下高度
                if (position % 2 == 0) {
                    ivImage.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,SizeUtil.dip2px(ivImage.getContext(),150)));
                } else {
                    ivImage.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,SizeUtil.dip2px(ivImage.getContext(),200)));
                }

            }
        }
    }

}
