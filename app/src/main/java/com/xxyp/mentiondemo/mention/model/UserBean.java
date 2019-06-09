package com.xxyp.mentiondemo.mention.model;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;

import com.sunhapper.x.spedit.mention.span.BreakableSpan;
import com.sunhapper.x.spedit.mention.span.ISpan;
import com.sunhapper.x.spedit.mention.span.IntegratedSpan;
import com.xxyp.mentiondemo.R;
import com.xxyp.mentiondemo.mention.AppApplication;
import com.xxyp.mentiondemo.mention.CenterAlignImageSpan;

import java.io.Serializable;

public class UserBean implements IntegratedSpan, BreakableSpan,Serializable, ISpan {
    public String userName;
    public int userId;
    private Object styleSpan;
    private String text;

    public UserBean(String naeme, int id) {
        this.userName = naeme;
        this.userId = id;
    }
    @Override
    public boolean isBreak(Spannable text) {
        int spanStart = text.getSpanStart(this);
        int spanEnd = text.getSpanEnd(this);
        boolean isBreak = spanStart >= 0 && spanEnd >= 0 && !text.subSequence(spanStart, spanEnd).toString().equals(
                getDisplayText());
        if (isBreak && styleSpan != null) {
            text.removeSpan(styleSpan);
            styleSpan = null;
        }
        return isBreak;
    }
    @Override
    public Spannable getSpannableString() {
        styleSpan = new ForegroundColorSpan(Color.BLUE);
        SpannableString spannableString = new SpannableString(getDisplayText());
        spannableString.setSpan(styleSpan, 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(this, 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
//        CenterAlignImageSpan imageSpan1= getImageSpan(AppApplication.getsApplication(), R.mipmap.test);
//        spannableString.setSpan(imageSpan1, getDisplayText().length() - 1, getDisplayText().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return stringBuilder.append(spannableString).append(" ");
    }
    private static CenterAlignImageSpan getImageSpan(Context context, int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //图片居中
        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        return imageSpan;
    }
    private CharSequence getDisplayText() {
        return "@" +userName + " ";
    }
}
