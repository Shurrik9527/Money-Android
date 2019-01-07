package com.moyacs.canary.pay;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * @Author: Akame
 * @Date: 2019/1/7
 * @Description:
 */
public class LotEditText extends EditText {
    private TextChangeListener textChangeListener;

    public LotEditText(Context context) {
        super(context);
        initListener();
    }

    public LotEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initListener();
    }

    public LotEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initListener();
    }

    private void initListener() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (textChangeListener != null) {
                    textChangeListener.onTextChangeCallBack(getTextValue());
                }
            }
        });
    }

    public int getTextValue() {
        try {
            return Integer.parseInt(getText().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public interface TextChangeListener {
        void onTextChangeCallBack(int lot);
    }

    public void setTextChangeListener(TextChangeListener textChangeListener) {
        this.textChangeListener = textChangeListener;
    }


}
