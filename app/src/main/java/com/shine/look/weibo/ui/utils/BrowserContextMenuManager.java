package com.shine.look.weibo.ui.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.shine.look.weibo.ui.views.BrowserContextMenu;
import com.shine.look.weibo.utils.Utils;

/**
 * User:Shine
 * Date:2015-05-14
 * Description:
 */
public class BrowserContextMenuManager implements View.OnAttachStateChangeListener, View.OnTouchListener {

    private static BrowserContextMenuManager instance;

    private BrowserContextMenu contextMenuView;

    private boolean isContextMenuDismissing;
    private boolean isContextMenuShowing;

    public static BrowserContextMenuManager getInstance() {
        if (instance == null) {
            instance = new BrowserContextMenuManager();
        }
        return instance;
    }

    public void toggleContextMenuFromView(View openingView, BrowserContextMenu.OnBrowserContextMenuItemClickListener listener) {
        if (contextMenuView == null) {
            showContextMenuFromView(openingView, listener);
        } else {
            hideContextMenu();
        }
    }

    private void showContextMenuFromView(final View openingView, BrowserContextMenu.OnBrowserContextMenuItemClickListener
            listener) {
        if (!isContextMenuShowing) {
            isContextMenuShowing = true;
            contextMenuView = new BrowserContextMenu(openingView.getContext());
            contextMenuView.bindToItem(0);
            contextMenuView.addOnAttachStateChangeListener(this);
            contextMenuView.setOnBrowserMenuItemClickListener(listener);

            ((ViewGroup) openingView.getRootView().findViewById(android.R.id.content)).addView(contextMenuView);

            contextMenuView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    contextMenuView.getViewTreeObserver().removeOnPreDrawListener(this);
                    setupContextMenuInitialPosition();
                    performShowAnimation();
                    return false;
                }
            });
        }
    }


    public void setupContextMenuInitialPosition() {
        int additionalBottomMargin = Utils.dpToPx(56);
        contextMenuView.setTranslationX(Utils.getScreenWidth() - contextMenuView.getWidth());
        contextMenuView.setTranslationY(additionalBottomMargin);
    }

    private void performShowAnimation() {
        contextMenuView.setPivotX(contextMenuView.getWidth());
        contextMenuView.setPivotY(0);
        contextMenuView.setScaleX(0.1f);
        contextMenuView.setScaleY(0.1f);
        contextMenuView.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(150)
                .setInterpolator(new OvershootInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isContextMenuShowing = false;
                    }
                });
    }

    public void hideContextMenu() {
        if (!isContextMenuDismissing) {
            isContextMenuDismissing = true;
            performDismissAnimation();
        }
    }

    private void performDismissAnimation() {
        contextMenuView.setPivotX(contextMenuView.getWidth());
        contextMenuView.setPivotY(0);
        contextMenuView.animate()
                .scaleX(0.1f)
                .scaleY(0.1f)
                .setDuration(150)
                .setInterpolator(new AccelerateInterpolator())
                .setStartDelay(100)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (contextMenuView != null) {
                            contextMenuView.dismiss();
                        }
                        isContextMenuDismissing = false;
                    }
                });
    }

    private BrowserContextMenuManager() {

    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        contextMenuView = null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (contextMenuView != null) {
            hideContextMenu();
        }
        return false;
    }
}
