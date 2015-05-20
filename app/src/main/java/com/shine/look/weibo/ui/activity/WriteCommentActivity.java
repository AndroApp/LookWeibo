package com.shine.look.weibo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.shine.look.weibo.R;
import com.shine.look.weibo.medel.BaseModel;
import com.shine.look.weibo.medel.WriteCommentModel;
import com.shine.look.weibo.ui.utils.AnimationUtils;
import com.shine.look.weibo.ui.utils.SendMsgBroadcastReceiver;
import com.shine.look.weibo.ui.views.RevealBackgroundView;
import com.shine.look.weibo.utils.Constants;
import com.shine.look.weibo.utils.ToastHelper;

/**
 * User:Shine
 * Date:2015-05-19
 * Description:
 */
public class WriteCommentActivity extends BaseActivity implements View.OnClickListener, BaseModel.OnRequestListener {

    public static final String ARG_STARTING_LOCATION = "com.shine.look.weibo.startingLocation";

    private String mWeiboId;
    private int mBackMenuResId;

    private WriteCommentModel mModel;

    private RevealBackgroundView vRevealBackground;
    private EditText mEdWriteComment;
    private TextView mTvWriteMaxLength;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);

        vRevealBackground = (RevealBackgroundView) findViewById(R.id.vRevealBackground);
        mEdWriteComment = (EditText) findViewById(R.id.edWriteComment);
        mTvWriteMaxLength = (TextView) findViewById(R.id.tvWriteMaxLength);

        initializeToolbar();
        setupRevealBackground(savedInstanceState);
        init();
    }

    public void setupRevealBackground(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (savedInstanceState == null && intent != null) {
            mBackMenuResId = intent.getIntExtra(Constants.ARG_TOOLBAR_MENU_RES_ID, 0);
            mWeiboId = intent.getStringExtra(Constants.ARG_WEIBO_ID);
            final int[] startingLocation = intent.getIntArrayExtra(ARG_STARTING_LOCATION);
            vRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    vRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                    vRevealBackground.startFromLocation(startingLocation);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mEdWriteComment != null) {
                                mEdWriteComment.setVisibility(View.VISIBLE);
                            }
                        }
                    }, 500);
                    return true;
                }
            });
        } else {
            vRevealBackground.setToFinishedFrame();
        }

    }


    private void init() {
        mModel = new WriteCommentModel(this, mWeiboId);
        mModel.setOnRequestListener(this);

        mEdWriteComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                mTvWriteMaxLength.setText(length + "/140");
                if (length >= 140) {
                    mTvWriteMaxLength.setTextColor(Color.RED);
                } else if (mTvWriteMaxLength.getTextColors().getDefaultColor() == Color.RED) {
                    mTvWriteMaxLength.setTextColor(getResources().getColor(R.color.secondary_text));
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getDrawerToggle().onDrawerOpened(getDrawerLayout());
        //覆盖箭头图标的点击事件
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setInboxMenuItemOnClick(this);
        return true;
    }

    @Override
    public boolean toolBarIsMenu() {
        return false;
    }


    @Override
    public int toolBarMenuResId() {
        return R.drawable.ic_action_done;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnInbox:
                String text = mEdWriteComment.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    ToastHelper.show("请输入内容");
                    return;
                }
                v.setEnabled(false);

                mModel.setText(text);
                mModel.request();

                Intent intent = new Intent(SendMsgBroadcastReceiver.BROADCAST_SEND_MSG);
                intent.putExtra(SendMsgBroadcastReceiver.ARG_SEND_STATUS, SendMsgBroadcastReceiver.SEND_STATUS_REQUEST);
                sendBroadcast(intent);

                break;
        }
    }

    @Override
    public void onSuccess(Object info) {
        Intent intent = new Intent(SendMsgBroadcastReceiver.BROADCAST_SEND_MSG);
        intent.putExtra(SendMsgBroadcastReceiver.ARG_SEND_STATUS, SendMsgBroadcastReceiver.SEND_STATUS_SUCCESS);
        sendBroadcast(intent);
        onBackPressed();
    }

    @Override
    public void onFailure(VolleyError error) {
        Intent intent = new Intent(SendMsgBroadcastReceiver.BROADCAST_SEND_MSG);
        intent.putExtra(SendMsgBroadcastReceiver.ARG_SEND_STATUS, SendMsgBroadcastReceiver.SEND_STATUS_FAILURE);
        sendBroadcast(intent);
        getInboxMenuItem().getActionView().setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        mEdWriteComment.setVisibility(View.GONE);
        final ImageButton btn = (ImageButton) getInboxMenuItem().getActionView();
        btn.setScaleY(0);
        btn.setScaleX(0);
        btn.setImageResource(mBackMenuResId);
        AnimationUtils.scaleToOriginalAnimator(btn);
        AnimationUtils.closeEnterDownAnimation(vRevealBackground, getToolbar(), this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEdWriteComment != null) {
            mEdWriteComment = null;
        }
    }

    public static void start(Activity activity, String weiboId, boolean isMenu, int menuResId, int[] startingLocation) {
        Intent intent = new Intent(activity, WriteCommentActivity.class);
        intent.putExtra(Constants.ARG_WEIBO_ID, weiboId);
        intent.putExtra(Constants.ARG_TOOLBAR_IS_MENU, isMenu);
        intent.putExtra(Constants.ARG_TOOLBAR_MENU_RES_ID, menuResId);
        intent.putExtra(ARG_STARTING_LOCATION, startingLocation);
        activity.startActivity(intent);
    }
}
