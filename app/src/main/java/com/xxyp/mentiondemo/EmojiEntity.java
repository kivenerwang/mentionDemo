package com.xxyp.mentiondemo;

public class EmojiEntity {
    private String name;
    private int unicode;

    public EmojiEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnicode() {
        return String.valueOf(Character.toChars(unicode));
    }

    public void setUnicode(int unicode) {
        this.unicode = unicode;
    }

    @Override
    public String toString() {
        return "EmojiEntity{" +
                "name='" + name + '\'' +
                ", unicode=" + unicode +
                '}';
    }
}
