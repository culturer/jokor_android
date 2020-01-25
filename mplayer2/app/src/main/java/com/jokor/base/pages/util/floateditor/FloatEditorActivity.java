package com.jokor.base.pages.util.floateditor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jokor.base.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FloatEditorActivity extends Activity implements View.OnClickListener  {

    public static final String KEY_EDITOR_HOLDER = "editor_holder";
    public static final String KEY_EDITOR_CHECKER = "editor_checker";
    public static final String KEY_EDITOR_TITLE = "editor_title";
    private View cancel;
    private View submit;
    private TextView tv_title;
    private EditText etContent;
    private static EditorCallback mEditorCallback;
    private EditorHolder holder;
    private InputCheckRule checkRule;
    private boolean isClicked;
    private String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//

        holder = (EditorHolder) getIntent().getSerializableExtra(KEY_EDITOR_HOLDER);
        checkRule = (InputCheckRule) getIntent().getSerializableExtra(KEY_EDITOR_CHECKER);
        if(holder == null){
            throw new RuntimeException("EditorHolder params not found!");
        }
        title = getIntent().getStringExtra(KEY_EDITOR_TITLE);
        setContentView(holder.layoutResId);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        mEditorCallback.onAttached((ViewGroup) getWindow().getDecorView());

        initView();
        setEvent();
    }

    private void initView() {
        cancel = findViewById(holder.cancelViewId);
        submit = findViewById(holder.submitViewId);
        tv_title = findViewById(holder.titleId);
        etContent = findViewById(holder.editTextId);
        tv_title.setText(title);
    }

    private void setEvent() {
        if(cancel != null)
            cancel.setOnClickListener(this);

        submit.setOnClickListener(this);
    }

    public static void openEditor(Context context,String title, EditorCallback editorCallback, EditorHolder holder){
        openEditor(context, title,editorCallback, holder, null);
    }

    public static void openEditor(Context context,String title ,EditorCallback editorCallback, EditorHolder holder, InputCheckRule checkRule){
        Intent intent = new Intent(context, FloatEditorActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(KEY_EDITOR_HOLDER, holder);
        intent.putExtra(KEY_EDITOR_CHECKER, checkRule);
        intent.putExtra(KEY_EDITOR_TITLE,title);
        mEditorCallback = editorCallback;
        context.startActivity(intent);
    }

    public static void openDefaultEditor(Context context, EditorCallback editorCallback, InputCheckRule checkRule){
        openEditor(context, "回复",editorCallback, DefaultEditorHolder.createDefaultHolder(), checkRule);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == holder.cancelViewId){
            mEditorCallback.onCancel();
        }else if(id == holder.submitViewId){
            if (checkRule != null && !(checkRule.minLength == 0 && checkRule.maxLength == 0)) {
                if (!illegal()) {
                    isClicked = true;
                    mEditorCallback.onSubmit(etContent.getText().toString());
                    finish();
                }

                return;
            }
            mEditorCallback.onSubmit(etContent.getText().toString());
        }
        isClicked = true;
        finish();
    }

    private boolean illegal() {
        String content = etContent.getText().toString();
        if (TextUtils.isEmpty(content) || content.length() < checkRule.minLength) {
            Toast.makeText(this, getString(R.string.view_component_limit_min_warn, checkRule.minLength), Toast.LENGTH_SHORT).show();
            return true;
        }

        if (content.length() > checkRule.maxLength) {
            Toast.makeText(this, getString(R.string.view_component_limit_max_warn, checkRule.maxLength), Toast.LENGTH_SHORT).show();
            return true;
        }

        if (!TextUtils.isEmpty(checkRule.regxRule)) {
            Pattern pattern = Pattern.compile(checkRule.regxRule);
            Matcher matcher = pattern.matcher(content);
            if (!matcher.matches()) {
                Toast.makeText(this, getString(checkRule.regxWarn), Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isClicked){
            mEditorCallback.onCancel();
        }
        mEditorCallback = null;
    }

}
