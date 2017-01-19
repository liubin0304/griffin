package com.forbidden.griffin.tools;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 * TextView的字体设置，包括让其中部分字体颜色改变，大小改变等
 */
public class GinFontBuild {
    /**
     * 源字符
     */
    private static SpannableString mSrcFont;
    private static GinFontBuild mInstance;

    private GinFontBuild() {

    }

    public static GinFontBuild build(SpannableString src) {
        mSrcFont = src;
        return newInstance();
    }

    private static GinFontBuild newInstance() {
        return new GinFontBuild();
    }

    public SpannableString create() {
        return mSrcFont;
    }

    public static GinFontBuild build(String src) {
        return build(new SpannableString(src));
    }

    /**
     * 设置字体大小
     *
     * @param start 要设置的字体开始的下标
     * @param end   要设置的字体结束的小标
     */
    public GinFontBuild setFontSize(int size, int start, int end) {
        //设置字体大小（绝对值,单位：像素）
        mSrcFont.setSpan(new AbsoluteSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置字体大小
     *
     * @param size
     * @param tagStr
     */
    public GinFontBuild setFontSize(int size, SpannableString tagStr) {
        int start = mSrcFont.toString().indexOf(tagStr.toString());
        int end = start + tagStr.length();
        return setFontSize(size, start, end);
    }

    /**
     * 设置字体大小
     *
     * @param size
     * @param tagStr
     */
    public GinFontBuild setFontSize(int size, String tagStr) {
        return setFontSize(size, new SpannableString(tagStr));
    }

    /**
     * 设置字体颜色
     *
     * @param color
     * @param start
     * @param end
     */
    public GinFontBuild setFontColor(int color, int start, int end) {
        mSrcFont.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置字体颜色
     *
     * @param color
     * @param tagStr
     */
    public GinFontBuild setFontColor(int color, SpannableString tagStr) {
        int start = mSrcFont.toString().indexOf(tagStr.toString());
        int end = start + tagStr.length();
        return setFontColor(color, start, end);
    }

    /**
     * 设置字体颜色
     *
     * @param color
     * @param tagStr
     */
    public GinFontBuild setFontColor(int color, String tagStr) {
        return setFontColor(color, new SpannableString(tagStr));
    }

    /**
     * 设置颜色和字体
     *
     * @param color
     * @param size
     * @param start
     * @param end
     * @return
     */
    public GinFontBuild setFontColorAndSize(int color, int size, int start, int end) {
        return setFontSize(size, start, end).setFontColor(color, start, end);
    }

    /**
     * 设置颜色和字体
     *
     * @param color
     * @param size
     * @param tagStr
     * @return
     */
    public GinFontBuild setFontColorAndSize(int color, int size, SpannableString tagStr) {
        int start = mSrcFont.toString().indexOf(tagStr.toString());
        int end = start + tagStr.length();
        return setFontColorAndSize(color, size, start, end);
    }

    /**
     * 设置颜色和字体
     *
     * @param color
     * @param size
     * @param tagStr
     * @return
     */
    public GinFontBuild setFontColorAndSize(int color, int size, String tagStr) {
        return setFontColorAndSize(color, size, new SpannableString(tagStr));
    }
}
