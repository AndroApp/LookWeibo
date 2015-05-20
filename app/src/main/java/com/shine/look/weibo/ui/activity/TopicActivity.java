package com.shine.look.weibo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.shine.look.weibo.R;
import com.shine.look.weibo.ui.utils.AnimationUtils;
import com.shine.look.weibo.ui.utils.WeiboUrlSpan;
import com.shine.look.weibo.utils.Constants;

/**
 * User:Shine
 * Date:2015-05-09
 * Description:
 */
public class TopicActivity extends BaseActivity {

    private LinearLayout ll;

    private boolean mBackIsMenu;
    private int mBackMenuResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ll = (LinearLayout) findViewById(R.id.ll);
        initializeToolbar();
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (savedInstanceState == null && intent != null) {
            final int drawingStartLocation = intent.getIntExtra(WeiboUrlSpan.START_LOCATION, 0);
            mBackIsMenu = intent.getBooleanExtra(Constants.ARG_TOOLBAR_IS_MENU, true);
            mBackMenuResId = intent.getIntExtra(Constants.ARG_TOOLBAR_MENU_RES_ID, 0);
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

    @Override
    public boolean toolBarIsMenu() {
        return false;
    }

    @Override
    public int toolBarMenuResId() {
        return R.drawable.ic_action_more;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (mBackIsMenu) {
            //将菜单图标变为箭头图标
            AnimationUtils.menuToArrowAnimator(getDrawerToggle(), getDrawerLayout());
        } else {
            getDrawerToggle().onDrawerOpened(getDrawerLayout());
        }
        //覆盖箭头图标的点击事件
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        final ImageButton btn = (ImageButton) getInboxMenuItem().getActionView();
        btn.setScaleY(0);
        btn.setScaleX(0);
        btn.setImageResource(mBackMenuResId);
        AnimationUtils.scaleToOriginalAnimator(btn);
        if (mBackIsMenu) {
            AnimationUtils.arrowToMenuAnimator(getDrawerToggle(), getDrawerLayout());
        }
        AnimationUtils.closeEnterDownAnimation(ll, getToolbar(), this);
    }
}
