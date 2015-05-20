package com.shine.look.weibo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * User:Shine
 * Date:2015-05-17
 * Description:
 */
public class User implements Parcelable {
    /** 用户UID（int64） */
    public String id;
    /** 字符串型的用户 UID */
    public String idstr;
    /** 用户昵称 */
    public String screen_name;
    /** 友好显示名称 */
    public String name;
    /** 用户所在省级ID */
    public int province;
    /** 用户所在城市ID */
    public int city;
    /** 用户所在地 */
    public String location;
    /** 用户个人描述 */
    public String description;
    /** 用户博客地址 */
    public String url;
    /** 用户头像地址，50×50像素 */
    public String profile_image_url;
    /** 用户的微博统一URL地址 */
    public String profile_url;
    /** 用户的个性化域名 */
    public String domain;
    /** 用户的微号 */
    public String weihao;
    /** 性别，m：男、f：女、n：未知 */
    public String gender;
    /** 粉丝数 */
    public int followers_count;
    /** 关注数 */
    public int friends_count;
    /** 微博数 */
    public int statuses_count;
    /** 收藏数 */
    public int favourites_count;
    /** 用户创建（注册）时间 */
    public String created_at;
    /** 暂未支持 */
    public boolean following;
    /** 是否允许所有人给我发私信，true：是，false：否 */
    public boolean allow_all_act_msg;
    /** 是否允许标识用户的地理位置，true：是，false：否 */
    public boolean geo_enabled;
    /** 是否是微博认证用户，即加V用户，true：是，false：否 */
    public boolean verified;
    /** 暂未支持 */
    public int verified_type;
    /** 用户备注信息，只有在查询用户关系时才返回此字段 */
    public String remark;
    /** 用户的最近一条微博信息字段 */
    public Status status;
    /** 是否允许所有人对我的微博进行评论，true：是，false：否 */
    public boolean allow_all_comment;
    /** 用户大头像地址 */
    public String avatar_large;
    /** 用户高清大头像地址 */
    public String avatar_hd;
    /** 认证原因 */
    public String verified_reason;
    /** 该用户是否关注当前登录用户，true：是，false：否 */
    public boolean follow_me;
    /** 用户的在线状态，0：不在线、1：在线 */
    public int online_status;
    /** 用户的互粉数 */
    public int bi_followers_count;
    /** 用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语 */
    public String lang;

    /** 注意：以下字段暂时不清楚具体含义，OpenAPI 说明文档暂时没有同步更新对应字段 */
    public String star;
    public String mbtype;
    public String mbrank;
    public String block_word;
    public String cover_image_phone;


    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.idstr);
        dest.writeString(this.screen_name);
        dest.writeString(this.name);
        dest.writeInt(this.province);
        dest.writeInt(this.city);
        dest.writeString(this.location);
        dest.writeString(this.description);
        dest.writeString(this.url);
        dest.writeString(this.profile_image_url);
        dest.writeString(this.profile_url);
        dest.writeString(this.domain);
        dest.writeString(this.weihao);
        dest.writeString(this.gender);
        dest.writeInt(this.followers_count);
        dest.writeInt(this.friends_count);
        dest.writeInt(this.statuses_count);
        dest.writeInt(this.favourites_count);
        dest.writeString(this.created_at);
        dest.writeByte(following ? (byte) 1 : (byte) 0);
        dest.writeByte(allow_all_act_msg ? (byte) 1 : (byte) 0);
        dest.writeByte(geo_enabled ? (byte) 1 : (byte) 0);
        dest.writeByte(verified ? (byte) 1 : (byte) 0);
        dest.writeInt(this.verified_type);
        dest.writeString(this.remark);
        dest.writeByte(allow_all_comment ? (byte) 1 : (byte) 0);
        dest.writeString(this.avatar_large);
        dest.writeString(this.avatar_hd);
        dest.writeString(this.verified_reason);
        dest.writeByte(follow_me ? (byte) 1 : (byte) 0);
        dest.writeInt(this.online_status);
        dest.writeInt(this.bi_followers_count);
        dest.writeString(this.lang);
        dest.writeString(this.star);
        dest.writeString(this.mbtype);
        dest.writeString(this.mbrank);
        dest.writeString(this.block_word);
        dest.writeString(this.cover_image_phone);
    }

    private User(Parcel in) {
        this.id = in.readString();
        this.idstr = in.readString();
        this.screen_name = in.readString();
        this.name = in.readString();
        this.province = in.readInt();
        this.city = in.readInt();
        this.location = in.readString();
        this.description = in.readString();
        this.url = in.readString();
        this.profile_image_url = in.readString();
        this.profile_url = in.readString();
        this.domain = in.readString();
        this.weihao = in.readString();
        this.gender = in.readString();
        this.followers_count = in.readInt();
        this.friends_count = in.readInt();
        this.statuses_count = in.readInt();
        this.favourites_count = in.readInt();
        this.created_at = in.readString();
        this.following = in.readByte() != 0;
        this.allow_all_act_msg = in.readByte() != 0;
        this.geo_enabled = in.readByte() != 0;
        this.verified = in.readByte() != 0;
        this.verified_type = in.readInt();
        this.remark = in.readString();
        this.status = in.readParcelable(Status.class.getClassLoader());
        this.allow_all_comment = in.readByte() != 0;
        this.avatar_large = in.readString();
        this.avatar_hd = in.readString();
        this.verified_reason = in.readString();
        this.follow_me = in.readByte() != 0;
        this.online_status = in.readInt();
        this.bi_followers_count = in.readInt();
        this.lang = in.readString();
        this.star = in.readString();
        this.mbtype = in.readString();
        this.mbrank = in.readString();
        this.block_word = in.readString();
        this.cover_image_phone = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
