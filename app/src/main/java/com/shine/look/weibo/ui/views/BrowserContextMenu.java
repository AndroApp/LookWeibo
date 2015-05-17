package com.shine.look.weibo.ui.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shine.look.weibo.R;
import com.shine.look.weibo.utils.Utils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * User:Shine
 * Date:2015-05-14
 * Description:
 */
public class BrowserContextMenu extends LinearLayout {

    private static final int CONTEXT_MENU_WIDTH = Utils.dpToPx(200);

    private int feedItem = -1;

    private OnBrowserContextMenuItemClickListener onItemClickListener;

    public BrowserContextMenu(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_context_menu, this);
        setBackgroundResource(R.drawable.bg_container_shadow);
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(CONTEXT_MENU_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.inject(this);
    }

    public void bindToItem(int feedItem) {
        this.feedItem = feedItem;
    }

    public void dismiss() {
        ((ViewGroup) getParent()).removeView(BrowserContextMenu.this);
    }

    @OnClick(R.id.btnRefresh)
    public void onRefreshClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onRefreshClick(feedItem);
        }
    }

    @OnClick(R.id.btnCopy)
    public void onCopyClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onCopyClick(feedItem);
        }
    }
/*  分享功能 暂时不做
    @OnClick(R.id.btnShare)
    public void onShareClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onShareClick(feedItem);
        }
    }*/

    @OnClick(R.id.btnBrowserOpen)
    public void onBrowserOpenClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onBrowserOpenClick(feedItem);
        }
    }

    public void setOnBrowserMenuItemClickListener(OnBrowserContextMenuItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnBrowserContextMenuItemClickListener {
        public void onRefreshClick(int feedItem);

        public void onCopyClick(int feedItem);

        public void onBrowserOpenClick(int feedItem);
        //分享功能 暂时不做
        //public void onShareClick(int feedItem);
    }
}
