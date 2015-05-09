package com.shine.look.weibo.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

/**
 * User:Shine
 * Date:2015-05-09
 * Description:
 */
public class TopicActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("话题");
        setContentView(textView);
    }
}
