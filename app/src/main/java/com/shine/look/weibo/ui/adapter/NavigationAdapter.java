package com.shine.look.weibo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shine.look.weibo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * User:Shine
 * Date:2015-05-11
 * Description:
 */
public class NavigationAdapter extends ArrayAdapter<NavigationAdapter.NavigationMenuItem> {

    private static final int TYPE_MENU_ITEM = 0;
    private static final int TYPE_DIVIDER = 1;

    private final LayoutInflater mInflater;
    private final List<NavigationMenuItem> mMenuItems = new ArrayList<>();

    public NavigationAdapter(Context context) {
        super(context, 0);
        this.mInflater = LayoutInflater.from(context);
        setupMenuItems();
    }

    private void setupMenuItems() {
        mMenuItems.add(new NavigationMenuItem(R.drawable.ic_action_home, getContext().getString(R.string.home)));
//        mMenuItems.add(new NavigationMenuItem(R.drawable.ic_action_supervisor_account, "好友"));
//        mMenuItems.add(new NavigationMenuItem(R.drawable.ic_action_room, "周边"));
//        mMenuItems.add(new NavigationMenuItem(R.drawable.ic_action_grade, "收藏"));
//        mMenuItems.add(new NavigationMenuItem(R.drawable.ic_action_email, "私信"));
//        mMenuItems.add(NavigationMenuItem.dividerMenuItem());
//        mMenuItems.add(new NavigationMenuItem(0, "设置"));
//        mMenuItems.add(new NavigationMenuItem(0, "关于"));
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMenuItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public NavigationMenuItem getItem(int position) {
        return mMenuItems.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return mMenuItems.get(position).isDivider ? TYPE_DIVIDER : TYPE_MENU_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == TYPE_MENU_ITEM) {
            MenuItemViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_navigation_menu, parent, false);
                holder = new MenuItemViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (MenuItemViewHolder) convertView.getTag();
            }
            NavigationMenuItem item = mMenuItems.get(position);
            holder.ivIcon.setImageResource(item.iconResId);
            holder.tvLabel.setText(item.label);
            holder.ivIcon.setVisibility(item.iconResId == 0 ? View.GONE : View.VISIBLE);
            return convertView;
        } else {
            return mInflater.inflate(R.layout.item_menu_divider, parent, false);
        }
    }

    public static class MenuItemViewHolder {

        ImageView ivIcon;
        TextView tvLabel;

        public MenuItemViewHolder(View view) {
            ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
            tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        }

    }

    public static class NavigationMenuItem {
        public int iconResId;
        public String label;
        public boolean isDivider;

        private NavigationMenuItem() {

        }

        public NavigationMenuItem(int iconResId, String label) {
            this.iconResId = iconResId;
            this.label = label;
            this.isDivider = false;
        }

        public static NavigationMenuItem dividerMenuItem() {
            NavigationMenuItem navigationMenuItem = new NavigationMenuItem();
            navigationMenuItem.isDivider = true;
            return navigationMenuItem;
        }
    }
}
