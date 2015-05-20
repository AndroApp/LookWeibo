package com.shine.look.weibo.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.shine.look.weibo.R;
import com.shine.look.weibo.ui.utils.AnimationUtils;
import com.shine.look.weibo.ui.utils.BrowserContextMenuManager;
import com.shine.look.weibo.ui.utils.WeiboUrlSpan;
import com.shine.look.weibo.ui.views.BrowserContextMenu;
import com.shine.look.weibo.ui.views.ContentTextView;
import com.shine.look.weibo.utils.Constants;

import java.util.regex.Matcher;

/**
 * User:Shine
 * Date:2015-05-09
 * Description:
 */
public class BrowserActivity extends BaseActivity implements View.OnClickListener, BrowserContextMenu.OnBrowserContextMenuItemClickListener {

    private WebView mWvBrowser;

    private FrameLayout mFlBrowser;
    private ProgressBar mPbBrowser;

    private boolean mBackIsMenu;
    private int mBackMenuResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        mFlBrowser = (FrameLayout) findViewById(R.id.flBrowser);
        mPbBrowser = (ProgressBar) findViewById(R.id.pbBrowser);
        initializeToolbar();
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (savedInstanceState == null && intent != null) {
            mWvBrowser = new WebView(getApplicationContext());
            mFlBrowser.addView(mWvBrowser);
            final int drawingStartLocation = intent.getIntExtra(WeiboUrlSpan.START_LOCATION, 0);
            mBackIsMenu = intent.getBooleanExtra(Constants.ARG_TOOLBAR_IS_MENU, true);
            mBackMenuResId = intent.getIntExtra(Constants.ARG_TOOLBAR_MENU_RES_ID, 0);
            mFlBrowser.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mFlBrowser.getViewTreeObserver().removeOnPreDrawListener(this);
                    AnimationUtils.openEnterFromYAnimation(mFlBrowser, getToolbar(), drawingStartLocation);
                    return true;
                }
            });
        }
        WebSettings webSettings = mWvBrowser.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        mWvBrowser.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.endsWith(".apk")) {
                    startSystemBrowser(url);
                    return true;
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });
        mWvBrowser.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mPbBrowser.setProgress(newProgress);
                mPbBrowser.setVisibility(newProgress == 100 ? View.GONE : View.VISIBLE);
                super.onProgressChanged(view, newProgress);
            }
        });
        mWvBrowser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                BrowserContextMenuManager.getInstance().onTouch(v, event);
                return false;
            }
        });

        Uri uri = intent.getData();
        if (uri != null) {
            Matcher matcher = ContentTextView.HTTP_URL.matcher(uri.toString());
            if (matcher.find()) {
                String url = matcher.group();
                mWvBrowser.loadUrl(url);
            }
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
                closeEnterAnimation();
            }
        });
        setInboxMenuItemOnClick(this);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (mWvBrowser.canGoBack()) {
            mWvBrowser.goBack();
        } else {
            closeEnterAnimation();
        }
    }

    private void closeEnterAnimation() {
        final ImageButton btn = (ImageButton) getInboxMenuItem().getActionView();
        btn.setScaleY(0);
        btn.setScaleX(0);
        btn.setImageResource(mBackMenuResId);
        AnimationUtils.scaleToOriginalAnimator(btn);
        if (mBackIsMenu) {
            AnimationUtils.arrowToMenuAnimator(getDrawerToggle(), getDrawerLayout());
        }
        AnimationUtils.closeEnterDownAnimation(mFlBrowser, getToolbar(), this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnInbox:
                BrowserContextMenuManager.getInstance().toggleContextMenuFromView(v, this);
                break;
        }
    }


    @Override
    public void onRefreshClick(int feedItem) {
        if (mWvBrowser != null) {
            mWvBrowser.reload();
            BrowserContextMenuManager.getInstance().hideContextMenu();
        }
    }

    @Override
    public void onCopyClick(int feedItem) {
        if (mWvBrowser != null) {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("url", mWvBrowser.getUrl());
            clipboardManager.setPrimaryClip(clip);
            BrowserContextMenuManager.getInstance().hideContextMenu();
        }
    }

    @Override
    public void onBrowserOpenClick(int feedItem) {
        if (mWvBrowser != null) {
            startSystemBrowser(mWvBrowser.getUrl());
            BrowserContextMenuManager.getInstance().hideContextMenu();
        }
    }

    public void startSystemBrowser(String url) {
        Uri uri = Uri.parse(url);
        Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(viewIntent);
    }

    @Override
    protected void onDestroy() {
        if (mFlBrowser != null && mWvBrowser != null) {
            mFlBrowser.removeAllViews();
            mWvBrowser.destroy();
            mWvBrowser = null;
        }
        super.onDestroy();
    }

    //     分享功能 暂时不做
//    @Override
//    public void onShareClick(int feedItem) {
//
//    }
}
