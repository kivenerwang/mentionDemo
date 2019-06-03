package com.xxyp.mentiondemo.mention;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sunhapper.x.spedit.view.SpXEditText;
import com.xxyp.mentiondemo.R;
import com.xxyp.mentiondemo.mention.model.UserBean;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.xxyp.mentiondemo.mention.MentionActivity.DEFAULT_MENTION_PATTERN;
import static com.xxyp.mentiondemo.mention.MentionActivity.DEFAULT_METION_TAG;


public class MentionDialog extends DialogFragment implements View.OnClickListener {
    private SpXEditText metComment;
    private TextView tvSend;
    private static final int REQUEST_CODE = 2;
    private Map<String, Pattern> mPatternMap = new HashMap<>();
    private DialogFragmentDataCallback dataCallback;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.appearance_BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.appearance_dialog_fragment_comment_layout);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            windowParams.gravity = Gravity.BOTTOM;
            windowParams.dimAmount = 0.0f;
            windowParams.windowAnimations = R.style.appearance_BottomToTopAnim;

            window.setAttributes(windowParams);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getDecorView().setPadding(0, 0, 0, 0);

            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        }
        metComment = (SpXEditText) dialog.findViewById(R.id.et_comment);
        tvSend = dialog.findViewById(R.id.tv_send);
        metComment.setOnClickListener(this);
//        fillEditText();
        metComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (count == 1 && !TextUtils.isEmpty(charSequence)) {
                    char mentionChar = charSequence.toString().charAt(start);
                    for (Map.Entry<String, Pattern> entry : mPatternMap.entrySet()) {
                        if (entry.getKey().equals(String.valueOf(mentionChar))) {
                            onMentionCharacterInput(entry.getKey());
                            break;
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        addPattern(DEFAULT_METION_TAG, DEFAULT_MENTION_PATTERN);
        return dialog;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        if (dataCallback != null) {
            dataCallback.setCommentText(metComment.getText());
        }
        super.onDismiss(dialog);
    }

    private void fillEditText() {
        Editable commentText = dataCallback.getCommentText();
        if (commentText == null) {
            tvSend.setEnabled(false);
            return;
        } else {
            Log.i("wdd", "fillEditText not null");
        }
        metComment.setText(commentText);
        metComment.setSelection(commentText.length());


        if (commentText.length() == 0) {
            tvSend.setEnabled(false);
        }
    }
    public void onMentionCharacterInput(String tag) {
        Log.i("wdd", "检测到@事件");
        startActivityForResult(new Intent(getContext(), UserInfoActivity.class), REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_CODE) {
            updateMentionUI(data);
        }
    }

    private void updateMentionUI(Intent data) {
        UserBean user = (UserBean) data.getSerializableExtra("test");
        replace(user.getSpanableString());
    }
    private void replace(CharSequence charSequence) {
        Editable editable = metComment.getText();
        insertSpannableString(editable, charSequence);
    }


    public void insertSpannableString(Editable editable, CharSequence text) {
        int start = Selection.getSelectionStart(editable);
        int end = Selection.getSelectionEnd(editable);
        if (end < start) {
            int temp = start;
            start = end;
            end = temp;
        }
        editable.replace(start -1, end, text);
    }

    public void addPattern(String tag, String pattern) {
        mPatternMap.put(tag, Pattern.compile(pattern));
    }

    @Override
    public void onClick(View v) {

    }
    /**
     * 输入数据接口绑定
     * @param callback callback
     */
    public void bind(DialogFragmentDataCallback callback) {
        this.dataCallback = callback;
    }
    public interface DialogFragmentDataCallback {
        Editable getCommentText();
        void setCommentText(Editable commentEditable);
    }
}
