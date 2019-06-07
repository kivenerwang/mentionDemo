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
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sunhapper.x.spedit.mention.span.ISpan;
import com.sunhapper.x.spedit.mention.span.IntegratedSpan;
import com.sunhapper.x.spedit.view.SpXEditText;
import com.xxyp.mentiondemo.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MentionFragment extends Fragment implements View.OnClickListener ,  MentionDialog.DialogFragmentDataCallback  {
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

    public  class MyClickableSpan extends ClickableSpan implements IntegratedSpan {

        @Override
        public void onClick(View widget) {
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.parseColor("#6359E4"));
            ds.bgColor = 0x00000000;
            ds.setUnderlineText(false);
        }
    }

    public int getTranslateColor() {
        return android.R.color.transparent;
    }
    private static CenterAlignImageSpan getImageSpan(Context context,int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //图片居中
        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        return imageSpan;
    }

    /**
     * 设置发布内容样式
     * @param context context
     * @param source source
     * @param textView textView
     * @return SpannableString
     */
    public Spannable getPublishContent1(final Context context, String source, TextView textView) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(source);
        //话题开始位置
        int topicStartIndex;
        //话题结束位置
        int topicEndIndex;
        //尾部文案开始位置
        int dirctionStartIndex;
        //尾部文案结束位置
        int dirctionEndIndex;
        String topic;
        //头部图片类型资源
        CenterAlignImageSpan imageSpan;
        //尾部图片类型资源
        CenterAlignImageSpan imageSpan1;
        MyClickableSpan clickableSpan;
        //设置正则
        try {
            Pattern pattern = Pattern.compile(TOPIC1);
            final Matcher matcher = pattern.matcher(source);
            while (matcher.find()) {
                topic = matcher.group();
                // 研祥科技大厦[地点]
                String strType = getTopicType(topic);
                Log.i("wdd", "start = " + matcher.start());
                Log.i("wdd", "end = " + matcher.end());
                //图片居中
                imageSpan = getImageSpan(context,getTagImgResource(strType));
                topicStartIndex = matcher.start();
                topicEndIndex =  matcher.end();
                //设置图片
                spannableString.setSpan(imageSpan,  topicStartIndex, topicStartIndex + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                //设置字体颜色
                final int finalTopicEndIndex = topicEndIndex;
                clickableSpan = new MyClickableSpan() {

                    @Override
                    public void onClick(View widget) {
                        //这里需要做跳转用户的实现，先用一个Toast代替
                        setEditSlection(finalTopicEndIndex);
                    }
                };
                spannableString.setSpan(clickableSpan, topicStartIndex, topicEndIndex,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                dirctionStartIndex = topicStartIndex + topic.indexOf(strType);
                dirctionEndIndex = dirctionStartIndex + strType.length();
                imageSpan1= getImageSpan(context,R.mipmap.test);
                spannableString.setSpan(imageSpan1, dirctionStartIndex, dirctionEndIndex + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return spannableString;
    }

    private void setEditSlection(final int end) {
        //todo 添加异常判断
        Log.i("wdd", "end = " + end);
        if (end >= mentionEditText.getText().toString().length()) {
            mMentionText.post(new Runnable() {
                @Override
                public void run() {
                    mMentionText.setSelection(end);
                }
            });
        }

    }

    private static String getTopicType(String topic) {
        String type = "";
        Pattern pattern = Pattern.compile(COMENT_TYPE);
        Matcher matcher = pattern.matcher(topic);
        while (matcher.find()) {
            type = matcher.group();
        }
        return type;
    }

    public static final String TAG_TYPE_ADDRESS = "[地点]";
    public static final String TAG_TYPE_SHOP =  "[商品]";;
    public static final String TAG_TYPE_BRAND =  "[品牌]";;
    public static final String TAG_TYPE_FILM =  "[地点]";;
    public static final String TAG_TYPE_GOODS =  "[地点]";;
    public static final String TAG_TYPE_AITE =  "[地点]";;
    private static int getTagImgResource(String tagType) {
        if (TAG_TYPE_ADDRESS.equals(tagType)) {
            return R.mipmap.list_icon_address_blue;
        } else if (TAG_TYPE_SHOP.equals(tagType)) {
            return R.mipmap.shop_icon_blue;
        } else if (TAG_TYPE_BRAND.equals(tagType)) {
            return R.mipmap.biaoqian_icon_blue;
        } else if (TAG_TYPE_FILM.equals(tagType)) {
            return R.mipmap.cnime_shape_icon_blue;
        } else if (TAG_TYPE_GOODS.equals(tagType)) {
            return R.mipmap.combined_little_icon_blue;
        } else if (TAG_TYPE_AITE.equals(tagType)) {
            return R.mipmap.aite_icon_blue;
        }

        return R.mipmap.list_icon_address_blue;
    }
    public interface SpanCLickLisenter {
        void clickSpanText(int endIndex);
    }
    private void initMetionView() {
        String strDesc = "#研祥科技大厦[地点]# #研祥科技大厦[地点]# #神仙水乳套装 神仙水100ml+乳液100ml+小样水30ml+乳30ml[商品]# 哈哈哈";
        Spannable spannableString = getPublishContent1(getContext(), strDesc, mMentionText);
        mMentionText.setText(spannableString);
        mMentionText.setClickable(false);
        mMentionText.setLongClickable(false);
        mMentionText.setMovementMethod(ClickableMovementMethod.getInstance());
        Log.i("wdd", "数据 ："+ mMentionText.getText().toString());
    }

    private void filterText(String strDesc) {
//        Pattern pattern = Pattern.compile(COMMENT_REGEX);
//        Matcher matcher = pattern.matcher(spannableString);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_send) {
            sendText();
        } else if (id == R.id.et_comment) {
            showDialog();
        }
    }
    private void sendText() {
//        ISpan[] dataSpans = mentionEditText.getText().getSpans(0, mentionEditText.length(), ISpan.class);
//
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("完整的信息:");
//        stringBuilder.append(mentionEditText.getText().toString());
//        stringBuilder.append("\n数据信息:");
//        for (ISpan dataSpan : dataSpans) {
//            stringBuilder.append(dataSpan.toString())
//                    .append("\n");
//        }
//        Toast.makeText(getContext(), stringBuilder, Toast.LENGTH_SHORT).show();
    }

    private void showDialog() {
        MentionDialog dialog = new MentionDialog();
        dialog.bind(this);
        dialog.show(getActivity().getSupportFragmentManager(), "aaaa");
    }



    @Override
    public Editable getCommentText() {
        return (Editable) mentionEditText.getText();
    }

    @Override
    public void setCommentText(Editable commentEditable) {
        mentionEditText.setText(commentEditable);
    }
}
