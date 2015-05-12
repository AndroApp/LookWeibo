package com.shine.look.weibo.utils;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.shine.look.weibo.WeiboApplication;

import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User:Shine
 * Date:2015-05-07
 * Description:
 */
public class ExpressionMap {

    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\[(\\S+?)\\]");

    private static ExpressionMap mInstance = new ExpressionMap();

    public static ExpressionMap getInstance() {
        return mInstance;
    }

    private static final int mImageSize = Utils.dpToPx(20);

    private LinkedHashMap map;

    public LinkedHashMap getMap() {
        return map;
    }

    private ExpressionMap() {
        map = new LinkedHashMap();
        map.put("[爱你]", "d_aini");
        map.put("[奥特曼]", "d_aoteman");
        map.put("[拜拜]", "d_baibai");
        map.put("[悲伤]", "d_beishang");
        map.put("[鄙视]", "d_bishi");
        map.put("[闭嘴]", "d_bizui");
        map.put("[馋嘴]", "d_chanzui");
        map.put("[吃惊]", "d_chijing");
        map.put("[哈欠]", "d_dahaqi");
        map.put("[打脸]", "d_dalian");
        map.put("[顶]", "d_ding");
        map.put("[doge]", "d_doge");
        map.put("[肥皂]", "d_feizao");
        map.put("[感冒]", "d_ganmao");
        map.put("[鼓掌]", "d_guzhang");
        map.put("[哈哈]", "d_haha");
        map.put("[害羞]", "d_haixiu");
        map.put("[汗]", "d_han");
        map.put("[呵呵]", "d_hehe");
        map.put("[黑线]", "d_heixian");
        map.put("[哼]", "d_heng");
        map.put("[色]", "d_huaxin");
        map.put("[挤眼]", "d_jiyan");
        map.put("[可爱]", "d_keai");
        map.put("[可怜]", "d_kelian");
        map.put("[酷]", "d_ku");
        map.put("[困]", "d_kun");
        map.put("[白眼]", "d_landelini");
        map.put("[浪]", "d_lang");
        map.put("[泪]", "d_lei");
        map.put("[喵喵]", "d_miao");
        map.put("[男孩儿]", "d_nanhaier");
        map.put("[怒]", "d_nu");
        map.put("[怒骂]", "d_numa");
        map.put("[女孩儿]", "d_nvhaier");
        map.put("[钱]", "d_qian");
        map.put("[亲亲]", "d_qinqin");
        map.put("[傻眼]", "d_shayan");
        map.put("[生病]", "d_shengbing");
        map.put("[草泥马]", "d_shenshou");
        map.put("[失望]", "d_shiwang");
        map.put("[衰]", "d_shuai");
        map.put("[睡觉]", "d_shuijiao");
        map.put("[思考]", "d_sikao");
        map.put("[太开心]", "d_taikaixin");
        map.put("[偷笑]", "d_touxiao");
        map.put("[吐]", "d_tu");
        map.put("[兔子]", "d_tuzi");
        map.put("[挖鼻]", "d_wabishi");
        map.put("[委屈]", "d_weiqu");
        map.put("[笑cry]", "d_xiaoku");
        map.put("[熊猫]", "d_xiongmao");
        map.put("[嘻嘻]", "d_xixi");
        map.put("[嘘]", "d_xu");
        map.put("[阴险]", "d_yinxian");
        map.put("[疑问]", "d_yiwen");
        map.put("[右哼哼]", "d_youhengheng");
        map.put("[晕]", "d_yun");
        map.put("[抓狂]", "d_zhuakuang");
        map.put("[猪头]", "d_zhutou");
        map.put("[最右]", "d_zuiyou");
        map.put("[左哼哼]", "d_zuohengheng");
        map.put("[NO]", "h_buyao");
        map.put("[good]", "h_good");
        map.put("[haha]", "h_haha");
        map.put("[来]", "h_lai");
        map.put("[ok]", "h_ok");
        map.put("[弱]", "h_ruo");
        map.put("[握手]", "h_woshou");
        map.put("[耶]", "h_ye");
        map.put("[赞]", "h_zan");
        map.put("[作揖]", "h_zuoyi");
        map.put("[互粉]", "f_hufen");
        map.put("[心]", "l_xin");
        map.put("[伤心]", "l_shangxin");
        map.put("[威武]", "f_v5");
        map.put("[鲜花]", "w_xianhua");
        map.put("[钟]", "o_zhong");
        map.put("[浮云]", "w_fuyun");
        map.put("[飞机]", "o_feiji");
        map.put("[月亮]", "w_yueliang");
        map.put("[太阳]", "w_taiyang");
        map.put("[微风]", "w_weifeng");
        map.put("[下雨]", "w_xiayu");
        map.put("[给力]", "f_geili");
        map.put("[神马]", "f_shenma");
        map.put("[围观]", "o_weiguan");
        map.put("[话筒]", "o_huatong");
        map.put("[萌]", "f_meng");
        map.put("[囧]", "f_jiong");
        map.put("[织]", "f_zhi");
        map.put("[礼物]", "o_liwu");
        map.put("[喜]", "f_xi");
        map.put("[围脖]", "o_weibo");
        map.put("[音乐]", "o_yinyue");
        map.put("[绿丝带]", "o_lvsidai");
        map.put("[蛋糕]", "o_dangao");
        map.put("[蜡烛]", "o_lazhu");
        map.put("[干杯]", "o_ganbei");
        map.put("[照相机]", "o_zhaoxiangji");
        map.put("[沙尘暴]", "w_shachenbao");
    }

    public static void addExpression(Spannable spannable) {
        Matcher matcher = EXPRESSION_PATTERN.matcher(spannable);
        int start;
        int end = 0;
        while (matcher.find(end)) {
            start = matcher.start();
            end = matcher.end();
            String group = matcher.group();
            String imageName = (String) ExpressionMap.getInstance().getMap().get(group);
            int resId = Utils.getResourceByImageName(imageName);
            if (resId == 0) {
                continue;
            }
            Drawable drawable = WeiboApplication.getContext().getResources().getDrawable(resId);
            drawable.setBounds(0, 0, mImageSize, mImageSize);
            spannable.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }
}
