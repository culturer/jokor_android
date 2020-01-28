package vip.jokor.im.pages.util.article;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import vip.jokor.im.R;
import vip.jokor.im.model.bean.CommentsBean;
import vip.jokor.im.model.bean.ReplysBean;
import vip.jokor.im.pages.util.floateditor.EditorCallback;
import vip.jokor.im.pages.util.floateditor.EditorHolder;
import vip.jokor.im.pages.util.floateditor.FloatEditorActivity;
import vip.jokor.im.presenter.ArticlePresenter;
import vip.jokor.im.util.base.GsonUtil;
import vip.jokor.im.util.base.TimeUtil;
import vip.jokor.im.wedgit.util.ShowUtil;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends BaseAdapter {


    private String TAG = "CommentAdapter";

    private List<Comment> comments = new ArrayList<>();
    private ArticleActivity activity;
    RequestOptions options1 = new RequestOptions()
            .error(R.mipmap.logo)//加载错误之后的错误图
            .override(400,400)//指定图片的尺寸
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图片

    public CommentAdapter(List<CommentsBean> comments, ArticleActivity activity, List<ReplysBean> replys) {
        this.activity = activity;
        if (comments!=null && comments.size()!=0){
            for (CommentsBean item :comments){
                List<ReplysBean> mReplys = new ArrayList<>();
                if (replys!=null && replys.size()!=0){
                    for (ReplysBean reply :replys){
                        if (reply.getCommentId() == item.getId()){
                            mReplys.add(reply);
                        }
                    }
                }
                Comment comment = new Comment(item,mReplys);
                this.comments.add(comment);
            }
        }
    }

    public void update(CommentsBean commentsBean){
        if (commentsBean!=null){
            Comment comment = new Comment(commentsBean,new ArrayList<>());
            this.comments.add(comment);
        }
    }

    @Override
    public int getCount() {
        if (comments!=null) return comments.size();
        return 0;
    }

    @Override
    public Comment getItem(int position){
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(activity);
        View contentView = inflater.inflate(R.layout.article_comment_item,null);
        contentView.setOnClickListener(null);
        TextView comment = contentView.findViewById(R.id.comment);
        TextView username = contentView.findViewById(R.id.username);
        TextView time = contentView.findViewById(R.id.time);
        TextView time_count = contentView.findViewById(R.id.time_count);
        ImageView icon = contentView.findViewById(R.id.icon);
        ImageView menu = contentView.findViewById(R.id.menu);

        TextView replay = contentView.findViewById(R.id.replay);
        TextView reply1 = contentView.findViewById(R.id.reply1);
        TextView reply2 = contentView.findViewById(R.id.reply2);
        TextView reply3 = contentView.findViewById(R.id.reply3);

        Comment item = getItem(position);

        comment.setText(item.getCommentsBean().getContent());
        username.setText(item.getCommentsBean().getUsername());
        time.setText(TimeUtil.date2Str(TimeUtil.String2Date(item.getCommentsBean().getCreateTime())));

        initReply(item,time_count,replay,reply1,reply2,reply3);

        menu.setOnClickListener(v -> {
            ShowUtil.showToast(activity, "删除");
        });

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowUtil.showToast(activity,"进入个人名片页面");
            }
        });

        Glide.with(activity)
                .load(item.getCommentsBean().getIcon())
                .apply(options1)
                .into(icon);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowUtil.showToast(activity,"进入个人名片页面");
            }
        });

        return contentView;
    }

    private void initReply( Comment item,TextView time_count,TextView reply,TextView reply1,TextView reply2,TextView reply3){

        View.OnClickListener listener = v -> activity.startReplyActivity(GsonUtil.getGson().toJson(item));

        time_count.setOnClickListener(listener);
        reply1.setOnClickListener(listener);
        reply2.setOnClickListener(listener);
        reply3.setOnClickListener(listener);

        if (item.getReplys() == null || item.getReplys().size() == 0){
            reply1.setVisibility(View.GONE);
            reply2.setVisibility(View.GONE);
            reply3.setVisibility(View.GONE);
            time_count.setVisibility(View.GONE);
        }else {
            switch (item.getReplys().size()){
                case 1 :
                    reply1.setVisibility(View.VISIBLE);
                    String strMsg1 = item.getReplys().get(0).getUsername()+ ":" + item.getReplys().get(0).getContent();
                    reply1.setText(strMsg1);
                    reply2.setVisibility(View.GONE);
                    reply3.setVisibility(View.GONE);
                    time_count.setVisibility(View.GONE);
                    break;
                case 2 :
                    reply1.setVisibility(View.VISIBLE);
                    String strMsg21 = item.getReplys().get(0).getUsername()+ ":" + item.getReplys().get(0).getContent();
                    reply1.setText(strMsg21);
                    reply2.setVisibility(View.VISIBLE);
                    String strMsg22 = item.getReplys().get(1).getUsername()+ ":" + item.getReplys().get(1).getContent();
                    reply2.setText(strMsg22);
                    reply3.setVisibility(View.GONE);
                    time_count.setVisibility(View.GONE);
                    break;

                default :
                    reply1.setVisibility(View.VISIBLE);
                    String strMsg31 = item.getReplys().get(0).getUsername()+ ":" + item.getReplys().get(0).getContent();
                    reply1.setText(strMsg31);
                    reply2.setVisibility(View.VISIBLE);
                    String strMsg32 = item.getReplys().get(1).getUsername()+ ":" + item.getReplys().get(1).getContent();
                    reply2.setText(strMsg32);
                    reply3.setVisibility(View.VISIBLE);
                    String strMsg33 = item.getReplys().get(2).getUsername()+ ":" + item.getReplys().get(2).getContent();
                    reply3.setText(strMsg33);
                    if (item.getReplys().size() == 3){
                        time_count.setVisibility(View.GONE);
                    }else {
                        String strCount = ""+item.getReplys().size()+"条回复";
                        time_count.setText(strCount);
                        time_count.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }

        reply.setOnClickListener(v -> {
            //点击进入评论
            EditorCallback editorCallback = new EditorCallback() {
                @Override
                public void onCancel() {
                    ShowUtil.showToast(activity,"取消");
                }

                @Override
                public void onSubmit(String content) {

                    HttpCallback callback = new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            Log.e(TAG, "add reply onSuccess: "+t );
                            try {
                                JSONObject jb = new JSONObject(t);
                                int status = jb.getInt("status");
                                if (status == 200){
                                    String strData = jb.getString("reply");
                                    ReplysBean replysBean = GsonUtil.getGson().fromJson(strData,ReplysBean.class);
                                    item.getReplys().add(replysBean);
                                    notifyDataSetChanged();
                                    activity.setResultData(replysBean);
                                }else{
                                    ShowUtil.showToast(activity,"回复失败，请稍后再试");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int errorNo, String strMsg) {
                            Log.e(TAG, "onFailure: "+errorNo+","+strMsg );
                        }
                    };
                    ArticlePresenter.getInstance().addReply(item.getCommentsBean().getArticleId(),item.commentsBean.getId(),content,callback);
                }

                @Override
                public void onAttached(ViewGroup rootView) {

                }
            };

            String subtitle = "回复 - "+item.getCommentsBean().getUsername();
//            if (item.getArticle().getContent().length()>5){
//                subtitle = "回复 - "+ item.getArticle().getContent().substring(0,5)+"...";
//            }else {
//                subtitle = "回复 - "+ item.getArticle().getContent();
//            }
            FloatEditorActivity.openEditor(activity,subtitle,editorCallback, new EditorHolder(R.layout.fast_reply_floating_layout, R.id.tv_cancel, R.id.tv_submit, R.id.et_content, R.id.tv_title));

        });
    }


}
