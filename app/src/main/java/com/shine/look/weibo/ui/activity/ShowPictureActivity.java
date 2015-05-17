package com.shine.look.weibo.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import com.shine.look.weibo.ui.fragment.HomeFragment;
import com.shine.look.weibo.ui.transition.ActivityTransition;
import com.shine.look.weibo.ui.transition.ExitActivityTransition;
import com.shine.look.weibo.ui.views.byakugallery.GalleryViewPager;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * User:Shine
 * Date:2015-05-14
 * Description:
 */
public class ShowPictureActivity extends AppCompatActivity implements View.OnClickListener, GalleryLargeTouchPicFragment.OnPicLoadingEnd, ViewPager.OnPageChangeListener {

    private ExitActivityTransition mExitTransition;
    private ArrayList<ThumbnailPic> mData;
    private boolean isBackPressed;
    private GalleryLargeTouchPicAdapter mAdapter;

    @InjectView(R.id.ivShowPicture)
    ImageView ivShowPicture;
    @InjectView(R.id.llShowPictureBg)
    View llShowPictureBg;
    @InjectView(R.id.ivMore)
    ImageButton ivMore;
    @InjectView(R.id.svShowPicture)
    ScrollView svShowPicture;
    @InjectView(R.id.gvpLargeTouchPics)
    GalleryViewPager gvpLargeTouchPics;
    @InjectView(R.id.tvCurrentPage)
    TextView tvCurrentPage;

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
        ButterKnife.inject(this);

        if (getIntent() != null) {
            mData = getIntent().getParcelableArrayListExtra(HomeFragment.ARG_PICTURE_LIST_URL);
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
                thumbnailPic.thumbnail_pic = getIntent().getStringExtra(HomeFragment.ARG_PICTURE_URL);
                mData.add(thumbnailPic);
            }
            if (!isBackPressed) {
                mAdapter.addItems(mData);
                gvpLargeTouchPics.setVisibility(View.VISIBLE);
                int position = getIntent().getIntExtra(HomeFragment.ARG_PICTURE_POSITION, 0);
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
}
