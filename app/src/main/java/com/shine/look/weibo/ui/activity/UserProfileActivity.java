package com.shine.look.weibo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.ThumbnailPic;
import com.shine.look.weibo.bean.User;
import com.shine.look.weibo.bean.gson.HomeInfo;
import com.shine.look.weibo.medel.BaseModel;
import com.shine.look.weibo.medel.HomeModel;
import com.shine.look.weibo.medel.UserModel;
import com.shine.look.weibo.ui.adapter.HomeWeiboAdapter;
import com.shine.look.weibo.ui.adapter.ThumbnailPicAdapter;
import com.shine.look.weibo.ui.adapter.UserProfileAdapter;
import com.shine.look.weibo.ui.utils.AnimationUtils;
import com.shine.look.weibo.ui.utils.WeiboUrlSpan;
import com.shine.look.weibo.ui.views.ContentTextView;
import com.shine.look.weibo.ui.views.RevealBackgroundView;
import com.shine.look.weibo.ui.views.WeiboContentView;
import com.shine.look.weibo.utils.Constants;

import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * User:Shine
 * Date:2015-05-09
 * Description:
 */
public class UserProfileActivity extends BaseActivity implements RevealBackgroundView.OnStateChangeListener
        , BaseModel.OnRequestListener<HomeInfo>, WeiboContentView.OnPictureListener, ThumbnailPicAdapter.OnThumbnailPicListener
        , HomeWeiboAdapter.OnFeedItemClickListener {

    private static final String ARG_STARTING_LOCATION = "com.shine.look.weibo.startingLocation";
    private static final String ARG_USER_ID = "com.shine.look.weibo.userId";

    private RevealBackgroundView vRevealBackground;
    private RecyclerView mRvUserProfile;
    private UserProfileAdapter mAdapter;

    private boolean mBackIsMenu;
    private int mBackMenuResId;
    private String mUserId;
    private String mUserName;
    private HomeModel mHomeModel;

    private boolean isRunning = true;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        vRevealBackground = (RevealBackgroundView) findViewById(R.id.vRevealBackground);
        mRvUserProfile = (RecyclerView) findViewById(R.id.rvUserProfile);

        initializeToolbar();

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            final int drawingStartLocation = getIntent().getIntExtra(WeiboUrlSpan.START_LOCATION, 0);
            final int[] startingLocation = getIntent().getIntArrayExtra(ARG_STARTING_LOCATION);
            if (drawingStartLocation != 0) {
                mRvUserProfile.setVisibility(View.VISIBLE);
                mRvUserProfile.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        mRvUserProfile.getViewTreeObserver().removeOnPreDrawListener(this);
                        AnimationUtils.openEnterFromYAnimation(mRvUserProfile, getToolbar(), drawingStartLocation);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isRunning)
                                    initRecyclerView();
                            }
                        }, 200);
                        return true;
                    }
                });
            }

            if (startingLocation != null) {
                vRevealBackground.setOnStateChangeListener(this);
                vRevealBackground.setVisibility(View.VISIBLE);
                vRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        vRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                        vRevealBackground.startFromLocation(startingLocation);
                        return true;
                    }
                });
            }
        } else {
            if (vRevealBackground.getVisibility() == View.VISIBLE)
                vRevealBackground.setToFinishedFrame();
        }
        Intent intent = getIntent();
        if (intent != null) {
            mBackIsMenu = intent.getBooleanExtra(Constants.ARG_TOOLBAR_IS_MENU, true);
            mBackMenuResId = intent.getIntExtra(Constants.ARG_TOOLBAR_MENU_RES_ID, 0);
            mUserId = intent.getStringExtra(ARG_USER_ID);
            Uri uri = getIntent().getData();
            if (uri != null) {
                Matcher matcher = ContentTextView.AT_URL.matcher(uri.toString());
                if (matcher.find()) {
                    mUserName = matcher.group().replace("@", "");
                }
            }
        }


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
                onBackPressed();
            }
        });

        return true;
    }

    private void initRecyclerView() {
        mRvUserProfile.setVisibility(View.VISIBLE);
        RequestManager requestManager = Glide.with(this);
        mRvUserProfile.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new UserProfileAdapter(this, requestManager);
        mRvUserProfile.setAdapter(mAdapter);
        mRvUserProfile.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mAdapter.closeAnimateItems();
            }
        });
        mAdapter.setOnThumbnailPicListener(this);
        mAdapter.setOnFeedItemClickListener(this);
        mAdapter.setOnPictureListener(this);
        UserModel model = new UserModel(this, mUserId, mUserName);
        model.setOnRequestListener(new BaseModel.OnRequestListener<User>() {
            @Override
            public void onSuccess(final User info) {
                mAdapter.addUser(info);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isRunning) {
                            mHomeModel = new HomeModel(UserProfileActivity.this);
                            mHomeModel.setPath(Constants.URL_USER_TIMELINE_PATH);
                            mHomeModel.setUid(info.idstr);
                            mHomeModel.setOnRequestListener(UserProfileActivity.this);
                            mHomeModel.request();
                        }
                    }
                }, 600);
            }

            @Override
            public void onFailure(VolleyError error) {

            }
        });
        model.request();


    }

    @Override
    public void onSuccess(HomeInfo info) {
        if (info.statuses != null && info.statuses.size() > 0 && info.total_number > 0) {
            mAdapter.addItems(info.statuses);
        }
    }

    @Override
    public void onFailure(VolleyError error) {

    }

    @Override
    public void onBackPressed() {
        final ImageButton btn = (ImageButton) getInboxMenuItem().getActionView();
        btn.setScaleY(0);
        btn.setScaleX(0);
        btn.setImageResource(mBackMenuResId);
        AnimationUtils.scaleToOriginalAnimator(btn);
        if (mBackIsMenu) {
            AnimationUtils.arrowToMenuAnimator(getDrawerToggle(), getDrawerLayout());
        }
        AnimationUtils.closeEnterDownAnimation(mRvUserProfile, getToolbar(), this);
    }

    @Override
    public boolean toolBarIsMenu() {
        return false;
    }


    @Override
    public int toolBarMenuResId() {
        return R.drawable.ic_action_more;
    }

    public static void start(Activity activity, String userId, boolean isMenu, int menuResId, int[] startingLocation) {
        Intent intent = new Intent(activity, UserProfileActivity.class);
        intent.putExtra(ARG_USER_ID, userId);
        intent.putExtra(Constants.ARG_TOOLBAR_IS_MENU, isMenu);
        intent.putExtra(Constants.ARG_TOOLBAR_MENU_RES_ID, menuResId);
        intent.putExtra(ARG_STARTING_LOCATION, startingLocation);
        activity.startActivity(intent);
    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
            vRevealBackground.setVisibility(View.GONE);
            initRecyclerView();
        } else {
            mRvUserProfile.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRepostsClick(View view, int position) {

    }

    @Override
    public void onCommentsClick(View view, String weiboId) {
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);
        CommentsActivity.start(this, screenLocation[1], weiboId, toolBarIsMenu(), toolBarMenuResId());
    }

    @Override
    public void onPictureClick(View v) {
        ImageView imageView = (ImageView) v;
        String picUrl = (String) imageView.getTag();
        Drawable drawable = imageView.getDrawable();
        ShowPictureActivity.start(this, imageView, drawable, picUrl);
    }

    @Override
    public void onHeaderPicClick(View v) {
        int[] startingLocation = new int[2];
        v.getLocationOnScreen(startingLocation);
        startingLocation[0] += v.getWidth() / 2;
        UserProfileActivity.start(this, (String) v.getTag(), toolBarIsMenu(), toolBarMenuResId(), startingLocation);
    }

    @Override
    public void onThumbnailPicClick(View view, int position, ArrayList<ThumbnailPic> data) {
        ImageView imageView = (ImageView) view;
        Drawable drawable = imageView.getDrawable();
        ShowPictureActivity.start(this, imageView, drawable, data, position);
    }
}
