package com.shine.look.weibo.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.Status;
import com.shine.look.weibo.ui.adapter.ThumbnailPicAdapter;
import com.shine.look.weibo.ui.utils.ScaledTransformation;
import com.shine.look.weibo.utils.Constants;
import com.shine.look.weibo.utils.Utils;


/**
 * User:Shine
 * Date:2015-05-10
 * Description:
 */
public class WeiboContentView extends RelativeLayout {

    private static final int COLUMN_COUNT = 3;
    private static final int mAvatarSize = 50;

    private static final int ROOT_PIC_WIDTH = Utils.getScreenWidth() - Utils.dpToPx(32);
    private static final int RETWEETED_PIC_WIDTH = ROOT_PIC_WIDTH - Utils.dpToPx(32);

    private ThumbnailPicAdapter.OnThumbnailPicListener mOnThumbnailPicListener;
    private ScaledTransformation mTransformation;
    private RequestManager mGlide;
    private String mPicUrl;

    private OnPictureListener mOnPictureListener;

    private ContentTextView mTvText;//文字内容
    private RecyclerView mRvThumbnailPic;//缩略图
    private ImageView mIvLargePic;//一张是显示的大图
    private ImageView mIvUserProfile;//用户头像
    private TextView mTvUserName;//用户名
    private DateTextView mTvCreatedTime;//发表时间
    private TextView mTvSource;//来源


    public WeiboContentView(Context context) {
        super(context);
        init();
    }

    public WeiboContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WeiboContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WeiboContentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_weibo_content, this);

        mTvText = (ContentTextView) view.findViewById(R.id.tvText);
        mRvThumbnailPic = (RecyclerView) view.findViewById(R.id.rvThumbnailPic);
        mIvLargePic = (ImageView) view.findViewById(R.id.ivLargePic);
        mIvUserProfile = (ImageView) view.findViewById(R.id.ivUserProfile);
        mTvUserName = (TextView) view.findViewById(R.id.tvUserName);
        mTvCreatedTime = (DateTextView) view.findViewById(R.id.tvCreatedTime);
        mTvSource = (TextView) view.findViewById(R.id.tvSource);

        mRvThumbnailPic.setLayoutManager(new ThumbnailPicLayoutManager(getContext(), COLUMN_COUNT));
        mRvThumbnailPic.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mTransformation = new ScaledTransformation(getContext());
        mIvLargePic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnPictureListener != null) {
                    v.setTag(mPicUrl);
                    mOnPictureListener.onPictureClick(v);
                }
            }
        });
    }

    public void setWeiboContent(Status status, boolean isRetweeted) {
        try {
            if (status.user == null) {
                mTvText.dealWithText(status.text);
                mIvUserProfile.setVisibility(View.GONE);
                mTvUserName.setVisibility(View.GONE);
                mTvCreatedTime.setVisibility(View.GONE);
                mTvSource.setVisibility(View.GONE);
                return;
            }
            mIvUserProfile.setVisibility(View.VISIBLE);
            mTvUserName.setVisibility(View.VISIBLE);
            mTvCreatedTime.setVisibility(View.VISIBLE);
            mTvSource.setVisibility(View.VISIBLE);
            //用户名
            mTvUserName.setText(status.user.screen_name);
            //发表时间
            mTvCreatedTime.setTextByDate(status.created_at);
            //来源
            Spannable spannable = new SpannableString(Html.fromHtml(status.source));
            spannable.setSpan(new ForegroundColorSpan(0) {
                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                    ds.setColor(getContext().getResources().getColor(R.color.secondary_text));
                }
            }, 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTvSource.setText(spannable);
            //用户头像
            mGlide.load(status.user.profile_image_url)
                    .asBitmap()
                    .override(mAvatarSize, mAvatarSize)
                    .into(mIvUserProfile);
            //微博文字内容
            mTvText.dealWithText(status.text + " ");
            //微博图片
            int picSize = status.pic_urls.size();
            if (picSize == 0) {
                mIvLargePic.setVisibility(View.GONE);
                mRvThumbnailPic.setVisibility(View.GONE);
            } else if (picSize == 1) {
                mIvLargePic.setVisibility(View.VISIBLE);
                mRvThumbnailPic.setVisibility(View.GONE);
                // mPicUrl = "http://ww1.sinaimg.cn/large/7fd54a81tw1erwr8ox7x9j20cs4817uy.jpg";
                mPicUrl = status.pic_urls.get(0).thumbnail_pic.replace(Constants.URL_THUMBNAIL_PATH, Constants.URL_BMIDDLE_PATH);
                mTransformation.setBitmapWidth(isRetweeted ? RETWEETED_PIC_WIDTH : ROOT_PIC_WIDTH);
                mTransformation.setGifLabel(mPicUrl.indexOf(".gif") != -1);
                mGlide.load(mPicUrl)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transform(mTransformation)
                        .into(mIvLargePic);

            } else if (picSize > 1) {
                mIvLargePic.setVisibility(View.GONE);
                mRvThumbnailPic.setVisibility(View.VISIBLE);
                ThumbnailPicAdapter adapter = new ThumbnailPicAdapter(getContext(), status.pic_urls, mGlide);
                adapter.setOnThumbnailPicListener(mOnThumbnailPicListener);
                mRvThumbnailPic.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //视频  --- 后续在添加
           /* Matcher matcher = mVideoPattern.matcher(statusBean.text);
            if (matcher.find()) {
                String url = matcher.group();
                UrlShortModel model = new UrlShortModel((Activity) mContext);
                model.setUrlShort(url);
                model.request();
                model.setOnRequestListener(new BaseModel.OnRequestListener() {
                    @Override
                    public void onSuccess(Object info) {
                        Log.i("huangruimin", "string");
                    }

                    @Override
                    public void onFailure(VolleyError error) {
                        Log.i("huang", error.toString());
                    }
                });

            }*/
    }

    public void setGlide(RequestManager glide) {
        mGlide = glide;
    }

    public void setOnPictureListener(OnPictureListener onPictureListener) {
        mOnPictureListener = onPictureListener;
    }

    public void setOnThumbnailPicListener(ThumbnailPicAdapter.OnThumbnailPicListener listener) {
        mOnThumbnailPicListener = listener;
    }

    public interface OnPictureListener {
        public void onPictureClick(View v);
    }


}
