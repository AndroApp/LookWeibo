package com.shine.look.weibo.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
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
import com.shine.look.weibo.ui.utils.WeiboTextUrlSpan;
import com.shine.look.weibo.ui.views.BrowserContextMenu;
import com.shine.look.weibo.ui.views.ContentTextView;

import java.util.regex.Matcher;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * User:Shine
 * Date:2015-05-09
 * Description:
 */
public class BrowserActivity extends BaseActivity implements View.OnClickListener, BrowserContextMenu.OnBrowserContextMenuItemClickListener {

    private WebView mWvBrowser;

    @InjectView(R.id.flBrowser)
    FrameLayout mFlBrowser;
    @InjectView(R.id.pbBrowser)
    ProgressBar mPbBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        ButterKnife.inject(this);
        initializeToolbar();
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mWvBrowser = new WebView(getApplicationContext());
            mFlBrowser.addView(mWvBrowser);
            final int drawingStartLocation = getIntent().getIntExtra(WeiboTextUrlSpan.START_LOCATION, 0);
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

        Uri uri = getIntent().getData();
        if (uri != null) {
            Matcher matcher = ContentTextView.HTTP_URL.matcher(uri.toString());
            if (matcher.find()) {
                String url = matcher.group();
                mWvBrowser.loadUrl(url);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //将菜单图标变为箭头图标
        AnimationUtils.menuToArrowAnimator(getDrawerToggle(), getDrawerLayout());
        //覆盖箭头图标的点击事件
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtils.closeEnterDownAnimation(mFlBrowser, getToolbar(), BrowserActivity.this);
            }
        });
        setInboxMenuItemOnClick(this);
        //右边图标改为more图标
        final ImageButton btn = (ImageButton) getInboxMenuItem().getActionView();
        btn.setScaleY(0);
        btn.setScaleX(0);
        btn.setImageResource(R.drawable.ic_action_more);
        AnimationUtils.scaleToOriginalAnimator(btn);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWvBrowser.canGoBack()) {
                mWvBrowser.goBack();
            } else {
                AnimationUtils.closeEnterDownAnimation(mFlBrowser, getToolbar(), this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
