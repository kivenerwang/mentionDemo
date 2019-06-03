package com.xxyp.mentiondemo.mention.model;


import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.sunhapper.x.spedit.mention.span.BreakableSpan;
import com.sunhapper.x.spedit.mention.span.ISpan;
import com.sunhapper.x.spedit.mention.span.IntegratedSpan;

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
    public Spannable getSpanableString() {
        styleSpan = new ForegroundColorSpan(Color.BLUE);
        SpannableString spannableString = new SpannableString(getDisplayText());
        spannableString.setSpan(styleSpan, 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(this, 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        return stringBuilder.append(spannableString).append(" ");
    }

    private CharSequence getDisplayText() {
        return "@" +userName;
    }
}
