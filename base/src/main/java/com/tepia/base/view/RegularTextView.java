package com.tepia.base.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Android Studio
 *
 * @author: Arthur
 * Date:    2019/5/28
 * Time:    16:56
 * Describe:显示Regular字体的textview
 */
public class RegularTextView extends android.support.v7.widget.AppCompatTextView {

    public RegularTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public RegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public RegularTextView(Context context) {
        super(context);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
//            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Regular.ttf"));
    }
}
