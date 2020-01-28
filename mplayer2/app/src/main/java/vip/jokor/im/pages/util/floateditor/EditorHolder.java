package vip.jokor.im.pages.util.floateditor;


import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

import java.io.Serializable;


public class EditorHolder implements Serializable{
    int layoutResId;
    int cancelViewId;
    int submitViewId;
    int editTextId;
    int titleId;
    public EditorHolder(@LayoutRes int layoutResId, @IdRes int cancelViewId,
                        @IdRes int submitViewId, @IdRes int editTextId, @IdRes int titleId){
        this.layoutResId = layoutResId;
        this.cancelViewId = cancelViewId;
        this.submitViewId = submitViewId;
        this.editTextId = editTextId;
        this.titleId = titleId;
    }
}
