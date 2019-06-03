package com.xxyp.mentiondemo.mention;

import android.content.Context;
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
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxyp.mentiondemo.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MentionFragment extends Fragment implements View.OnClickListener ,  MentionDialog.DialogFragmentDataCallback  {
    private static final String COMENT_TOPIC = "(?<=#)(\\S+)(?=#)";// 移除[#]中的匹配符
    private static final String TOPIC1 = "#([^#]+?)#";// ##话题
    private static final String COMENT_TYPE = "\\[(?<=\\[)(\\S+)(?=\\])\\]";//获取[]中的内容 包含[]
    private static final String REGIX = "(" + TOPIC1 + ")" + "|" + "(" + COMENT_TYPE + ")";
    private TextView mentionEditText;
    private TextView mSendView;
    private TextView mMentionText;
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
    /**
     * 设置发布内容样式
     * @param context context
     * @param source source
     * @param textView textView
     * @return SpannableString
     */
    public static Spannable getPublishContent(final Context context, String source, TextView textView) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(source);

        //设置正则
        Pattern pattern = Pattern.compile(TOPIC1);
        Matcher matcher = pattern.matcher(source);
        int tagType;
        Drawable drawable;
        Object styleSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.appearance_color_5C9));
        while (matcher.find()) {
            String topic = matcher.group();
            Log.i("wdd", "topic = " + topic);
            String strType = getTopicType(topic);
            Log.i("wdd", "strType  = " + strType );
            String topicContent = topic.replace(strType, "");
            Log.i("wdd", "topicContent  = " + topicContent );
            String result = topicContent.substring(0,topicContent.length() -2);
            Log.i("wdd", "result  = " + result );
            SpannableString spannabletopicContent = new SpannableString(topicContent);

            drawable = context.getResources().getDrawable(getTagImgResource(strType));
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            //图片居中
            CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
            spannabletopicContent.setSpan(imageSpan, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(styleSpan, 0, spannableString.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.append(spannabletopicContent).append(" ");
        }

        return spannableString;
    }

    /**
     * 设置发布内容样式
     * @param context context
     * @param source source
     * @param textView textView
     * @return SpannableString
     */
    public static Spannable getPublishContent1(final Context context, String source, TextView textView) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(source);

        //设置正则
        Pattern pattern = Pattern.compile(TOPIC1);
        Matcher matcher = pattern.matcher(source);
        int tagType;
        int startIndex;
        int endIndex;
        Drawable drawable;

        while (matcher.find()) {
            Object styleSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.appearance_color_5C9));
            String topic = matcher.group();
            // 研祥科技大厦[地点]
            Log.i("wdd", "topic = " + topic);
            String strType = getTopicType(topic);

            spannableString.delete(spannableString.toString().indexOf(strType), spannableString.toString().indexOf(strType) + strType.length());
            source = spannableString.toString();
            Log.i("wdd", "strType  = " + strType );
            String topicContent = topic.replace(strType, "");
            Log.i("wdd", "topicContent  = " + topicContent);

            drawable = context.getResources().getDrawable(getTagImgResource(strType));
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            //图片居中
            CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
            startIndex = source.indexOf(topicContent);
            endIndex = startIndex + topicContent.length();
            Log.i("wdd", "startIndex  = " + startIndex );
            Log.i("wdd", "endIndex  = " + endIndex );
            //设置图片
            spannableString.setSpan(imageSpan,  startIndex, startIndex + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            //设置字体颜色
            spannableString.setSpan(styleSpan, startIndex, endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.delete(endIndex-1, endIndex );
        }

        return spannableString;
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
    private void initMetionView() {
        String strDesc = "#研祥科技大厦[地点]# #漕河泾现代服务业园区[地点]# #神仙水乳套装 神仙水100ml+乳液100ml+小样水30ml+乳30ml[商品]# 哈哈哈";
        Spannable spannableString = getPublishContent1(getContext(), strDesc, mMentionText);
        mMentionText.setText(spannableString);
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
