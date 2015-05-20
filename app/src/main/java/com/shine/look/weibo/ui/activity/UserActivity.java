package com.shine.look.weibo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.android.volley.VolleyError;
import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.User;
import com.shine.look.weibo.medel.BaseModel;
import com.shine.look.weibo.ui.utils.WeiboUrlSpan;
import com.shine.look.weibo.ui.views.RevealBackgroundView;
import com.shine.look.weibo.utils.Constants;

/**
 * User:Shine
 * Date:2015-05-09
 * Description:
 */
public class UserActivity extends BaseActivity implements BaseModel.OnRequestListener<User> {

    private static final String ARG_STARTING_LOCATION = "com.shine.look.weibo.startingLocation";
    private static final String ARG_USER_ID = "com.shine.look.weibo.userId";

    private RevealBackgroundView vRevealBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        vRevealBackground = (RevealBackgroundView) findViewById(R.id.vRevealBackground);

        initializeToolbar();

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (savedInstanceState == null && intent != null) {
            final int drawingStartLocation = intent.getIntExtra(WeiboUrlSpan.START_LOCATION, 0);
            final int[] startingLocation = intent.getIntArrayExtra(ARG_STARTING_LOCATION);

//            if (drawingStartLocation != 0)
//                AnimationUtils.openEnterFromYAnimation(vRevealBackground, getToolbar(), drawingStartLocation);

            if (startingLocation != null) {
                // vRevealBackground.setVisibility(View.VISIBLE);
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

//            Uri uri = getIntent().getData();
//            Matcher matcher = ContentTextView.AT_URL.matcher(uri.toString());
//            if (matcher.find()) {
//                String userName = matcher.group().replace("@", "");
//                UserModel model = new UserModel(this, userName);
//                model.setOnRequestListener(this);
//                model.setDiskCache(false);
//                model.request();
//            }


    }

    @Override
    public void onSuccess(User info) {
        Log.i("huangruiin", info.toString());
    }

    @Override
    public void onFailure(VolleyError error) {
        Log.i("huangruiin", error.toString());
    }

    @Override
    public boolean toolBarIsMenu() {
        return false;
    }

    @Override
    public int toolBarMenuResId() {
        return 0;
    }


    public static void start(Activity activity, String userId, boolean isMenu, int menuResId, int[] startingLocation) {
        Intent intent = new Intent(activity, UserActivity.class);
        intent.putExtra(ARG_USER_ID, userId);
        intent.putExtra(Constants.ARG_TOOLBAR_IS_MENU, isMenu);
        intent.putExtra(Constants.ARG_TOOLBAR_MENU_RES_ID, menuResId);
        intent.putExtra(ARG_STARTING_LOCATION, startingLocation);
        activity.startActivity(intent);
    }

}
