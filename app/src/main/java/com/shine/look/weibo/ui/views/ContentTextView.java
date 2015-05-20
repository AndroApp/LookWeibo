package com.shine.look.weibo.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.shine.look.weibo.R;
import com.shine.look.weibo.ui.utils.WeiboUrlSpan;
import com.shine.look.weibo.utils.ExpressionMap;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * User:Shine
 * Date:2015-05-05
 * Description:
 */
public class ContentTextView extends TextView {

    public static final Pattern TOPIC_URL = Pattern.compile("#[\\p{InCJK_UNIFIED_IDEOGRAPHS}\\w\\p{Print}&&[^#]]+#");
    public static final Pattern AT_URL = Pattern.compile("@[\\p{InCJK_UNIFIED_IDEOGRAPHS}\\w-]{1,20}");
    public static final Pattern HTTP_URL = Pattern.compile("http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]");

    private static final String TOPIC_SCHEME = "com.shine.look.weibo.topic://";
    private static final String AT_SCHEME = "com.shine.look.weibo.at://";
    private static final String HTTP_SCHEME = "com.shine.look.weibo.browser://";

    /**
     * 被按下时的背景颜色
     */
    private int mBackgroundColor;
    /**
     * 透明背景，抬起时将背景恢复
     */
    private int mTransparentColor;
    /**
     * 部分文字被按下初始位置
     */
    private int mPressedStart;
    /**
     * 部分文字被按下结束位置
     */
    private int mPressedEnd;
    /**
     * 当前文字是否被按下
     */
    private boolean isPressed;

    public ContentTextView(Context context) {
        super(context);
        init();
    }

    public ContentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContentTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ContentTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mBackgroundColor = getResources().getColor(R.color.colorPrimaryLight);
        mTransparentColor = getResources().getColor(android.R.color.transparent);
        setClickable(true);
        //给url设置点击和颜色
        setMovementMethod(LinkMovementMethod.getInstance());
        //设置点击效果颜色
        setLinkTextColor(getResources().getColor(R.color.colorAccent));
    }


    public void dealWithText(CharSequence text) {
        Spannable spannable = new SpannableStringBuilder(text);
        //处理文字
        addLinks(spannable, TOPIC_URL, TOPIC_SCHEME);
        addLinks(spannable, AT_URL, AT_SCHEME);
        addLinks(spannable, HTTP_URL, HTTP_SCHEME);

        ExpressionMap.addExpression(spannable);
        setText(spannable);
    }

    private void addLinks(Spannable spannable, Pattern pattern, String scheme) {
        Matcher matcher = pattern.matcher(spannable);
        int start;
        int end = 0;
        String prefix = (scheme == null) ? "" : scheme.toLowerCase(Locale.ROOT);
        while (matcher.find(end)) {
            start = matcher.start();
            end = matcher.end();

            String url = makeUrl(matcher.group(0), new String[]{prefix});
            WeiboUrlSpan span = new WeiboUrlSpan(url);
            spannable.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

    private static final String makeUrl(String url, String[] prefixes) {
        boolean hasPrefix = false;

        for (int i = 0; i < prefixes.length; i++) {
            if (url.regionMatches(true, 0, prefixes[i], 0,
                    prefixes[i].length())) {
                hasPrefix = true;

                // Fix capitalization if necessary
                if (!url.regionMatches(false, 0, prefixes[i], 0,
                        prefixes[i].length())) {
                    url = prefixes[i] + url.substring(prefixes[i].length());
                }

                break;
            }
        }

        if (!hasPrefix) {
            url = prefixes[0] + url;
        }

        return url;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= getTotalPaddingLeft();
        y -= getTotalPaddingTop();

        x += getScrollX();
        y += getScrollY();

        Layout layout = getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);
        Spannable buffer = (Spannable) getText();
        ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (link.length != 0) {
                    link[0].onClick(this);
                    buffer.setSpan(new BackgroundColorSpan(mTransparentColor), mPressedStart, mPressedEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    isPressed = false;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                if (link.length != 0) {
                    mPressedStart = buffer.getSpanStart(link[0]);
                    mPressedEnd = buffer.getSpanEnd(link[0]);
                    buffer.setSpan(new BackgroundColorSpan(mBackgroundColor), mPressedStart, mPressedEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    isPressed = true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (isPressed) {
                    isPressed = false;
                    Spannable spannable = (Spannable) getText();
                    spannable.setSpan(new BackgroundColorSpan(mTransparentColor), mPressedStart, mPressedEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isPressed && link.length == 0) {
                    isPressed = false;
                    Spannable spannable = (Spannable) getText();
                    spannable.setSpan(new BackgroundColorSpan(mTransparentColor), mPressedStart, mPressedEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                break;
        }
        return true;
    }
}
