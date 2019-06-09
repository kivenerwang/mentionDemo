package com.xxyp.mentiondemo.mention;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.sunhapper.x.spedit.view.SpXEditText;
import com.xxyp.mentiondemo.R;

public class EditStatusView extends FrameLayout implements View.OnClickListener{

    private View iv_label,iv_aiti,iv_expression;
    private EmojiKeyboard emojiKeyboard;
    private SpXEditText metComment;

    private InputMethodManager inputMethodManager;

    private boolean isShownEmoji = false;//记录当前显示表情键盘
    private OnEmojiKeyboardListener onEmojiKeyboardListener;

    public EditStatusView(@NonNull Context context) {
        this(context,null);
    }

    public EditStatusView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EditStatusView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.appearance_dialog_release_edit,this);


        iv_label = findViewById(R.id.iv_label);
        iv_aiti = findViewById(R.id.iv_aiti);
        iv_expression = findViewById(R.id.iv_expression);
        emojiKeyboard = (EmojiKeyboard) findViewById(R.id.emojiKeyboard);
        iv_label.setOnClickListener(this);
        iv_expression.setOnClickListener(this);
        iv_aiti.setOnClickListener(this);
        emojiKeyboard.setVisibility(View.GONE);
//        initListener();
    }

    public void setOnEmojiKeyboardListener(OnEmojiKeyboardListener onEmojiKeyboardListener) {
        this.onEmojiKeyboardListener = onEmojiKeyboardListener;
    }

    public void setEditText(SpXEditText metComment) {
        this.metComment = metComment;
        setSoftKeyboard();
    }


//    private void initListener(){
//        RxView.clicks(iv_label)
//                .compose(RxJavaUtils.<Void>clickInterval())
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        //添加标签
//                        ARouter.getInstance().build(APPEARANCE_ACTIVITY_LABEL).navigation((Activity) getContext(), ReleaseFragment.TAG_SELECT_CODE);
//                    }
//                });
//        RxView.clicks(iv_aiti)
//                .compose(RxJavaUtils.<Void>clickInterval())
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        //添加@人
//                        Intent i = new Intent(getContext(), AtUserActivity.class);
//                        i.putExtra(AtUserActivity.SOURCE_TYPE, 1);
//                        getContext().startActivity(i);
//
//                    }
//                });
//        RxView.clicks(iv_expression)
//                .compose(RxJavaUtils.<Void>clickInterval())
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        emojiKeyboard.switchKeyboard();
//                        isShownEmoji = !isShownEmoji;
//                    }
//                });
//
//
//    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if(visibility == VISIBLE){//显示
            isShownEmoji = false;
        }else if(visibility == INVISIBLE){//隐藏

        }
    }

    //是否显示表情键盘
    public boolean isEmojiKeyboardShown(){
        return isShownEmoji;
    }


    //切换表情键盘
    public void switchKeyboard(){
        emojiKeyboard.switchKeyboard();
    }

    private void setSoftKeyboard() {
        //表情键盘 和 编辑框 有任何一个为空 都不继续
        if(emojiKeyboard == null){
            return;
        }
        if(metComment == null){
            return;
        }
        emojiKeyboard.bind(metComment,null);

        metComment.setFocusable(true);
        metComment.setFocusableInTouchMode(true);
        metComment.requestFocus();

        //为 metComment 设置监听器，在 DialogFragment 绘制完后立即呼出软键盘，呼出成功后即注销
        metComment.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                inputMethodManager = (InputMethodManager)  getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    if (inputMethodManager.showSoftInput(metComment, 0)) {
                        metComment.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_send) {

        } else if (id == R.id.et_comment) {
            Intent i = new Intent(getContext(), UserInfoActivity.class);
            getContext().startActivity(i);
        } else if (id == R.id.iv_aiti) {

        } else if (id == R.id.iv_expression) {

        }
    }

    public static interface OnEmojiKeyboardListener{
        void onShowEmojiKeyboard();

        void onHideEmojiKeyboard();
    }


}
