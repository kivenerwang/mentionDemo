package com.xxyp.mentiondemo.mention;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.xxyp.mentiondemo.R;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MentionActivity extends AppCompatActivity{
    private static final int REQUEST_CODE = 2;

    public static final String DEFAULT_METION_TAG = "@";
    public static final String DEFAULT_MENTION_PATTERN = "@[\\u4e00-\\u9fa5\\w\\-]+ ";
    public static final String DEFAULT_METION_TAG_1 = "#";
    public static final String DEFAULT_MENTION_PATTERN_1 = "#[\\u4e00-\\u9fa5\\w\\-]+ ";
    private Map<String, Pattern> mPatternMap = new HashMap<>();
    /**
     * set regularExpression by tag
     *
     * @param pattern regularExpression
     */
    public void setPattern(String tag, String pattern) {
        addPattern(tag, pattern);
    }

    /**
     * add regularExpression by tag
     * @param tag   set tag for regularExpression
     * @param pattern   regularExpression
     */
    public void addPattern(String tag, String pattern) {
        mPatternMap.put(tag, Pattern.compile(pattern));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_mention);
        Fragment fragment = ReleaseFragment.newInstance();
        Fragment fragment1 = MentionFragment.newInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.mention_contain, fragment, "aa").commitAllowingStateLoss();
    }
    private static final int REQUEST_CODE1 = 2;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ReleaseFragment fragment = (ReleaseFragment) getSupportFragmentManager().getFragments().get(0);
        if (resultCode == REQUEST_CODE1) {
            fragment.updateMentionUI(data);
        }
    }
}
