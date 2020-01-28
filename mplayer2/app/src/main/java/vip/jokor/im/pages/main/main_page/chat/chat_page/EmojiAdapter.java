package vip.jokor.im.pages.main.main_page.chat.chat_page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import vip.jokor.im.R;

/**
 * Created by HONGDA on 2017/3/15.
 */
public class EmojiAdapter extends BaseAdapter {

    private Context ctx;
    private List<Integer> emojiIds;
    private LayoutInflater inflater;

    public EmojiAdapter(Context ctx, List<Integer> emojiIds) {
        this.ctx = ctx;
        this.emojiIds = emojiIds;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return emojiIds.size();
    }

    @Override
    public Object getItem(int i) {
        return emojiIds.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        emojiHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_emoji, null);
            holder = new emojiHolder();
            holder.emojiImg = view.findViewById(R.id.img_emoji);
            view.setTag(holder);
        } else {
            holder = (emojiHolder) view.getTag();
        }
        holder.emojiImg.setImageResource(emojiIds.get(i));
        return view;
    }

    class emojiHolder {
        ImageView emojiImg;
    }
}
