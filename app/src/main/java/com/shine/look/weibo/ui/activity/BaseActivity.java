package com.shine.look.weibo.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.shine.look.weibo.R;
import com.shine.look.weibo.http.RequestManager;
import com.shine.look.weibo.ui.utils.AnimationUtils;
import com.shine.look.weibo.ui.views.NavigationMenuView;
import com.shine.look.weibo.utils.ResourceHelper;

/**
 * Abstract activity with toolbar, navigation drawer and cast support. Needs to be extended by
 * any activity that wants to be shown as a top level activity.
 * <p/>
 * The requirements for a subclass is to call {@link #initializeToolbar()} on onCreate, after
 * setContentView() is called and have three mandatory layout elements:
 * a {@link android.support.v7.widget.Toolbar} with id 'toolbar',
 * a {@link android.support.v4.widget.DrawerLayout} with id 'drawerLayout' and
 * a {@link android.widget.ListView} with id 'drawerList'.
 */
public abstract class BaseActivity extends AppCompatActivity implements NavigationMenuView.OnHeaderClickListener {


    private MenuItem inboxMenuItem;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationMenuView mDrawerList;
    private Toolbar mToolbar;

    private boolean mToolbarInitialized;

    ImageView mIvLogo;

    public abstract boolean toolBarIsMenu();

    public abstract int toolBarMenuResId();

    protected void initializeToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null) {
            throw new IllegalStateException("Layout is required to include a Toolbar with id " +
                    "'toolbar'");
        }

        mToolbar.inflateMenu(R.menu.menu_main);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        if (mDrawerLayout != null) {
            mDrawerList = (NavigationMenuView) findViewById(R.id.drawerList);
            if (mDrawerList == null) {
                throw new IllegalStateException("A layout with a drawerLayout is required to" +
                        "include a ListView with id 'drawerList'");
            }
            mDrawerList.setOnHeaderClickListener(this);
            mDrawerLayout.setStatusBarBackgroundColor(
                    ResourceHelper.getThemeColor(this, R.attr.colorPrimaryDark, android.R.color.black));
            setSupportActionBar(mToolbar);
        } else {
            setSupportActionBar(mToolbar);
            mDrawerLayout = new DrawerLayout(this);
        }
        // Create an ActionBarDrawerToggle that will handle opening/closing of the drawer:
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.open_content_drawer, R.string.close_content_drawer);
        mToolbarInitialized = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mIvLogo = (ImageView) findViewById(R.id.ivLogo);
        inboxMenuItem = menu.findItem(R.id.action_inbox);
        inboxMenuItem.setActionView(R.layout.menu_item_view);
        ImageButton imageButton = (ImageButton) inboxMenuItem.getActionView();
        imageButton.setImageResource(toolBarMenuResId());
        if (!(this instanceof MainActivity)) {
            //右边图标改为more图标
            imageButton.setScaleY(0);
            imageButton.setScaleX(0);
            AnimationUtils.scaleToOriginalAnimator(imageButton);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //如果抽屉是打开的，那边按下返回键时将关闭它
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null && mDrawerLayout != null) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null && mDrawerLayout != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mToolbarInitialized) {
            throw new IllegalStateException("你必须在你的onCreate方法结束前运行super.initializeToolbar");
        }
    }


    public Toolbar getToolbar() {
        return mToolbar;
    }

    public MenuItem getInboxMenuItem() {
        return inboxMenuItem;
    }

    public ImageView getIvLogo() {
        return mIvLogo;
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }


    public void setInboxMenuItemOnClick(View.OnClickListener listener) {
        if (inboxMenuItem != null) {
            inboxMenuItem.getActionView().setOnClickListener(listener);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onHeaderClick(View v) {
        mDrawerLayout.closeDrawer(Gravity.START);
        int[] startingLocation = new int[2];
        v.getLocationOnScreen(startingLocation);
        startingLocation[0] += v.getWidth() / 2;
        UserActivity.start(this, null, toolBarIsMenu(), toolBarMenuResId(), startingLocation);
    }

}
