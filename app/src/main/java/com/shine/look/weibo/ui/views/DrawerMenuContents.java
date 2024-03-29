/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shine.look.weibo.ui.views;

import android.content.Context;

import com.shine.look.weibo.R;
import com.shine.look.weibo.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawerMenuContents {
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_ICON = "icon";

    private ArrayList<Map<String, ?>> items;
    private Class[] activities;

    public DrawerMenuContents(Context ctx) {
        activities = new Class[1];
        items = new ArrayList<>(1);

        activities[0] = MainActivity.class;
        items.add(populateDrawerItem(ctx.getString(R.string.home), R.drawable.ic_action_home));

    }

    public List<Map<String, ?>> getItems() {
        return items;
    }

    public Class getActivity(int position) {
        return activities[position];
    }

    public int getPosition(Class activityClass) {
        for (int i = 0; i < activities.length; i++) {
            if (activities[i].equals(activityClass)) {
                return i;
            }
        }
        return -1;
    }

    private Map<String, ?> populateDrawerItem(String title, int icon) {
        HashMap<String, Object> item = new HashMap<>();
        item.put(FIELD_TITLE, title);
        item.put(FIELD_ICON, icon);
        return item;
    }
}
