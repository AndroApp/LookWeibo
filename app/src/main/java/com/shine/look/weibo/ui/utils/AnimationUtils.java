package com.shine.look.weibo.ui.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.shine.look.weibo.utils.Utils;

/**
 * User:Shine
 * Date:2015-05-14
 * Description:
 */
public class AnimationUtils {

    public static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    public static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();

    /**
     * 将view放大到原来的size
     *
     * @param view
     */
    public static void scaleToOriginalAnimator(View view) {
        view.animate()
                .scaleX(1)
                .scaleY(1)
                .setDuration(300)
                .setStartDelay(100)
                .setInterpolator(DECELERATE_INTERPOLATOR)
                .start();
    }

    /**
     * 将view缩小到消失
     *
     * @param view
     */
    public static void scaleToDisappearAnimator(View view) {
        view.animate()
                .scaleX(0)
                .scaleY(0)
                .setDuration(300)
                .setStartDelay(100)
                .setInterpolator(DECELERATE_INTERPOLATOR)
                .start();
    }

    /**
     * 将菜单图标变为箭头图标
     */
    public static void menuToArrowAnimator(final ActionBarDrawerToggle drawerToggle, final DrawerLayout drawerLayout) {
        if (drawerToggle != null && drawerLayout != null) {
            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.setDuration(300);
            animator.setInterpolator(ACCELERATE_INTERPOLATOR);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float currentValue = (float) animation.getAnimatedValue();
                    drawerToggle.onDrawerSlide(drawerLayout, currentValue);

                }
            });
            animator.start();
        }
    }

    /**
     * 将箭头图标变为菜单图标
     */
    public static void arrowToMenuAnimator(final ActionBarDrawerToggle drawerToggle, final DrawerLayout drawerLayout) {
        if (drawerToggle != null && drawerLayout != null) {
            ValueAnimator animator = ValueAnimator.ofFloat(1f, 0f);
            animator.setDuration(300);
            animator.setInterpolator(ACCELERATE_INTERPOLATOR);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float currentValue = (float) animation.getAnimatedValue();
                    drawerToggle.onDrawerSlide(drawerLayout, currentValue);
                }
            });
            animator.start();
        }
    }

    /**
     * 入场动画之一，从startLocation位置开始y轴展开
     */
    public static void openEnterFromYAnimation(View view, final Toolbar toolbar, int startLocation) {
        if (view != null && toolbar != null && startLocation != 0) {
            ViewCompat.setElevation(toolbar, 0);
            view.setScaleY(0.1f);
            view.setPivotY(startLocation);
            view.animate()
                    .scaleY(1)
                    .setDuration(200)
                    .setInterpolator(new AccelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            ViewCompat.setElevation(toolbar, Utils.dpToPx(8));
                        }
                    })
                    .start();

        }
    }

    /**
     * 退场动画之一，向下滑动退场
     */
    public static void closeEnterDownAnimation(View view, final Toolbar toolbar, final Activity activity) {
        if (view != null && toolbar != null && activity != null) {
            ViewCompat.setElevation(toolbar, 0);
            view.animate().translationY(Utils.getScreenHeight())
                    .setDuration(300).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    activity.finish();
                }
            }).start();
        }
    }

    /**
     * 移动RecyclerView的Y轴到顶部
     */
    public static void moveRecyclerViewToTop(final RecyclerView view) {
        if (view != null) {
            int firstVisibleItemPosition = ((LinearLayoutManager) view.getLayoutManager()).findFirstVisibleItemPosition();
            ValueAnimator animator = ValueAnimator.ofInt(firstVisibleItemPosition, 0);
            animator.setDuration(10 * firstVisibleItemPosition);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int currentValue = (int) animation.getAnimatedValue();
                    view.scrollToPosition(currentValue);
                }
            });
            animator.start();
        }
    }

}
