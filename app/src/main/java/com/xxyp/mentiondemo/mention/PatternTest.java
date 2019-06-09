package com.xxyp.mentiondemo.mention;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTest {
    public static void main(String[] args) {
//       test();
//        test1();
//        test2();
//        test3();
//        test4();
        test5();
        String strDesc = "#研祥科技大厦[地点]# #漕河泾现代服务业园区[地点]# #神仙水乳套装 神仙水100ml+乳液100ml+小样水30ml+乳30ml[商品]# 哈哈哈";
//        getData(strDesc);
    }

    private static void test5() {
        
    }

    private static final String TOPIC = "(?<=#)(\\S+)(?=#)";// ##话题
    private static final String TOPIC1 = "#([^#]+?)#";// ##话题
    private static final String TOPIC2 = "(?<=#)(\\S+)(?=#)";// ##话题
    private static final String POSITION = "\\[(?<=\\[)(\\S+)(?=\\])\\]";//[方括号]
    private static final String TOPIC3= "(?<=#)("+POSITION+")(?=#)";// ##话题
    private static final String POSITION1 = "(?<=\\[)(\\S+)(?=\\])";//[方括号]
    private static final String REGIX = "(" + TOPIC1 + ")" + "|" + "(" + POSITION + ")";
/*    public static List<String> getData(String data){

        List<String> list=new ArrayList<String>();
        String in = "Item(s): [item1.test],[item2.qa],[item3.production]";

        Pattern p = Pattern.compile();
        Matcher m = p.matcher(in);

        while(m.find()) {
            System.out.println(m.group(1));
        }
        return list;
    }*/
    private static void test4() {
        String strDesc = "#研祥科技大厦[地点]# #漕河泾现代服务业园区[地点]# #神仙水乳套装 神仙水100ml+乳液100ml+小样水30ml+乳30ml[商品]# 哈哈哈";
        Pattern pattern = Pattern.compile(REGIX,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(strDesc);
        while (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                System.out.println( "tpic = " + matcher.group(i));
            }
            final String topic = matcher.group();
            String  postion = matcher.group(1);

//           test(topic);
        }
    }

    //验证是否为邮箱地址
    private static void test3() {
        String str = "我是[001]真心求救的[002]，你能帮帮我吗";
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            System.out.println(matcher.group(1));
        }
    }

    //文字替换（首次出现字符）
    private static void test2() {
        Pattern pattern = Pattern.compile("正则表达式");
        Matcher matcher = pattern.matcher("正则表达式 Hello World,正则表达式 Hello World");
        //替换第一个符合正则的数据
        System.out.print(matcher.replaceFirst("Java"));
    }
    //以多条件分割字符串时
    private static void test1() {
        Pattern pattern = Pattern.compile("[, |]+");
        String[] strs = pattern.split("Java Hello World  Java,Hello,,World|Sun");
        for (int i=0;i<strs.length;i++) {
            System.out.println(strs[i]);
        }
    }
    //查找以Java开头,任意结尾的字符串
    private static void test(String topic) {
        Pattern pattern = Pattern.compile(POSITION,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(topic);
        boolean b= matcher.matches();  //当条件满足时，将返回true，否则返回false
        while (matcher.find()) {
            String position = matcher.group();
            String topicContet= topic.replace(position, "");
            System.out.println("topic = " +topicContet.substring(1, topicContet.length() -1)   + "->类型 = " + position);
        }
    }
}
