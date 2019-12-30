package com.yangj.dahemodule.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.yangj.dahemodule.R;

/**
 * Author:xch
 * Date:2019/12/18
 * Description:
 */
public class FeedbackTxtView extends ViewBase {
    private EditText edit_feed_back;

    public FeedbackTxtView(Context context) {
        super(context);
    }

    public FeedbackTxtView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FeedbackTxtView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_feed_back_txt;
    }

    @Override
    public void initData() {
        edit_feed_back = findViewById(R.id.edit_feed_back);
    }

    public String getInputTxt() {
        return edit_feed_back.getText().toString().trim();
    }

    public void setInputTxt(String content) {
        edit_feed_back.setText(content);
    }

    public void setInputFocusable(boolean focusable) {
        edit_feed_back.setFocusableInTouchMode(focusable);
        edit_feed_back.setFocusable(focusable);
    }
}
