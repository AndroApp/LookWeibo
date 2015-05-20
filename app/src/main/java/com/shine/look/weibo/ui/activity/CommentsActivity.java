package com.shine.look.weibo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.gson.CommentsInfo;
import com.shine.look.weibo.medel.BaseModel;
import com.shine.look.weibo.medel.CommentsModel;
import com.shine.look.weibo.ui.adapter.CommentsAdapter;
import com.shine.look.weibo.ui.utils.AnimationUtils;
import com.shine.look.weibo.ui.views.LoadMoreRecyclerView;
import com.shine.look.weibo.utils.Constants;

/**
 * User:Shine
 * Date:2015-05-18
 * Description:
 */
public class CommentsActivity extends BaseActivity implements BaseModel.OnRequestListener<CommentsInfo>
        , View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LoadMoreRecyclerView.OnRefreshEndListener {

    public static final String ARG_SCREEN_Y = "com.shine.look.weibo.screenY";


    private LoadMoreRecyclerView mRvComments;
    private CommentsAdapter mAdapter;
    private SwipeRefreshLayout mSwipeContainer;
    private CommentsModel mModel;
    private boolean mBackIsMenu;
    private int mBackMenuResId;
    private String mWeiboId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        initializeToolbar();

        mRvComments = (LoadMoreRecyclerView) findViewById(R.id.rvComments);
        mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeContainer.setOnRefreshListener(this);
        mSwipeContainer.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorAccent);

        Intent intent = getIntent();
        if (intent != null) {
            mBackIsMenu = intent.getBooleanExtra(Constants.ARG_TOOLBAR_IS_MENU, true);
            mBackMenuResId = intent.getIntExtra(Constants.ARG_TOOLBAR_MENU_RES_ID, 0);
            final int drawingStartLocation = intent.getIntExtra(ARG_SCREEN_Y, 0);
            mRvComments.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mRvComments.getViewTreeObserver().removeOnPreDrawListener(this);
                    AnimationUtils.openEnterFromYAnimation(mRvComments, getToolbar(), drawingStartLocation);
                    return true;
                }
            });
        }

        init();
    }

    private void init() {
        RequestManager requestManager = Glide.with(this);
        mAdapter = new CommentsAdapter(this, requestManager);
        mRvComments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRvComments.setAdapter(mAdapter);
        mRvComments.setOnRefreshEndListener(this);

        mWeiboId = getIntent().getStringExtra(Constants.ARG_WEIBO_ID);
        mModel = new CommentsModel(this, mWeiboId);
        mModel.setOnRequestListener(this);
        mModel.request();

    }

    @Override
    public void onSuccess(CommentsInfo info) {
        closeRefresh();
        if (info.comments.size() > 0 && info.total_number > 0) {
            if (mModel.getMaxId() == 0 && mAdapter.getItemCount() > 0) {
                mAdapter.clear();
            }
            mAdapter.addItems(info.comments);
        }
        if (mAdapter.getItemCount() == info.total_number || info.comments.size() == 0) {
            mRvComments.setLoading(true);
        }
    }

    @Override
    public void onFailure(VolleyError error) {
        closeRefresh();
    }

    private void closeRefresh() {
        if (mRvComments.isLoading()) {
            mRvComments.setLoading(false);
            mAdapter.removeLoading();
        }
        if (mSwipeContainer.isRefreshing()) {
            mSwipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        if (mModel != null) {
            mRvComments.setLoading(false);
            mModel.setMaxId(0);
            mModel.request();
        }
    }


    @Override
    public void onEnd() {
        if (mModel != null) {
            mAdapter.addLoading();
            mModel.setMaxId(mAdapter.getMaxId() - 1);
            mModel.request();
        }
    }

    @Override
    public boolean toolBarIsMenu() {
        return false;
    }

    @Override
    public int toolBarMenuResId() {
        return R.drawable.ic_action_mode_edit;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setInboxMenuItemOnClick(this);
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
        return true;
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
        AnimationUtils.closeEnterDownAnimation(mRvComments, getToolbar(), this);
    }

    @Override
    public void onBackPressed() {
        closeEnterAnimation();
    }

    public static void start(Activity activity, int screenY, String weiboId, boolean isMenu, int menuResId) {
        Intent intent = new Intent(activity, CommentsActivity.class);
        intent.putExtra(ARG_SCREEN_Y, screenY);
        intent.putExtra(Constants.ARG_WEIBO_ID, weiboId);
        intent.putExtra(Constants.ARG_TOOLBAR_IS_MENU, isMenu);
        intent.putExtra(Constants.ARG_TOOLBAR_MENU_RES_ID, menuResId);
        activity.startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnInbox:
                int[] startingLocation = new int[2];
                v.getLocationOnScreen(startingLocation);
                startingLocation[1] += v.getWidth() / 2;

                WriteCommentActivity.start(CommentsActivity.this, mWeiboId, toolBarIsMenu(), toolBarMenuResId(), startingLocation);
                break;
        }
    }
}
