package com.shine.look.weibo.ui.views;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shine.look.weibo.utils.Utils;

/**
 * User:Shine
 * Date:2015-05-10
 * Description:
 */
public class ThumbnailPicLayoutManager extends GridLayoutManager {

    public ThumbnailPicLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        View view = recycler.getViewForPosition(0);
        if (view != null) {
            measureChild(view, widthSpec, heightSpec);
            int measuredHeight = view.getMeasuredHeight();

            int columnCount = (int) (state.getItemCount() / 3f + 0.9f);

            int heightSize = columnCount * measuredHeight + Utils.dpToPx(10) * columnCount;
            heightSpec = View.MeasureSpec.makeMeasureSpec(heightSize, View.MeasureSpec.EXACTLY);
        }

        super.onMeasure(recycler, state, widthSpec, heightSpec);


    }
}
