package com.shine.look.weibo.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.shine.look.weibo.R;
import com.shine.look.weibo.ui.fragment.HomeFragment;
import com.shine.look.weibo.utils.Utils;

/**
 * User:Shine
 * Date:2015-05-03
 * Description:主页面
 */
public class MainActivity extends BaseActivity {

    private static final int ANIM_DURATION_TOOLBAR = 300;


    private Fragment mCurrentFragment;
    private boolean mPendingIntroAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeToolbar();
        if (savedInstanceState == null) {
            mPendingIntroAnimation = true;
        }
    }

    @Override
    public boolean toolBarIsMenu() {
        return true;
    }

    @Override
    public int toolBarMenuResId() {
        return R.drawable.ic_action_publish;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        startIntroAnimation();
        return true;
    }


    public void startIntroAnimation() {
        if (mPendingIntroAnimation) {
            mPendingIntroAnimation = false;
            //将Toolbar隐藏
            int actionbarSize = Utils.dpToPx(56);
            getToolbar().setTranslationY(-actionbarSize);
            getIvLogo().setTranslationY(-actionbarSize);
            getInboxMenuItem().getActionView().setTranslationY(-actionbarSize);
            //开始入场动画
            getToolbar().animate()
                    .translationY(0)
                    .setDuration(ANIM_DURATION_TOOLBAR);
            getIvLogo().animate()
                    .translationY(0)
                    .setDuration(ANIM_DURATION_TOOLBAR)
                    .setStartDelay(100);
            getInboxMenuItem().getActionView().animate()
                    .translationY(0)
                    .setDuration(ANIM_DURATION_TOOLBAR)
                    .setStartDelay(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            //addFragment
                            mCurrentFragment = new HomeFragment();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().add(R.id.contentFrame, mCurrentFragment).commit();
                        }
                    })
                    .start();

        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            case TRIM_MEMORY_UI_HIDDEN:
                break;
            case TRIM_MEMORY_RUNNING_MODERATE:
                Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);
                break;
            case TRIM_MEMORY_RUNNING_LOW:
                break;
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }


/*
    @Override
    protected void onPause() {
        super.onPause();
        if (!isTaskTop()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getDrawerToggle() != null && getDrawerLayout() != null) {
                        getDrawerToggle().onDrawerOpened(getDrawerLayout());
                    }
                    if (getInboxMenuItem() != null) {
                        getInboxMenuItem().getActionView().setScaleY(0);
                        getInboxMenuItem().getActionView().setScaleX(0);
                    }

                }
            }, 300);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isTaskTop()) {
            setTaskTop(true);
            AnimationUtils.arrowToMenuAnimator(getDrawerToggle(), getDrawerLayout());
            AnimationUtils.scaleToOriginalAnimator(getInboxMenuItem().getActionView());
        }
    }*/

}
