package com.qiu.tvselecttextview.view;

import android.content.Context;
import android.text.InputType;
import android.text.Selection;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.qiu.tvselecttextview.utils.Tool;

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
                                try {
                                    selectPosition = findPreSelectTextPosition(selectPosition);
                                    selectPosition = findPreStartPosition(selectPosition);
                                    int nextPosition = findNextSelectTextPosition(selectPosition);
                                    currText = getText().toString().substring(selectPosition, nextPosition);
                                    Selection.setSelection(getText(), selectPosition, nextPosition);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return true;
                            }
                            break;
                        case KeyEvent.KEYCODE_DPAD_RIGHT://右
                            if (v.hasFocus() && getText().length() > 0) {
                                try {
                                    int nextPosition = findNextSelectTextPosition(selectPosition);
                                    currText = getText().toString().substring(selectPosition, nextPosition);
                                    Selection.setSelection(getText(), selectPosition, nextPosition);
                                    selectPosition = findNextStartPosition(nextPosition);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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

    private int findNextStartPosition(int pos) {
        if (getText().length() > 0) {
            if (pos >= getText().length()) {
                return 0;
            }
            String currText = getText().toString().substring(pos, pos + 1);
            if (currText.equals(" ") || currText.equals("\n") || currText.equals(",") || currText.equals(".") || currText.equals("？") || currText.equals("?")
                    || currText.equals("，") || currText.equals("。") || currText.equals("！") || currText.equals("!")) {
                return findNextStartPosition(pos + 1);
            } else {
                return pos;
            }
        }
        return 0;
    }

    private int findNextSelectTextPosition(int pos) {
        if (getText().length() > 0) {
            if (pos < getText().length()) {
                String currText = getText().toString().substring(pos, pos + 1);
                if (Tool.INSTANCE.isChinese(currText)) {
                    return pos + 1;
                } else {
                    if (currText.equals(" ") || currText.equals("\n") || currText.equals(",") || currText.equals(".") || currText.equals("？") || currText.equals("?")
                            || currText.equals("，") || currText.equals("。") || currText.equals("！") || currText.equals("!")) {
                        return pos;
                    }
                }
            } else {
                return getText().length();
            }
        } else {
            return 0;
        }
        return findNextSelectTextPosition(pos + 1);
    }

    private int findPreStartPosition(int pos) {
        if (getText().length() > 0) {
            if (pos <= 0) {
                return 0;
            }
            String currText = getText().toString().substring(pos - 1, pos);
            if (Tool.INSTANCE.isChinese(currText)) {
                return pos - 1;
            } else {
                if (currText.equals(" ") || currText.equals("\n") || currText.equals(",") || currText.equals(".") || currText.equals("？") || currText.equals("?")
                        || currText.equals("，") || currText.equals("。") || currText.equals("！") || currText.equals("!")) {
                    return pos;
                } else {
                    return findPreStartPosition(pos - 1);
                }
            }
        }
        return 0;
    }

    private int findPreSelectTextPosition(int pos) {
        if (getText().length() > 0) {
            if (pos > 0) {
                String currText = getText().toString().substring(pos - 1, pos);
                if (Tool.INSTANCE.isChinese(currText)) {
                    return pos;
                } else {
                    if (currText.equals(" ") || currText.equals("\n") || currText.equals(",") || currText.equals(".") || currText.equals("？") || currText.equals("?")
                            || currText.equals("，") || currText.equals("。") || currText.equals("！") || currText.equals("!")) {
                        return findPreSelectTextPosition(pos - 1);
                    } else {
                        return pos;
                    }
                }
            } else {
                return getText().length();
            }
        } else {
            return 0;
        }
    }

    public void setOnTextSelectListener(OnTextSelectionListener listener) {
        this.listener = listener;
    }

    public interface OnTextSelectionListener {
        void onTextSelected(String text);
    }
}

