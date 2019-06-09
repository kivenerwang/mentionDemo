package com.xxyp.mentiondemo.mention;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupTest {
    private static final String AT = "@[\\u4e00-\\u9fa5\\w\\-*]+ ";// @人
    private static final String TOPIC1 = "#([^#]+?)#";// ##话题
    private static final String REGEX = "(" + AT + ")" + "|"+ "(" + TOPIC1 + ")";

    public static void main(String[] args) {
        test();
    }
    private static String desp = "@张林 @王东东 #张江高科[地点]#";
    private static void test() {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(desp);
        int i = 0;
        System.out.println("group count = " + matcher.groupCount());
        while (matcher.find()) {

            System.out.printf(" group1 =   " + matcher.group(1));
            System.out.printf(" group2 = " + matcher.group(2));
            System.out.println("group = " + matcher.group() + " count = " + i++ );


        }

    }
}
