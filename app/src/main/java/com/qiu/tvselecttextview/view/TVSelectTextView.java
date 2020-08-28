package com.qiu.tvselecttextview.view;

import android.content.Context;
import android.text.InputType;
import android.text.Selection;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by qiu on 2020/8/28 10:09.
 */
public class TVSelectTextView extends AppCompatEditText {
    private String currText = "";
    private int selectPosition = 0;
    private OnTextSelectionListener listener = null;

    public TVSelectTextView(@NonNull Context context) {
        super(context);
        initView();
    }

    public TVSelectTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TVSelectTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        this.setInputType(InputType.TYPE_NULL);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        if (this.hasFocus()) {
            if (getText().length() > 0) {
                currText = getText().toString().substring(selectPosition, selectPosition + 1);
                Selection.setSelection(getText(), selectPosition, selectPosition + 1);
            }
        }
        this.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_LEFT://左
                            if (v.hasFocus() && getText().length() > 0) {
                                int nextPosition = selectPosition - 1;
                                if (nextPosition > 0) {
                                    selectPosition -= 1;
                                } else {
                                    selectPosition = getText().length();
                                }
                                currText = getText().toString().substring(selectPosition - 1, selectPosition);
                                Selection.setSelection(getText(), selectPosition - 1, selectPosition);
                                return true;
                            }
                            break;
                        case KeyEvent.KEYCODE_DPAD_RIGHT://右
                            if (v.hasFocus() && getText().length() > 0) {
                                int nextPosition = selectPosition + 1;
                                if (nextPosition < getText().length()) {
                                    selectPosition += 1;
                                } else {
                                    selectPosition = 0;
                                }
                                currText = getText().toString().substring(selectPosition, selectPosition + 1);
                                Selection.setSelection(getText(), selectPosition, selectPosition + 1);
                                return true;
                            }
                            break;
                        //上下
                        case KeyEvent.KEYCODE_DPAD_UP:
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            if (v.hasFocus()) {
                                return true;
                            }
                            break;
                        //确定
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                            if (v.hasFocus()) {
                                if (listener != null) {
                                    listener.onTextSelected(currText);
                                }
                                return true;
                            }
                            break;
                    }
                    return false;
                }
                return false;
            }
        });
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (getText().length() > 0) {
                        currText = getText().toString().substring(selectPosition, selectPosition + 1);
                        Selection.setSelection(getText(), selectPosition, selectPosition + 1);
                    }
                }
            }
        });
    }

    public void setOnTextSelectListener(OnTextSelectionListener listener) {
        this.listener = listener;
    }

    public interface OnTextSelectionListener {
        void onTextSelected(String text);
    }
}

