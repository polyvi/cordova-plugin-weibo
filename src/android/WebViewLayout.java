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

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class WebViewLayout extends RelativeLayout {
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private Activity mActivity;
    private Dialog mDialog;

    public WebViewLayout(Activity activity) {
        super(activity);
        mActivity = activity;
        init();
    }

    private void init() {
        setBackgroundColor(0xffffff);
        mWebView = createWebView();
        addView(mWebView);
        mProgressBar = createProgressBar();
        addView(mProgressBar);
        buildDilog();
    }

    public WebView getWebView() {
        return mWebView;
    }

    public void showProgress() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideProgress() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 停止加载并清理webview及容器
     */
    public void destroy() {
        mWebView.stopLoading();
        mWebView.destroy();
        mDialog.dismiss();
    }

    /**
     * 创建存放WebViewLayout的Dialog容器
     */
    private void buildDilog() {
        mDialog = new Dialog(mActivity, android.R.style.Theme_NoTitleBar);
        mDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(true);
        mDialog.setContentView(this);
        mDialog.show();
    }

    /**
     * 创建和设置webview
     *
     * @return
     */
    private WebView createWebView() {
        WebView webView = new WebView(mActivity);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.requestFocus();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        webView.setLayoutParams(params);
        return webView;
    }

    /**
     * 创建和设置进度条
     *
     * @return
     */
    private ProgressBar createProgressBar() {
        ProgressBar progressBar = new ProgressBar(mActivity);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        progressBar.setLayoutParams(params);
        return progressBar;
    }
}
