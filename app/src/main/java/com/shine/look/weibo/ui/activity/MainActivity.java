package com.shine.look.weibo.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

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
        if (savedInstanceState == null) {
            mPendingIntroAnimation = true;
        }

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
            //addFragment
            mCurrentFragment = new HomeFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().add(R.id.contentFrame, mCurrentFragment).commit();
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
                    .start();

        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }


}
