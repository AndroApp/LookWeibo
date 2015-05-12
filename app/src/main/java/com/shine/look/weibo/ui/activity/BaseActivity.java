package com.shine.look.weibo.ui.activity;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.shine.look.weibo.R;
import com.shine.look.weibo.http.RequestManager;
import com.shine.look.weibo.ui.utils.DrawerLayoutInstaller;
import com.shine.look.weibo.ui.views.NavigationMenuView;
import com.shine.look.weibo.utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

public class BaseActivity extends AppCompatActivity implements NavigationMenuView.OnHeaderClickListener {

    @Optional
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @Optional
    @InjectView(R.id.ivLogo)
    ImageView mIvLogo;

    private MenuItem inboxMenuItem;
    private DrawerLayout drawerLayout;


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);

        setupToolbar();
        if (shouldInstallDrawer()) {
            setupDrawer();
        }
    }

    protected void setupToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationIcon(R.drawable.ic_menu_white);
        }
    }

    private void setupDrawer() {
        NavigationMenuView menuView = new NavigationMenuView(this);
        menuView.setOnHeaderClickListener(this);

        drawerLayout = DrawerLayoutInstaller.from(this)
                .drawerRoot(R.layout.drawer_root)
                .drawerLeftView(menuView)
                .drawerLeftWidth(Utils.dpToPx(300))
                .withNavigationIconToggler(getToolbar())
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        inboxMenuItem = menu.findItem(R.id.action_inbox);
        inboxMenuItem.setActionView(R.layout.menu_item_view);
        return true;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }


    protected boolean shouldInstallDrawer() {
        return true;
    }


    @Override
    public void onHeaderClick(View v) {

    }
}
