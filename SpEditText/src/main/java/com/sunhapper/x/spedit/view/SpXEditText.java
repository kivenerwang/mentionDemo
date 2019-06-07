package com.sunhapper.x.spedit.view;

import android.content.Context;
import android.text.Editable;
import android.text.NoCopySpan;
import android.text.Selection;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
        setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        return true;
    }

    @Override
    public boolean isSuggestionsEnabled() {
        return false;
    }

    boolean canPaste()
    {
        return false;
    }

    private boolean handleKeyEvent(KeyEvent keyEvent) {
        //处理删除事件
        Editable text = getText();
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            int selectionStart = Selection.getSelectionStart(text);
            int selectionEnd = Selection.getSelectionEnd(text);
            if (selectionEnd != selectionStart) {
                return false;
            }
            IntegratedSpan[] integratedSpans = text.getSpans(selectionStart, selectionEnd, IntegratedSpan.class);
            if (integratedSpans != null && integratedSpans.length > 0) {
                //部分设备上的span是无序的，所以需要遍历一遍找可以删除的span
                for (IntegratedSpan span : integratedSpans) {
                    int spanStart = text.getSpanStart(span);
                    int spanEnd = text.getSpanEnd(span);
                    if (spanEnd == selectionStart) {
                        Selection.setSelection(text, selectionStart, spanStart);
                        return true;
                    }
                }
            }
        }

        //处理光标左移事件
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT
                && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

            int selectionStart = Selection.getSelectionStart(text);
            int selectionEnd = Selection.getSelectionEnd(text);
            IntegratedSpan[] integratedSpans = text.getSpans(selectionEnd, selectionEnd, IntegratedSpan.class);
            if (integratedSpans != null && integratedSpans.length > 0) {
                for (IntegratedSpan span : integratedSpans) {
                    int spanStart = text.getSpanStart(span);
                    int spanEnd = text.getSpanEnd(span);
                    //selectionEnd表示主动移动的光标
                    if (spanEnd == selectionEnd) {
                        Selection.setSelection(text, selectionStart, spanStart);
                        return true;
                    }
                }
            }
        }
        //处理光标右移事件
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT
                && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            int selectionStart = Selection.getSelectionStart(text);
            int selectionEnd = Selection.getSelectionEnd(text);
            IntegratedSpan[] integratedSpans = text.getSpans(selectionEnd, selectionEnd, IntegratedSpan.class);
            if (integratedSpans != null && integratedSpans.length > 0) {
                for (IntegratedSpan span : integratedSpans) {
                    int spanStart = text.getSpanStart(span);
                    int spanEnd = text.getSpanEnd(span);
                    if (spanStart == selectionEnd) {
                        Selection.setSelection(text, selectionStart, spanEnd);
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
