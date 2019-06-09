package com.sunhapper.x.spedit.mention.span;

import android.text.Spannable;

/**
 * 泛型接口@功能 用户数据均要实现该接口
 */
public interface ISpan {
    /**
     * 获取带颜色的字体
     * @return
     */
    Spannable getSpannableString();
}
