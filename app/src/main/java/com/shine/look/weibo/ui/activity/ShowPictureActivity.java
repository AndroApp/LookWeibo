package com.shine.look.weibo.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.ThumbnailPic;
import com.shine.look.weibo.ui.adapter.GalleryLargeTouchPicAdapter;
import com.shine.look.weibo.ui.fragment.GalleryLargeTouchPicFragment;
import com.shine.look.weibo.ui.utils.transition.ActivityTransition;
import com.shine.look.weibo.ui.utils.transition.ActivityTransitionLauncher;
import com.shine.look.weibo.ui.utils.transition.ExitActivityTransition;
import com.shine.look.weibo.ui.views.byakugallery.GalleryViewPager;
import com.shine.look.weibo.utils.Utils;

import java.util.ArrayList;

/**
 * User:Shine
 * Date:2015-05-14
 * Description:
 */
public class ShowPictureActivity extends AppCompatActivity implements View.OnClickListener, GalleryLargeTouchPicFragment.OnPicLoadingEnd, ViewPager.OnPageChangeListener {

    private static final String ARG_PICTURE_URL = "com.shine.look.weibo.picUrl";
    private static final String ARG_PICTURE_POSITION = "com.shine.look.weibo.picPosition";
    private static final String ARG_PICTURE_LIST_URL = "com.shine.look.weibo.list_picUrl";

    private ExitActivityTransition mExitTransition;
    private ArrayList<ThumbnailPic> mData;
    private boolean isBackPressed;
    private GalleryLargeTouchPicAdapter mAdapter;

    private ImageView ivShowPicture;
    private View llShowPictureBg;
    private ImageButton ivMore;
    private ScrollView svShowPicture;
    private GalleryViewPager gvpLargeTouchPics;
    private TextView tvCurrentPage;

    private AnimatorListenerAdapter mAnimatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            startDownloadPic();
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);

        ivShowPicture = (ImageView) findViewById(R.id.ivShowPicture);
        llShowPictureBg = findViewById(R.id.llShowPictureBg);
        ivMore = (ImageButton) findViewById(R.id.ivMore);
        svShowPicture = (ScrollView) findViewById(R.id.svShowPicture);
        gvpLargeTouchPics = (GalleryViewPager) findViewById(R.id.gvpLargeTouchPics);
        tvCurrentPage = (TextView) findViewById(R.id.tvCurrentPage);

        if (getIntent() != null) {
            mData = getIntent().getParcelableArrayListExtra(ARG_PICTURE_LIST_URL);
            mExitTransition = ActivityTransition.with(getIntent()).to(ivShowPicture).duration(300).thumbnail(mData == null ? false : true).
                    listener(mAnimatorListenerAdapter).start(savedInstanceState);
        }
        (llShowPictureBg.getRootView().findViewById(android.R.id.content)).setBackgroundColor(getResources().getColor(android.R.color.black));
        // ivShowPicture.setOnClickListener(this);

        mAdapter = new GalleryLargeTouchPicAdapter(getSupportFragmentManager());
        mAdapter.setOnPicLoadingEnd(this);
        gvpLargeTouchPics.setAdapter(mAdapter);
        gvpLargeTouchPics.setOffscreenPageLimit(1);
        gvpLargeTouchPics.setOnPageChangeListener(this);
    }

    private void startDownloadPic() {
        if (getIntent() != null) {
            if (mData == null) {
                mData = new ArrayList<>();
                ThumbnailPic thumbnailPic = new ThumbnailPic();
                thumbnailPic.thumbnail_pic = getIntent().getStringExtra(ARG_PICTURE_URL);
                mData.add(thumbnailPic);
            }
            if (!isBackPressed) {
                mAdapter.addItems(mData);
                gvpLargeTouchPics.setVisibility(View.VISIBLE);
                int position = getIntent().getIntExtra(ARG_PICTURE_POSITION, 0);
                gvpLargeTouchPics.setCurrentItem(position);
                tvCurrentPage.setText((position + 1) + "/" + mData.size());
            }
        }

    }


    @Override
    public void onClick(View v) {

    }


    @Override
    public void onBackPressed() {
        isBackPressed = true;
        svShowPicture.setVisibility(View.VISIBLE);
        gvpLargeTouchPics.setVisibility(View.GONE);
        tvCurrentPage.setVisibility(View.GONE);
        ivMore.setVisibility(View.GONE);
        (llShowPictureBg.getRootView().findViewById(android.R.id.content)).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        if (mExitTransition != null) {
            mExitTransition.exit(this);
        }
    }

    @Override
    public void onPicLoadingEnd() {
        if (svShowPicture.getVisibility() == View.VISIBLE) {
            if (!isBackPressed) {
                svShowPicture.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onFinishClick() {
        onBackPressed();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tvCurrentPage.setText((position + 1) + "/" + mData.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static void start(Activity activity, View view, Drawable drawable, ArrayList<ThumbnailPic> data, int position) {
        final Intent intent = new Intent(activity, ShowPictureActivity.class);
        intent.putExtra(ARG_PICTURE_POSITION, position);
        intent.putParcelableArrayListExtra(ARG_PICTURE_LIST_URL, data);
        if (drawable == null) {
            activity.startActivity(intent);
        } else {
            ActivityTransitionLauncher.with(activity).from(view).image(Utils.drawableToBitmap(drawable)).launch(intent);
        }
    }

    public static void start(Activity activity, View view, Drawable drawable, String picUrl) {
        final Intent intent = new Intent(activity, ShowPictureActivity.class);
        intent.putExtra(ARG_PICTURE_URL, picUrl);
        ActivityTransitionLauncher.with(activity).from(view).image(Utils.drawableToBitmap(drawable)).launch(intent);
    }
}
