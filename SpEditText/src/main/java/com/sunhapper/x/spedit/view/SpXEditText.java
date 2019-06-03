package com.sunhapper.x.spedit.view;

import android.content.Context;
import android.text.NoCopySpan;
import android.text.Selection;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import com.sunhapper.x.spedit.mention.span.IntegratedSpan;
import com.sunhapper.x.spedit.mention.watcher.SpanChangedWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunhapper on 2019/1/25 .
 */
public class SpXEditText extends android.support.v7.widget.AppCompatEditText {

    public SpXEditText(Context context) {
        super(context);
    }

    public SpXEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpXEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        List<NoCopySpan> watchers = new ArrayList<>();
        watchers.add(new SpanChangedWatcher());
        setEditableFactory(new SpXEditableFactory(watchers));
    }

    private boolean handleKeyEvent(KeyEvent keyEvent) {
        //处理删除事件

        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            int selectionStart = Selection.getSelectionStart(getText());
            int selectionEnd = Selection.getSelectionEnd(getText());
            if (selectionEnd != selectionStart) {
                return false;
            }
            IntegratedSpan[] integratedSpans = getText().getSpans(selectionStart, selectionEnd, IntegratedSpan.class);
            if (integratedSpans != null && integratedSpans.length > 0) {
                //部分设备上的span是无序的，所以需要遍历一遍找可以删除的span
                for (IntegratedSpan span : integratedSpans) {
                    int spanStart = getText().getSpanStart(span);
                    int spanEnd = getText().getSpanEnd(span);
                    if (spanEnd == selectionStart) {
//                        text.delete(spanStart, spanEnd);
                        Log.i("wdd", "selectionStart = " + selectionStart + " spanEnd = " + spanEnd);
                        setSelection(spanStart, spanEnd);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new CustomInputConnectionWrapper(super.onCreateInputConnection(outAttrs), true);
    }


    /**
     * 解决google输入法删除不走OnKeyListener()回调问题
     */
    private class CustomInputConnectionWrapper extends InputConnectionWrapper {


        /**
         * Initializes a wrapper.
         *
         * <p><b>Caveat:</b> Although the system can accept {@code (InputConnection) null} in some
         * places, you cannot emulate such a behavior by non-null {@link InputConnectionWrapper} that
         * has {@code null} in {@code target}.</p>
         *
         * @param target  the {@link InputConnection} to be proxied.
         * @param mutable set {@code true} to protect this object from being reconfigured to target
         *                another {@link InputConnection}.  Note that this is ignored while the target is {@code
         *                null}.
         */
        public CustomInputConnectionWrapper(InputConnection target, boolean mutable) {
            super(target, mutable);
        }


        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,
                        KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            return (handleKeyEvent(event))
                    || super.sendKeyEvent(event);
        }
    }

}
