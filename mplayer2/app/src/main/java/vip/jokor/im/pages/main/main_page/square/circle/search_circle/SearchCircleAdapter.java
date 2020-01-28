package vip.jokor.im.pages.main.main_page.square.circle.search_circle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import vip.jokor.im.R;


public class SearchCircleAdapter extends BaseAdapter {

    private Context context;

    public SearchCircleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 30;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.search_circle_item,null);
        return contentView;
    }
}
