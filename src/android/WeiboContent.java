/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements. See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership. The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied. See the License for the
 specific language governing permissions and limitations
 under the License.
 */

package com.polyvi.plugins.weibo;

import android.content.Intent;

public class WeiboContent {
    private static final String TAG_URL = "url";
    private static final String TAG_REDIRECTOR_URL = "redirectorUrl";
    private String mUrl;
    private String mRedirectorUrl;

    public WeiboContent(String url, String redirectorUrl) {
        mUrl = url;
        mRedirectorUrl = redirectorUrl;
    }

    public void setIntent(Intent intent) {
        intent.putExtra(TAG_URL, mUrl);
        intent.putExtra(TAG_REDIRECTOR_URL, mRedirectorUrl);
    }

    public String getUrl() {
        return mUrl;
    }

    public String getRedirectorUrl() {
        return mRedirectorUrl;
    }
}
