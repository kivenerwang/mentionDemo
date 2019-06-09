package com.xxyp.mentiondemo.mention;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sunhapper.x.spedit.view.SpXEditText;
import com.xxyp.mentiondemo.R;
import com.xxyp.mentiondemo.RichTextViewUtils;

public class MentionFragment extends Fragment implements View.OnClickListener  {
    private static final String COMENT_TOPIC = "(?<=#)(\\S+)(?=#)";// 移除[#]中的匹配符
    private static final String TOPIC1 = "#([^#]+?)#";// ##话题
    private static final String COMENT_TYPE = "\\[(?<=\\[)(\\S+)(?=\\])\\]";//获取[]中的内容 包含[]
    private static final String REGIX = "(" + TOPIC1 + ")" + "|" + "(" + COMENT_TYPE + ")";
    private TextView mentionEditText;
    private TextView mSendView;
    private SpXEditText mMentionText;
    private View rootView;
    public static MentionFragment newInstance() {

        Bundle args = new Bundle();

        MentionFragment fragment = new MentionFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mention, container,false);
        mentionEditText = rootView.findViewById(R.id.et_comment);
        mentionEditText.setOnClickListener(this);
        mSendView = rootView.findViewById(R.id.tv_send);
        mMentionText = rootView.findViewById(R.id.mention_text);
        initMetionView();
        mSendView.setOnClickListener(this);
        return rootView;
    }
    private static CenterAlignImageSpan getImageSpan(Context context,int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //图片居中
        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        return imageSpan;
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
        String strDesc = "#研祥科技大厦[地点]# #研祥科技大厦[地点]# #神仙水乳套装 神仙水100ml+乳液100ml+小样水30ml+乳30ml[商品]# 哈哈哈";
        RichTextViewUtils.SpanClickListener listener = new RichTextViewUtils.SpanClickListener() {
            @Override
            public void setEditSlection(int index) {
                MentionFragment.this.setEditSlection(index);
            }
        };
        Spannable spannableString = RichTextViewUtils.getPublishContent1(getContext(), strDesc, mMentionText, listener);
        mMentionText.setText(spannableString);
        mMentionText.setClickable(false);
        mMentionText.setLongClickable(false);
        mMentionText.setMovementMethod(ClickableMovementMethod.getInstance());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }

}
