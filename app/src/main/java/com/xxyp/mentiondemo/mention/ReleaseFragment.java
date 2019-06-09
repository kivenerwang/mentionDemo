package com.xxyp.mentiondemo.mention;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunhapper.x.spedit.view.SpXEditText;
import com.xxyp.mentiondemo.R;
import com.xxyp.mentiondemo.RichTextViewUtils;
import com.xxyp.mentiondemo.mention.model.UserBean;

public class ReleaseFragment extends Fragment implements View.OnClickListener {

    private EditText mTitleView;
//    private TextView mentionEditText;
    private SpXEditText mMentionText;
    private EditStatusView mEditStatusView;
    private View rootView;
    public static ReleaseFragment newInstance() {

        Bundle args = new Bundle();

        ReleaseFragment fragment = new ReleaseFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_release, container,false);
//        mTitleView = rootView.findViewById(R.id.et_title);
        mMentionText = rootView.findViewById(R.id.mention_text);
//        mEditStatusView = rootView.findViewById(R.id.edit_status);
//        mEditStatusView.setEditText(mMentionText);
        initMetionView();
        return rootView;
    }

    //关闭辅助编辑浮层
    private void hideEditLayer(){

        //表情键盘正在显示
        if(mEditStatusView.isEmojiKeyboardShown()){
            //从新计算设置表情键盘底部外边距

        }else{
            //表情键盘没有显示 直接关闭编辑浮层
            mEditStatusView.setVisibility(View.INVISIBLE);
        }
        //浮层底部外边距清空
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mEditStatusView.getLayoutParams();
        if(layoutParams.bottomMargin != 0){
            layoutParams.bottomMargin = 0;
            mEditStatusView.setLayoutParams(layoutParams);

        }


    }
    //显示辅助编辑浮层
    private void showEditLayer(int height){
        mEditStatusView.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mEditStatusView.getLayoutParams();
        if(layoutParams.bottomMargin != height){
            layoutParams.bottomMargin = height;
            mEditStatusView.setLayoutParams(layoutParams);
        }

    }


    public void updateMentionUI(Intent data) {
        UserBean user = (UserBean) data.getSerializableExtra("test");
        mMentionText.replace(user.getSpannableString());
    }



    private void setEditSlection(int index) {
        //todo 添加异常判断
        if (null != mMentionText ) {
            int currentEtLength =mMentionText.getText().toString().length();
            if ( currentEtLength <  index) {
                index = currentEtLength - 1;
            }
            final int finalIndex = index;
            mMentionText.post(new Runnable() {
                @Override
                public void run() {
                    if (mMentionText != null) {
                        mMentionText .setSelection(finalIndex);
                    }
                }
            });
        }

    }

    private void initMetionView() {
        String strDesc = "@张林 #研祥科技大厦[地点]# #研祥科技大厦[地点]# #研祥科技大厦[地点]# #神仙水乳套装 神仙水100ml+乳液100ml+小样水30ml+乳30ml[商品]# 哈哈哈";
        RichTextViewUtils.SpanClickListener listener = new RichTextViewUtils.SpanClickListener() {
            @Override
            public void setEditSlection(int index) {
                ReleaseFragment.this.setEditSlection(index);
            }
        };
        Spannable spannableString = RichTextViewUtils.getPublishContent1(getContext(), strDesc, mMentionText, null);
        mMentionText.setText(spannableString);
        mMentionText.setClickable(false);
        mMentionText.setMovementMethod(ClickableMovementMethod.getInstance());
        //监听键盘高度变化
//        mMentionText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
//            //当键盘弹出隐藏的时候会 调用此方法。
//            @Override
//            public void onGlobalLayout() {
//                Rect r = new Rect();
//                //获取当前界面可视部分
//                getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
//                //获取屏幕的高度
//                int screenHeight =  getActivity().getWindow().getDecorView().getRootView().getHeight();
//                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
//                int heightDifference = screenHeight - r.bottom;
//
//                if(heightDifference > 0){//键盘显示了
//                    showEditLayer(heightDifference);
//                }else{//键盘隐藏了
//                    hideEditLayer();
//                }
//
//            }
//
//        });
//        mMentionText.setOnMentionCharacterInputListener(new SpXEditText.OnMentionCharacterInputListener() {
//            @Override
//            public void onMentionCharacterInput(CharSequence mentionChar) {
//                Intent intent = new Intent(getContext(), UserInfoActivity.class);
//                startActivityForResult(intent,2);
//            }
//        });
//        mMentionText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    //mEditStatusView.setVisibility(View.VISIBLE);
//                } else {
//                    //mEditStatusView.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
    }

}
