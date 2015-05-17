package com.shine.look.weibo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.ThumbnailPic;
import com.shine.look.weibo.ui.views.byakugallery.TileBitmapDrawable;
import com.shine.look.weibo.ui.views.byakugallery.TouchImageView;
import com.shine.look.weibo.utils.Constants;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * User:Shine
 * Date:2015-05-16
 * Description:
 */
public class GalleryLargeTouchPicFragment extends BaseFragment implements View.OnClickListener {

    private static final String ARG_PIC_URL = "com.shine.look.weibo.thumbnailPicUrl";

    @InjectView(R.id.gallery_item_image)
    TouchImageView mGalleryImage;
    @InjectView(R.id.gallery_item_gif_image)
    GifImageView mGalleryGifImage;
    @InjectView(R.id.gallery_item_progress)
    ProgressBar mGalleryProgress;

    private ThumbnailPic mThumbnailPic;

    private OnPicLoadingEnd mOnPicLoadingListener;


    public static GalleryLargeTouchPicFragment getInstance(ThumbnailPic thumbnailPic) {
        final GalleryLargeTouchPicFragment instance = new GalleryLargeTouchPicFragment();
        final Bundle params = new Bundle();
        params.putParcelable(ARG_PIC_URL, thumbnailPic);
        instance.setArguments(params);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThumbnailPic = getArguments().getParcelable(ARG_PIC_URL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gallery_view_pager_item, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        mGalleryGifImage.setOnClickListener(this);
        mGalleryImage.setOnClickListener(this);
        loadPicture();
    }

    private void loadPicture() {
        String url = mThumbnailPic.thumbnail_pic.replace(Constants.URL_THUMBNAIL_PATH, Constants.URL_LARGE_PATH);
        if (!url.endsWith(".gif")) {
            Glide.with(this)
                    .load(url)
                    .asBitmap()
                    .toBytes()
                    .into(new SimpleTarget<byte[]>() {
                        @Override
                        public void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                            TileBitmapDrawable.attachTileBitmapDrawable(mGalleryImage, resource, null, new TileBitmapDrawable.OnInitializeListener() {
                                @Override
                                public void onStartInitialization() {
                                    mGalleryProgress.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onEndInitialization() {
                                    mGalleryProgress.setVisibility(View.GONE);
                                    mGalleryImage.setVisibility(View.VISIBLE);
                                    if (mOnPicLoadingListener != null) {
                                        mOnPicLoadingListener.onPicLoadingEnd();
                                    }
                                }

                                @Override
                                public void onError(Exception ex) {
                                    mGalleryProgress.setVisibility(View.GONE);
                                }
                            });
                        }
                    });
        } else {
            Glide.with(this)
                    .load(url)
                    .asGif()
                    .toBytes()
                    .into(new SimpleTarget<byte[]>() {
                        @Override
                        public void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                            try {
                                GifDrawable gifDrawable = new GifDrawable(resource);
                                mGalleryGifImage.setImageDrawable(gifDrawable);
                                mGalleryGifImage.setVisibility(View.VISIBLE);
                                mGalleryProgress.setVisibility(View.GONE);
                                if (mOnPicLoadingListener != null) {
                                    mOnPicLoadingListener.onPicLoadingEnd();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                mGalleryProgress.setVisibility(View.GONE);
                            }
                        }
                    });

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mGalleryGifImage != null) {
            mGalleryGifImage.setImageDrawable(null);
            mGalleryGifImage = null;
        }
    }

    public void setOnPicLoadingEnd(OnPicLoadingEnd listener) {
        mOnPicLoadingListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnPicLoadingListener != null) {
            mOnPicLoadingListener.onFinishClick();
        }
    }

    public interface OnPicLoadingEnd {
        public void onPicLoadingEnd();

        public void onFinishClick();
    }

}
