package com.xxyp.mentiondemo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sunhapper.x.spedit.mention.span.IntegratedSpan;
import com.xxyp.mentiondemo.mention.CenterAlignImageSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RichTextViewUtils {
    public static class MyClickableSpan extends ClickableSpan implements IntegratedSpan {

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

    public interface SpanClickListener {
        void setEditSlection(int index);
    }
    private static final String AT = "@[\\u4e00-\\u9fa5\\w\\-*]+ ";// @人
    private static final String COMMENT_REGEX = "(" + AT + ")";
    private static final String COMENT_TOPIC = "(?<=#)(\\S+)(?=#)";// 移除[#]中的匹配符
    private static final String TOPIC1 = "#([^#]+?)#";// ##话题
    private static final String COMENT_TYPE = "\\[(?<=\\[)(\\S+)(?=\\])\\]";//获取[]中的内容 包含[]
    private static final String REGEX = "(" + AT + ")" + "|"+ "(" + TOPIC1 + ")";
    public static SpannableString getCommentContent(final Context context,
                                             String source,
                                             TextView textView) {
        SpannableString spannableString = new SpannableString(source);

        //设置正则
        Pattern pattern = Pattern.compile(AT);
        Matcher matcher = pattern.matcher(spannableString);

        if (matcher.find()) {
            // 要实现文字的点击效果，这里需要做特殊处理
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            // 重置正则位置
            matcher.reset();
        }


        while (matcher.find()) {


            // at类型
            final String at = matcher.group(1);
            // 处理@符号
            if (at != null) {
                //获取匹配位置
                int start = matcher.start(1);
                int end = start + at.length();
                MyClickableSpan clickableSpan = new MyClickableSpan() {

                    @Override
                    public void onClick(View widget) {
                        //这里需要做跳转用户的实现，先用一个Toast代替

                    }
                };
                spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

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
    public static Spannable getPublishContent1(final Context context, String source, TextView textView, final SpanClickListener listener) {
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
        RichTextViewUtils.MyClickableSpan clickableSpan;
        //设置正则
        try {
            Pattern pattern = Pattern.compile(REGEX);
            final Matcher matcher = pattern.matcher(source);
            int count = 0;
            String aite;


            while (matcher.find()) {
                aite = matcher.group(1);

                topic = matcher.group(2);
                clickableSpan = new MyClickableSpan() {

                    @Override
                    public void onClick(View widget) {
                        //这里需要做跳转用户的实现，先用一个Toast代替
                        if (listener != null) {
                            //                            listener.setEditSlection(finalTopicEndIndex);
                        }
                    }
                };
                if (!TextUtils.isEmpty(aite)) {
                    Log.i("wdd", "aite = " + aite);
                    spannableString.setSpan(clickableSpan, matcher.start(1), matcher.end(1),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (!TextUtils.isEmpty(topic)) {
                    // 研祥科技大厦[地点]
                    Log.i("wdd", "topic = " + topic);
                    String strType = getTopicType(topic);
                    //图片居中
                    imageSpan = getImageSpan(context,getTagImgResource(strType));
                    topicStartIndex = matcher.start(2);
                    topicEndIndex =  matcher.end(2);
                    //设置图片
                    spannableString.setSpan(imageSpan,  matcher.start(2), matcher.start(2) + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    int startIndex = matcher.start(2);
                    int endIndex = matcher.end(2);
                    Log.i("wdd", "topicStartIndex = " + startIndex);
                    Log.i("wdd", "topicEndIndex = " + endIndex);
                    //设置字体颜色

                    spannableString.setSpan(clickableSpan, startIndex, endIndex,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    dirctionStartIndex = matcher.start(2) + topic.indexOf(strType);
                    dirctionEndIndex = dirctionStartIndex + strType.length();
                    imageSpan1= getImageSpan(context,R.mipmap.test);
                    spannableString.setSpan(imageSpan1, dirctionStartIndex, dirctionEndIndex + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return spannableString;
    }

    private static void handleTopSpanable(String topic) {

    }

    private static CenterAlignImageSpan getImageSpan(Context context,int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //图片居中
        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        return imageSpan;
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
}
