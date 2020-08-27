package com.netease.lib_common_ui.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SpanUtil {
    private final String TAG = this.getClass().getSimpleName();
    /* 默认标识 */
    private final int DEF_SPAN_FLAG = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;
    private int mSpanFlag;
    private SpannableStringBuilder mBuilder;
    private Context mContext;

    private SpanUtil() {
        super();
        mSpanFlag = DEF_SPAN_FLAG;
        mBuilder = new SpannableStringBuilder();
    }

    /**
     * 创建一个 SpanUtil 实例
     *
     * @return {@link SpanUtil}
     */
    public static SpanUtil newInstance() {
        return new SpanUtil();
    }

    /**
     * 设置标识
     *
     * @param flag <ul>
     *             <li>{@link Spannable#SPAN_INCLUSIVE_EXCLUSIVE}:
     *             <p>前后不包括在内，也就是说，在前面和后面插入的新字符不会应用新样式</p>
     *             </li>
     *             <li>{@link Spannable#SPAN_EXCLUSIVE_INCLUSIVE}:
     *             <p>不包括前面，但是包括后面，在后面插入的新字符会应用新样式</p>
     *             </li>
     *             <li>{@link Spannable#SPAN_INCLUSIVE_EXCLUSIVE}:
     *             <p>包括前面，不包括后面</p>
     *             </li>
     *             <li>{@link Spannable#SPAN_INCLUSIVE_INCLUSIVE}:
     *             <p>同时包括前面和后面</p>
     *             </li>
     *             </ul>
     * @return {@link SpanUtil}
     */
    public SpanUtil setFlag(int flag) {
        this.mSpanFlag = flag;
        return this;
    }

    /**
     * 添加文本内容
     *
     * @param text 文本内容
     * @return {@link SpanUtil}
     */
    public SpanUtil appendText(CharSequence text) {
        mBuilder.append(text);
        return this;
    }

    /**
     * 设置字体大小
     *
     * @param size  字体大小
     * @param start 起始位置
     * @param end   结束位置
     * @return {@link SpanUtil}
     */
    public SpanUtil setFontSize(int size, int start, int end) {
        mBuilder.setSpan(new AbsoluteSizeSpan(size), start, end, mSpanFlag);
        return this;
    }

    /**
     * 设置字体大小
     *
     * @param size 字体大小
     * @param key  关键字
     * @return {@link SpanUtil}
     */
    public SpanUtil setFontSizeByKey(int size, String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] item : list) {
            setFontSize(size, item[0], item[1]);
        }
        return this;
    }

    /**
     * 设置字体大小
     *
     * @param size  字体大小
     * @param key   关键字
     * @param index 如果关键字有多个，选择指定 index 下标的关键字（从 0 开始）
     * @return {@link SpanUtil}
     */
    public SpanUtil setFontSizeByKey(int size, String key, int index) {
        List<int[]> list = searchAllIndex(key);
        if (index >= 0 && index < list.size()) {
            setFontSize(size, list.get(index)[0], list.get(index)[1]);
        }
        return this;
    }


    /**
     * 设置文字颜色
     *
     * @param color 文字颜色
     * @param start 文字颜色的起始位置
     * @param end   文字颜色结束位置
     * @return {@link SpanUtil}
     */
    public SpanUtil setFontColor(@ColorInt int color, int start, int end) {
        mBuilder.setSpan(new ForegroundColorSpan(color), start, end, mSpanFlag);
        return this;
    }

    /**
     * 设置文字颜色
     *
     * @param color 文字颜色
     * @param key   关键字
     * @return {@link SpanUtil}
     */
    public SpanUtil setFontColorByKey(@ColorInt int color, String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] item : list) {
            setFontColor(color, item[0], item[1]);
        }
        return this;
    }

    /**
     * 设置文字颜色
     *
     * @param color 文字颜色
     * @param key   关键字
     * @param index 如果关键字有多个，选择指定 index 下标的关键字（从 0 开始）
     * @return {@link SpanUtil}
     */
    public SpanUtil setFontColorByKey(@ColorInt int color, String key, int index) {
        List<int[]> list = searchAllIndex(key);
        if (index >= 0 && index < list.size()) {
            setFontColor(color, list.get(index)[0], list.get(index)[1]);
        }
        return this;
    }

    /**
     * 设置背景色
     *
     * @param color 文字背景颜色
     * @param start 背景颜色的起始位置
     * @param end   背景颜色的结束位置
     * @return {@link SpanUtil}
     */
    public SpanUtil setBgColor(@ColorInt int color, int start, int end) {
        mBuilder.setSpan(new BackgroundColorSpan(color), start, end, mSpanFlag);
        return this;
    }

    /**
     * 设置背景色
     *
     * @param color 文字背景颜色
     * @param key   被设置背景颜色的文字
     * @return {@link SpanUtil}
     */
    public SpanUtil setBgColorByKey(@ColorInt int color, String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] item : list) {
            setBgColor(color, item[0], item[1]);
        }
        return this;
    }

    /**
     * 设置背景色
     *
     * @param color 文字背景颜色
     * @param key   关键字
     * @param index 如果关键字有多个，选择指定 index 下标的关键字（从 0 开始）
     * @return {@link SpanUtil}
     */
    public SpanUtil setBgColorByKey(@ColorInt int color, String key, int index) {
        List<int[]> list = searchAllIndex(key);
        if (index >= 0 && index < list.size()) {
            setBgColor(color, list.get(index)[0], list.get(index)[1]);
        }
        return this;
    }

    /**
     * 对文字添加 URL 链接
     *
     * @param url   链接
     * @param start 被添加链接的文字的起始位置
     * @param end   被添加链接的文字的结束位置
     * @return {@link SpanUtil}
     */
    public SpanUtil setURL(@Nullable String url, int start, int end) {
        mBuilder.setSpan(new URLSpan(url), start, end, mSpanFlag);
        return this;
    }

    /**
     * 对文字添加 URL 链接
     *
     * @param url 链接
     * @param key 关键字
     * @return {@link SpanUtil}
     */
    public SpanUtil setURLByKey(@Nullable String url, String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] item : list) {
            setURL(url, item[0], item[1]);
        }
        return this;
    }

    /**
     * 对文字添加 URL 链接
     *
     * @param url   链接
     * @param key   关键字
     * @param index 如果关键字有多个，选择指定 index 下标的关键字（从 0 开始）
     * @return {@link SpanUtil}
     */
    public SpanUtil setURLByKey(@Nullable String url, String key, int index) {
        List<int[]> list = searchAllIndex(key);
        if (index >= 0 && index < list.size()) {
            setURL(url, list.get(index)[0], list.get(index)[1]);
        }
        return this;
    }

    /**
     * 设置文字字体样式，例如斜体
     *
     * @param style 字体样式 <ul>
     *              <li>{@link Typeface#NORMAL}</li>
     *              <li>{@link Typeface#BOLD}</li>
     *              <li>{@link Typeface#ITALIC}</li>
     *              <li>{@link Typeface#BOLD_ITALIC}</li>
     *              </ul>
     * @param start 被设置字体文字的起始位置
     * @param end   被设置字体文字的结束位置
     * @return {@link SpanUtil}
     */
    public SpanUtil setTypeface(int style, int start, int end) {
        mBuilder.setSpan(new StyleSpan(style), start, end, mSpanFlag);
        return this;
    }

    /**
     * 设置文字字体样式，例如斜体
     *
     * @param style 字体样式 <ul>
     *              <li>{@link Typeface#NORMAL}</li>
     *              <li>{@link Typeface#BOLD}</li>
     *              <li>{@link Typeface#ITALIC}</li>
     *              <li>{@link Typeface#BOLD_ITALIC}</li>
     *              </ul>
     * @param key   关键字
     * @return {@link SpanUtil}
     */
    public SpanUtil setTypefaceByKey(int style, String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            setTypeface(style, index[0], index[1]);
        }
        return this;
    }

    /**
     * 设置文字字体样式，例如斜体
     *
     * @param style 字体样式 <ul>
     *              <li>{@link Typeface#NORMAL}</li>
     *              <li>{@link Typeface#BOLD}</li>
     *              <li>{@link Typeface#ITALIC}</li>
     *              <li>{@link Typeface#BOLD_ITALIC}</li>
     *              </ul>
     * @param key   关键字
     * @param index 如果关键字有多个，选择指定 index 下标的关键字（从 0 开始）
     * @return {@link SpanUtil}
     */
    public SpanUtil setTypefaceByKey(int style, String key, int index) {
        List<int[]> list = searchAllIndex(key);
        if (index >= 0 && index < list.size()) {
            setTypeface(style, list.get(index)[0], list.get(index)[1]);
        }
        return this;
    }

    /**
     * 添加删除线
     *
     * @param start 被添加删除线的文字的起始位置
     * @param end   被添加删除线的文字的结束位置
     * @return {@link SpanUtil}
     */
    public SpanUtil setStrikethrough(int start, int end) {
        mBuilder.setSpan(new StrikethroughSpan(), start, end, mSpanFlag);
        return this;
    }

    /**
     * 添加删除线
     *
     * @param key 关键字
     * @return {@link SpanUtil}
     */
    public SpanUtil setStrikethroughByKey(String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            setStrikethrough(index[0], index[1]);
        }
        return this;
    }

    /**
     * 添加删除线
     *
     * @param key   关键字
     * @param index 如果关键字有多个，选择指定 index 下标的关键字（从0开始）
     * @return {@link SpanUtil}
     */
    public SpanUtil setStrikethroughByKey(String key, int index) {
        List<int[]> list = searchAllIndex(key);
        if (index >= 0 && index < list.size()) {
            setStrikethrough(list.get(index)[0], list.get(index)[1]);
        }
        return this;
    }

    /**
     * 用图片替代指定文字
     *
     * @param span  替代文字的图片样式
     * @param start 起始位置
     * @param end   结束位置
     * @return {@link SpanUtil}
     */
    public SpanUtil setImage(ImageSpan span, int start, int end) {
        mBuilder.setSpan(span, start, end, mSpanFlag);
        return this;
    }

    /**
     * 用图片替代指定文字
     *
     * @param span 替代文字的图片样式
     * @param key  关键字
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(ImageSpan span, String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            setImage(span, index[0], index[1]);
        }
        return this;
    }

    /**
     * 用图片替代指定文字
     *
     * @param span  替代文字的图片样式
     * @param key   关键字
     * @param index 如果关键字有多个，选择指定 index 下标的关键字（从 0 开始）
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(ImageSpan span, String key, int index) {
        List<int[]> list = searchAllIndex(key);
        if (index >= 0 && index < list.size()) {
            setImage(span, list.get(index)[0], list.get(index)[1]);
        }
        return this;
    }

    /**
     * 为指定文字添加点击事件
     *
     * @param start    起始位置
     * @param end      结束位置
     * @param listener 文字的点击事件
     * @return {@link SpanUtil}
     */
    public SpanUtil setClick(int start, int end, final OnTextClickListener listener) {
        return setClick(start, end, true, listener);
    }

    /**
     * 为指定文字添加点击事件
     *
     * @param start           起始位置
     * @param end             结束位置
     * @param isNeedUnderLine 文字是否需要添加下划线
     * @param listener        文字的点击事件
     * @return {@link SpanUtil}
     */
    public SpanUtil setClick(int start, int end, final boolean isNeedUnderLine, final OnTextClickListener listener) {
        mBuilder.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(isNeedUnderLine);
            }

            @Override
            public void onClick(View widget) {
                if (listener != null) {
                    listener.onTextClick(-1, widget);
                }
            }
        }, start, end, mSpanFlag);
        return this;
    }


    /**
     * 为指定文字添加点击事件
     *
     * @param start     起始位置
     * @param end       结束位置
     * @param lineColor 下划线颜色
     * @param listener  文字的点击事件
     * @return {@link SpanUtil}
     */
    public SpanUtil setClick(int start, int end, @ColorInt final int lineColor, final OnTextClickListener listener) {
        return setClick(start, end, true, lineColor, listener);
    }

    /**
     * 为指定文字添加点击事件
     *
     * @param start           起始位置
     * @param end             结束位置
     * @param isNeedUnderLine 文字是否需要添加下划线
     * @param lineColor       下划线颜色
     * @param listener        文字的点击事件
     * @return {@link SpanUtil}
     */
    public SpanUtil setClick(int start, int end, final boolean isNeedUnderLine, @ColorInt final int lineColor, final OnTextClickListener listener) {
        mBuilder.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(isNeedUnderLine);
                if (isNeedUnderLine) {
                    ds.setColor(lineColor);
                }
            }

            @Override
            public void onClick(View widget) {
                if (listener != null) {
                    listener.onTextClick(-1, widget);
                }
            }
        }, start, end, mSpanFlag);
        return this;
    }

    /**
     * 为指定文字添加点击事件
     *
     * @param key             关键字
     * @param isNeedUnderLine 文字是否需要添加下划线
     * @param listener        文字的点击事件
     * @return {@link SpanUtil}
     */
    public SpanUtil setClickByKey(String key, final boolean isNeedUnderLine, final OnTextClickListener listener) {
        List<int[]> list = searchAllIndex(key);
        for (int i = 0; i < list.size(); i++) {
            final int finalI = i;
            mBuilder.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(isNeedUnderLine);
                }

                @Override
                public void onClick(View widget) {
                    if (listener != null) {
                        listener.onTextClick(finalI, widget);
                    }
                }
            }, list.get(i)[0], list.get(i)[1], mSpanFlag);
        }
        return this;
    }

    /**
     * 为指定文字添加点击事件
     *
     * @param key       关键字
     * @param lineColor 下划线颜色
     * @param listener  文字的点击事件
     * @return {@link SpanUtil}
     */
    public SpanUtil setClickByKey(String key, @ColorInt final int lineColor, final OnTextClickListener listener) {
        return setClickByKey(key, true, lineColor, listener);
    }

    /**
     * 为指定文字添加点击事件
     *
     * @param key             关键字
     * @param isNeedUnderLine 文字是否需要添加下划线
     * @param lineColor       下划线颜色
     * @param listener        文字的点击事件
     * @return {@link SpanUtil}
     */
    public SpanUtil setClickByKey(String key, final boolean isNeedUnderLine, @ColorInt final int lineColor, final OnTextClickListener listener) {
        List<int[]> list = searchAllIndex(key);
        for (int i = 0; i < list.size(); i++) {
            final int finalI = i;
            mBuilder.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(isNeedUnderLine);
                    if (isNeedUnderLine) {
                        ds.setColor(lineColor);
                    }
                }

                @Override
                public void onClick(View widget) {
                    if (listener != null) {
                        listener.onTextClick(finalI, widget);
                    }
                }
            }, list.get(i)[0], list.get(i)[1], mSpanFlag);
        }
        return this;
    }

    /**
     * 为指定文字添加点击事件
     *
     * @param key             关键字
     * @param index           如果被替换的文字有相同的多个，通过 index 选择是当中第几个（从 0 开始）
     * @param isNeedUnderLine 文字是否需要添加下划线
     * @param listener        文字的点击事件
     * @return {@link SpanUtil}
     */
    public SpanUtil setClickByKey(String key, final int index, final boolean isNeedUnderLine, final OnTextClickListener listener) {
        List<int[]> list = searchAllIndex(key);
        if (index >= 0 && index < list.size()) {
            mBuilder.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(isNeedUnderLine);
                }

                @Override
                public void onClick(View widget) {
                    if (listener != null) {
                        listener.onTextClick(index, widget);
                    }
                }
            }, list.get(index)[0], list.get(index)[1], mSpanFlag);
        }
        return this;
    }

    /**
     * 为指定文字添加点击事件
     *
     * @param key       关键字
     * @param index     如果被替换的文字有相同的多个，通过 index 选择是当中第几个（从 0 开始）
     * @param lineColor 下划线颜色
     * @param listener  文字的点击事件
     * @return {@link SpanUtil}
     */
    public SpanUtil setClickByKey(String key, final int index, @ColorInt final int lineColor, final OnTextClickListener listener) {
        return setClickByKey(key, index, true, lineColor, listener);
    }

    /**
     * 为指定文字添加点击事件
     *
     * @param key             关键字
     * @param index           如果被替换的文字有相同的多个，通过 index 选择是当中第几个（从 0 开始）
     * @param isNeedUnderLine 文字是否需要添加下划线
     * @param lineColor       下划线颜色
     * @param listener        文字的点击事件
     * @return {@link SpanUtil}
     */
    public SpanUtil setClickByKey(String key, final int index, final boolean isNeedUnderLine, @ColorInt final int lineColor, final OnTextClickListener listener) {
        List<int[]> list = searchAllIndex(key);
        if (index >= 0 && index < list.size()) {
            mBuilder.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(isNeedUnderLine);
                    if (isNeedUnderLine) {
                        ds.setColor(lineColor);
                    }
                }

                @Override
                public void onClick(View widget) {
                    if (listener != null) {
                        listener.onTextClick(index, widget);
                    }
                }
            }, list.get(index)[0], list.get(index)[1], mSpanFlag);
        }
        return this;
    }

    /**
     * 执行
     *
     * @param textView textview 对象
     */
    public void build(TextView textView) {
        if (textView != null) {
            textView.setText(mBuilder);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    /**
     * 获取 SpannableStringBuilder
     *
     * @return SpannableStringBuilder 对象
     */
    public SpannableStringBuilder getBuilder() {
        if (mBuilder == null) return null;
        return mBuilder;
    }

    /**
     * 查询字符串中的所有关键字的位置
     *
     * @param key 关键字
     * @return 关键字位置的集合
     */
    public List<int[]> searchAllIndex(String key) {
        List<int[]> indexList = new ArrayList<int[]>();
        String text = mBuilder.toString();
        if (TextUtils.isEmpty(text)) {
            return indexList;
        }
        int a = text.indexOf(key);
        while (a != -1) {
            int[] index = new int[]{a, a + key.length()};
            indexList.add(index);
            a = text.indexOf(key, a + 1);
        }
        return indexList;
    }

    /**
     * 文字点击事件监听器
     */
    public interface OnTextClickListener {
        /**
         * @param position 点击文字在字符串中的位置
         * @param v        view 对象
         */
        void onTextClick(int position, View v);
    }
}