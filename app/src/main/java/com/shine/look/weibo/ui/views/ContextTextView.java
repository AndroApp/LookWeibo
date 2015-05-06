package com.shine.look.weibo.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shine.look.weibo.R;

/**
 * User:Shine
 * Date:2015-05-05
 * Description:
 */
public class ContextTextView extends TextView {


    private SpannableString mSpannable;
    private int lightColor;


    public ContextTextView(Context context) {
        super(context);
        init();
    }

    public ContextTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContextTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ContextTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setClickable(true);
        setMovementMethod(LinkMovementMethod.getInstance());
        lightColor = getResources().getColor(R.color.colorAccent);
    }

    public void setContextText(String contextText) {
        mSpannable = new SpannableString(contextText);
        setTopicText(contextText);
        setAtText(contextText);
        setHttpText(contextText);
        setText(mSpannable);
    }

    private void setTopicText(String text) {
        int start;
        int end = 0;
        while ((start = text.indexOf('#', end)) != -1) {
            end = text.indexOf('#', start + 1) + 1;
            ClickableSpan topicTextClick = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Toast.makeText(getContext(), "话题被点击", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                    ds.clearShadowLayer();
                    ds.setColor(lightColor);
                }
            };
            mSpannable.setSpan(topicTextClick, start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void setAtText(String text) {
        int replyAtStart;
        int replyAtEnd = 0;
        while ((replyAtStart = text.indexOf("//@", replyAtEnd)) != -1) {
            replyAtStart += 2;
            replyAtEnd = text.indexOf(':', replyAtStart);
            ClickableSpan replyAtClick = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Toast.makeText(getContext(), "话题被点击", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                    ds.clearShadowLayer();
                    ds.setColor(lightColor);
                }
            };
            mSpannable.setSpan(replyAtClick, replyAtStart, replyAtEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        int atStart;
        int atEnd = 0;
        while ((atStart = text.indexOf(" @", atEnd)) != -1) {
            atEnd = text.indexOf(' ', atStart + 2);
            ClickableSpan atClick = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Toast.makeText(getContext(), "话题被点击", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                    ds.clearShadowLayer();
                    ds.setColor(lightColor);
                }
            };
            mSpannable.setSpan(atClick, atStart, atEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void setHttpText(String contextText) {

    }

}
