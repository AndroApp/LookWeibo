package com.shine.look.weibo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * User:Shine
 * Date:2015-05-04
 * Description:
 */
public class ThumbnailPic implements Parcelable {
    /**
     * 缩略图片地址（小图），没有时不返回此字段
     */
    public String thumbnail_pic;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.thumbnail_pic);
    }

    public ThumbnailPic() {
    }

    private ThumbnailPic(Parcel in) {
        this.thumbnail_pic = in.readString();
    }

    public static final Creator<ThumbnailPic> CREATOR = new Creator<ThumbnailPic>() {
        public ThumbnailPic createFromParcel(Parcel source) {
            return new ThumbnailPic(source);
        }

        public ThumbnailPic[] newArray(int size) {
            return new ThumbnailPic[size];
        }
    };
}
