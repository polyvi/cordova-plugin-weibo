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
