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
public class MoneyEditText extends EditText {
    private String edText;//输入的内容
    private TextChangeListener textChangeListener;

    public MoneyEditText(Context context) {
        super(context);
        initListener();
    }

    public MoneyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initListener();
    }

    public MoneyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
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
                edText = s.toString();
                int dot = edText.indexOf(".");
                if (dot == 0) {
                    setEditText(s.insert(0, "0"));
                    dot = edText.indexOf(".");
                }
                if (dot != -1) {
                    int length = edText.length() - 1;
                    if (length - dot > 2) {
                        setEditText(s.delete(s.length() - 1, s.length()));
                    }
                }
                if (textChangeListener != null) {
                    textChangeListener.textChangeCallBack(getTextValue());
                }
            }
        });
    }

    private void setEditText(Editable text) {
        setText(text);
        setSelection(text.length());
    }

    public float getTextValue() {
        try {
            return Float.valueOf(getText().toString());
        } catch (Exception e) {
            return 0.0f;
        }
    }

    interface TextChangeListener {
        void textChangeCallBack(float textValue);
    }

    public void setTextChangeListener(TextChangeListener textChangeListener) {
        this.textChangeListener = textChangeListener;
    }
}
