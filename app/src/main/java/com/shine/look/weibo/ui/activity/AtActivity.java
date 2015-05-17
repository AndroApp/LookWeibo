package com.shine.look.weibo.ui.activity;

import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.shine.look.weibo.R;
import com.shine.look.weibo.ui.utils.AnimationUtils;
import com.shine.look.weibo.ui.utils.WeiboTextUrlSpan;

import butterknife.InjectView;

/**
 * User:Shine
 * Date:2015-05-09
 * Description:
 */
public class AtActivity extends BaseActivity {

    @InjectView(R.id.ll)
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at);
        initializeToolbar();
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            final int drawingStartLocation = getIntent().getIntExtra(WeiboTextUrlSpan.START_LOCATION, 0);
            ll.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    ll.getViewTreeObserver().removeOnPreDrawListener(this);
                    AnimationUtils.openEnterFromYAnimation(ll, getToolbar(), drawingStartLocation);
                    return true;
                }
            });
        }
    }
}
