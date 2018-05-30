package com.renyu.keyboarddemo;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by admin-2 on 2018/4/20.
 */

public class CustomKeyBoardView extends KeyboardView {
    public CustomKeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomKeyBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_MOVE) {
            Log.d("---ActionListener---->","-------onTouchEvent------->");
            return true;
        }

        return super.onTouchEvent(me);
    }
}
