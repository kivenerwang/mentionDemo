package com.xxyp.mentiondemo.mention;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.xxyp.mentiondemo.EmojiEntity;
import com.xxyp.mentiondemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EmojiKeyboard extends FrameLayout {

    private EmojiPagerAdapter emojiPagerAdapter;
    private EditText mEditText;
    private ImageView ivSwitchIcon;
    private int dp32;

    public EmojiKeyboard(@NonNull Context context) {
        this(context, null);
    }

    public EmojiKeyboard(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmojiKeyboard(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        dp32 = DensityUtil.dip2px(context, 32);
        init();
    }
    public static String readAssetsFile(Context context, String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);
            int fileLength = is.available();
            byte[] buffer = new byte[fileLength];
            int read = is.read(buffer);
            is.close();
            return new String(buffer, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "cause error: please check if the file exists";
    }
    public static List<EmojiEntity> parseEmojiList(String json) {
        List<EmojiEntity> emojiEntityList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.optJSONArray("emoji_list");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                    if (jsonObject1 != null) {
                        EmojiEntity mEmojiEntity = new EmojiEntity();
                        mEmojiEntity.setName(jsonObject1.optString("name", ""));
                        mEmojiEntity.setUnicode(jsonObject1.optInt("unicode", 0));
                        emojiEntityList.add(mEmojiEntity);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return emojiEntityList;
    }
    private void initKeyboard() {
        if (views == null) {
            views = new ArrayList<>();
        }
        views.clear();

        int screenWidth = DensityUtil.getScreenWidth(getContext());
        int emojiWidth = screenWidth / 7;
        int emojiHeight = 100;
        List<EmojiEntity> emojiEntities = parseEmojiList(readAssetsFile(getContext(), "EmojiList.json"));

        FlexboxLayout flexboxLayout = genFlexboxLayout();
        for (int i = 0; i < emojiEntities.size(); i++) {
            if ((i + 1) % 21 != 0) {
                EmojiEntity emoji = emojiEntities.get(i);
                TextView emojiView = genEmojiView(emoji.getUnicode(), emojiWidth, emojiHeight);
                flexboxLayout.addView(emojiView);
            } else {
                View backKey = genBackKeyView(emojiWidth, emojiHeight);
                flexboxLayout.addView(backKey);

                views.add(flexboxLayout);
                flexboxLayout = genFlexboxLayout();
            }
        }
        emojiPagerAdapter.notifyDataSetChanged();
    }

    private FlexboxLayout genFlexboxLayout() {
        FlexboxLayout flexboxLayout = new FlexboxLayout(getContext());
        flexboxLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        flexboxLayout.setFlexWrap(FlexWrap.WRAP);
        return flexboxLayout;
    }

    private TextView genEmojiView(final String unicode, int emojiWidth, int emojiHeight) {
        TextView view = new TextView(getContext());
        view.setText(unicode);
        view.setTextSize(25);
        view.setTextColor(Color.BLACK);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(emojiWidth, emojiHeight);
        view.setLayoutParams(layoutParams);
        view.setGravity(Gravity.CENTER);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText != null) {
                    Editable editable = mEditText.getText();
                    int start = mEditText.getSelectionStart();
                    int end = mEditText.getSelectionEnd();
                    editable.replace(start, end, unicode);
                }
            }
        });
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private View genBackKeyView(int width, int height) {

        ImageView ivBack = new ImageView(getContext());
        FrameLayout.LayoutParams layoutParams = new LayoutParams(dp32, dp32);
        layoutParams.gravity = Gravity.CENTER;
        ivBack.setLayoutParams(layoutParams);
        ivBack.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.keyb_emojy_delet_nol));

        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        frameLayout.addView(ivBack);

        ivBack.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int keyCode = KeyEvent.KEYCODE_DEL;
                KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
                KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mEditText.onKeyDown(keyCode, keyEventDown);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mEditText.onKeyUp(keyCode, keyEventUp);
                }
                return false;
            }
        });
        return frameLayout;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void bind(@NonNull final EditText editText, @Nullable ImageView switchIcon) {
        this.mEditText = editText;
        this.ivSwitchIcon = switchIcon;

        mEditText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showEmojiKeyboard(false);
                    showSystemKeyboard(mEditText);
                }
                return false;
            }
        });

        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v instanceof EditText) {
                    if (hasFocus) {
                        showSystemKeyboard(editText);
                    }
                }
            }
        });
    }

    public void bindV2(@NonNull final EditText editText) {
        this.mEditText = editText;
    }

    private static final long SHOW_TIME = 200;

    /**
     * 表情键盘是否展示
     * @return true/false
     */
    public boolean isEmojiKeyboardShown() {
        return this.getVisibility() == VISIBLE;
    }

    /**
     * 切换表情和系统键盘
     */
    public void switchKeyboard() {
        if (mEditText == null) {
            throw new RuntimeException("must bind EditText first!!!");
        }

        if (isEmojiKeyboardShown()) {
            showEmojiKeyboard(false);
            showSystemKeyboard(mEditText);
        } else {
            hideSystemKeyBoard(mEditText);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showEmojiKeyboard(true);
                }
            }, SHOW_TIME);
        }

        if (ivSwitchIcon != null) {
            // TODO: 2019/5/18 切换emoji和键盘的 icon
        }
    }

    /**
     * 展示/隐藏 表情键盘
     * @param show 展示/隐藏
     */
    public void showEmojiKeyboard(boolean show) {
        setVisibility(show ? VISIBLE : GONE);
    }

    /**
     * 显示系统软件盘
     * @param editText editText
     */
    private void showSystemKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            editText.requestFocus();
            imm.showSoftInput(editText, 0);
        }
    }

    /**
     * 隐藏系统键盘
     * @param edit edit
     */
    private void hideSystemKeyBoard(EditText edit) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null)
            return;
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
        }

        int currentVersion = Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            methodName = "setSoftInputShownOnFocus";
        }

        if (methodName == null) {
            edit.setInputType(0);
        } else {
            try {
                Method setShowSoftInputOnFocus = EditText.class.getMethod(methodName, Boolean.TYPE);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(edit, Boolean.FALSE);
            } catch (NoSuchMethodException e) {
                edit.setInputType(0);
                e.printStackTrace();
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    private void init() {
        setBackgroundColor(Color.WHITE);
        emojiPagerAdapter = new EmojiPagerAdapter();
        ViewPager viewPager = new ViewPager(getContext());
        viewPager.setBackgroundColor(Color.WHITE);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600);
        viewPager.setLayoutParams(layoutParams);
        viewPager.setAdapter(emojiPagerAdapter);
        addView(viewPager);

        initKeyboard();
    }

    private List<View> views;

    class EmojiPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return views == null ? 0 : views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
