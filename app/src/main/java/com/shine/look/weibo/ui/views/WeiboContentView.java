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
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shine.look.weibo.R;
import com.shine.look.weibo.bean.Status;
import com.shine.look.weibo.ui.adapter.ThumbnailPicAdapter;
import com.shine.look.weibo.utils.Constants;
import com.shine.look.weibo.utils.Utils;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * User:Shine
 * Date:2015-05-10
 * Description:
 */
public class WeiboContentView extends RelativeLayout {

    private static final int COLUMN_COUNT = 3;
    private static final int mAvatarSize = 50;

    private static final int ROOT_PIC_WIDTH = Utils.getScreenWidth() - Utils.dpToPx(36);
    private static final int RETWEETED_PIC_WIDTH = ROOT_PIC_WIDTH - Utils.dpToPx(24);

    private ScaledTransformation mTransformation;

    @InjectView(R.id.tvText)
    ContextTextView mTvText;//文字内容
    @InjectView(R.id.rvThumbnailPic)
    RecyclerView mRvThumbnailPic;//缩略图
    @InjectView(R.id.ivLargePic)
    ImageView mIvLargePic;//一张是显示的大图
    @InjectView(R.id.ivUserProfile)
    ImageView mIvUserProfile;//用户头像
    @InjectView(R.id.tvUserName)
    TextView mTvUserName;//用户名
    @InjectView(R.id.tvCreatedTime)
    DateTextView mTvCreatedTime;//发表时间
    @InjectView(R.id.tvSource)
    TextView mTvSource;//来源

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
        ButterKnife.inject(this, view);
        mRvThumbnailPic.setLayoutManager(new ThumbnailPicLayoutManager(getContext(), COLUMN_COUNT));
        this.mTransformation = new ScaledTransformation();
    }

    public void setWeiboContent(Status status, boolean isRetweeted) {
        if (status == null) {
            return;
        }
        try {
            if (status.user == null) {
                mTvText.dealWithText(status.text);
                mIvUserProfile.setVisibility(View.GONE);
                return;
            }
            mIvUserProfile.setVisibility(View.VISIBLE);
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
            Picasso.with(getContext())
                    .load(status.user.profile_image_url)
                    .resize(mAvatarSize, mAvatarSize)
                    .centerCrop()
                    .into(mIvUserProfile);
            //微博文字内容
            mTvText.dealWithText(status.text);
            //微博图片
            int picSize = status.pic_urls.size();
            if (picSize == 0) {
                mIvLargePic.setVisibility(View.GONE);
                mRvThumbnailPic.setVisibility(View.GONE);
            } else if (picSize == 1) {
                if (isRetweeted) {
                    mTransformation.setBitmapWidth(RETWEETED_PIC_WIDTH);
                } else {
                    mTransformation.setBitmapWidth(ROOT_PIC_WIDTH);
                }
                mIvLargePic.setVisibility(View.VISIBLE);
                mRvThumbnailPic.setVisibility(View.GONE);
                String largeUrl = status.pic_urls.get(0).thumbnail_pic.replace(Constants.URL_THUMBNAIL_PATH, Constants.URL_LARGE_PATH);
                Picasso.with(getContext())
                        .load(largeUrl)
                        .transform(mTransformation)
                        .into(mIvLargePic);
            } else if (picSize > 1) {
                mIvLargePic.setVisibility(View.GONE);
                mRvThumbnailPic.setVisibility(View.VISIBLE);
                mRvThumbnailPic.setAdapter(new ThumbnailPicAdapter(getContext(), status.pic_urls));
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

}
