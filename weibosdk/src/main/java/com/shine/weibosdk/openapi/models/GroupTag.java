/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
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

package com.shine.weibosdk.openapi.models;

import org.json.JSONObject;

/**
 * 分组标签结构体。
 * 
 * @author SINA
 * @since 2013-11-24
 */
public class GroupTag {
    /** 分组的组号 */
    public String tag;

    public static GroupTag parse(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        GroupTag tag = new GroupTag();
        // TODO: 结构不明？
        //tag.tag = jsonObject.optString("", "");
        return tag;
    }
}
