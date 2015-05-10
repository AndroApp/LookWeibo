package com.shine.look.weibo.ui.views;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.shine.look.weibo.ui.adapter.HomeWeiboAdapter;

/**
 * User:Shine
 * Date:2015-05-08
 * Description:
 */
public class LoadMoreRecyclerView extends RecyclerView {

    private boolean isLoading;
    private OnRefreshEndListener mEndListener;

    public LoadMoreRecyclerView(Context context) {
        super(context);
        init();
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        isLoading = false;
        setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        if (!isLoading) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
            if (layoutManager != null && layoutManager != null) {
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (lastVisibleItem + 1 >= totalItemCount && dy > 0) {
                    isLoading = true;
                    ((HomeWeiboAdapter) getAdapter()).addLoading();
                    if (mEndListener != null) {
                        mEndListener.onEnd();
                    }

                }
            }
        }

        super.onScrolled(dx, dy);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setCloseLoading() {
        this.isLoading = false;
        ((HomeWeiboAdapter) getAdapter()).removeLoading();
    }

    public OnRefreshEndListener getEndListener() {
        return mEndListener;
    }

    public void setOnRefreshEndListener(OnRefreshEndListener endListener) {
        mEndListener = endListener;
    }

    public interface OnRefreshEndListener {
        public void onEnd();
    }

}
