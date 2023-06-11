package com.zrt.kotlinapp.tools;

import androidx.annotation.IntRange;

/**

 */
/**
 * 以模式 AT_MOST 举例. 转成二进制 为           0000 0000 0000 0000 0000 0000 0000 0010
 * 那么把 10 向左移动 30位, 变成                       1000 0000 0000 0000 0000 0000 0000 0000
 * 比如值为 4, 转成二进制是 100,
 * 那他们组合后就变成一个 32位的二进制数据. 1000 0000 0000 0000 0000 0000 0000 0100
 * 这样就同时包含了模式与大小.
 * 作者：__Y_Q
 * 链接：https://www.jianshu.com/p/ba4de9a24b8b
 * 来源：简书
 * @author：Zrt
 * @date: 2022/11/22
 */
public class MyMeasureSpec {

    private static final int MODE_SHIFT = 30;
    // 二进制：11000000000000000000000000000000
    private static final int MODE_MASK = 0x3 << MODE_SHIFT; // -1073741824

    /**
     * Measure specification mode: The parent has not imposed any constraint on the
     * child. It can be whatever size it wants.
     * 不对View大小做限制，如：ListView，ScrollView. 这个在工作中极少碰到, 一般在系统中才会使用到.
     * 二进制：00000000000000000000000000000000
     * 十进制：0
     */
    public static final int UNSPECIFIED = 0 << MODE_SHIFT; // 0

    /**
     * Measure specification mode: The parent has determined an exact size for the
     * child. The child is going to be given those bounds regardless of how big it
     * wants to be.
     * 精确值模式，如：确切的数值或者 march_parent
     * 二进制：01000000000000000000000000000000
     * 十进制：1
     */
    public static final int EXACTLY = 1 << MODE_SHIFT; // 1073741824

    /**
     * Measure specification mode: The child can be as large as it wants up to the
     * specified size.
     * 最大值模式，如：wrap_content.
     * 二进制：10000000000000000000000000000000
     * 十进制：2
     */
    public static final int AT_MOST = 2 << MODE_SHIFT; // -2147483648

    public static int makeMeasureSpec(@IntRange(from = 0, to = (1 << MODE_SHIFT) - 1) int size,
            int mode) {
        println("##makeMeasureSpec true="+(size + mode)+"; false="+((size & ~MODE_MASK) | (mode & MODE_MASK)));
//		if (sUseBrokenMakeMeasureSpec) {
//			return size + mode;
//		} else {
        return (size & ~MODE_MASK) | (mode & MODE_MASK);
//		}
    }
    public static int getMode(int measureSpec) {
        //noinspection ResourceType
        //xx0000...00  & 11000000000000000000000000000000
        return (measureSpec & MODE_MASK);
    }

    public static int getSize(int measureSpec) {
        return (measureSpec & ~MODE_MASK); // ~MODE_MASK 二进制取反
    }

    public static void main(String[] args) {
        println("MODE_SHIFT="+MODE_SHIFT);
        println("MODE_MASK="+MODE_MASK); // -1073741824
        println("~MODE_MASK="+~MODE_MASK); // 1073741823
        println("UNSPECIFIED="+UNSPECIFIED); // 0
        println("EXACTLY="+EXACTLY); //1073741824
        println("AT_MOST="+AT_MOST); //-2147483648

        makeMeasureSpec(1, UNSPECIFIED); //true=1; false=1
        makeMeasureSpec(1, EXACTLY); //true=1073741825; false=1073741825
        makeMeasureSpec(1, AT_MOST); // true=-2147483647; false=-2147483647
        int a = -2147483648;
        int b = 2147483647;
        println("getSize(100)="+getSize(100)); // 100
        println("getSize(EXACTLY)="+getSize(EXACTLY)); // 0
        println("getSize(EXACTLY+1)="+getSize(EXACTLY+1)); // 1
        println("getSize(EXACTLY+100)="+getSize(EXACTLY+100)); // 100
        println("getSize(AT_MOST+10)="+getSize(AT_MOST+10)); // 10
        println("getSize(AT_MOST+100)="+getSize(AT_MOST+100)); // 100

        println("getMode(EXACTLY-1)="+getMode(EXACTLY-1)); // 0
        println("getMode(EXACTLY)="+getMode(EXACTLY)); // 1073741824
        println("getMode(EXACTLY+10)="+getMode(EXACTLY+10)); // 1073741824
        println("getMode(EXACTLY+100)="+getMode(EXACTLY+100)); // 1073741824
        println("getMode(AT_MOST)="+getMode(AT_MOST)); // -2147483648
        println("getMode(AT_MOST-100)="+getMode(AT_MOST-100)); // 1073741824
        println("getMode(AT_MOST+100)="+getMode(AT_MOST+100)); // -2147483648
        println("##Integer.MAX_VALUE >> 2="+(Integer.MAX_VALUE >> 2)); // 536870911
        println(makeMeasureSpec(Integer.MAX_VALUE >> 2, AT_MOST)); // true=-1610612737; false=-1610612737
        println("##getMode="+getMode(makeMeasureSpec(Integer.MAX_VALUE >> 2, AT_MOST)));
        println("##getSize="+getSize(makeMeasureSpec(Integer.MAX_VALUE >> 2, AT_MOST)));
        println("##ceil="+Math.ceil(1/2f));
    }
    public static void println(String param) {
        System.out.println(param);
    }
    public static void println(int param) {
        System.out.println(String.valueOf(param));
    }

}
