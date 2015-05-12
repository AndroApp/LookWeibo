package com.shine.look.weibo.ui.fragment;

import android.app.Fragment;

import com.shine.look.weibo.WeiboApplication;
import com.squareup.leakcanary.RefWatcher;

/**
 * User:Shine
 * Date:2015-05-11
 * Description:
 */
public class BaseFragment extends Fragment {

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = WeiboApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
