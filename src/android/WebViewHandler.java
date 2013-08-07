package org.apache.cordova.weibo;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewHandler {
    private WebView mWebView;
    private final String CODE = "access_token";
    private boolean mTokenReceived = true;
    private WebViewLayout webViewLayout;
    private WeiboContent mWeiboContent;
    private Activity mActivity;
    private CallbackContext mCallbackContext;

    public  WebViewHandler(Activity activity, CallbackContext callbackContext, WeiboContent weiboContent) {
        mActivity = activity;
        mCallbackContext = callbackContext;
        mWeiboContent = weiboContent;
        webViewLayout = new WebViewLayout(activity);
        mWebView = webViewLayout.getWebView();
        initData();
    }

    private void initData() {
        mWebView.setWebViewClient(new WeiboWebViewClient(mWeiboContent.getRedirectorUrl()));
        CookieSyncManager.createInstance(mActivity);
        mWebView.loadUrl(mWeiboContent.getUrl());
    }


    private class WeiboWebViewClient extends WebViewClient {
        private String mRedirectorUrl;
        public WeiboWebViewClient(String redirectorUrl) {
            mRedirectorUrl = redirectorUrl;
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webViewLayout.showProgress();
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                String description, String failingUrl) {
            mCallbackContext.error(description);
            webViewLayout.destroy();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            webViewLayout.showProgress();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            webViewLayout.hideProgress();
            if (mTokenReceived && url.startsWith(mRedirectorUrl)) {
                try {
                    String[] array =  url.split("[&,#]");
                    String redirector_url = array[0];
                    String access_token = array[1].substring("access_token=".length());
                    String remind_in = array[2].substring("remind_in=".length());
                    String expires_in = array[3].substring("expires_in=".length());
                    String uid = array[4].substring("uid=".length());
                    JSONObject result = new JSONObject();
                    result.put("redirect_uri", redirector_url);
                    result.put("access_token", access_token);
                    result.put("remind_in", remind_in);
                    result.put("expires_in", expires_in);
                    result.put("uid", uid);
                    mCallbackContext.success(result);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    mCallbackContext.error(e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                    mCallbackContext.error(e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    mCallbackContext.error(e.getMessage());
                }
                mTokenReceived = false;
                webViewLayout.destroy();
                return;
            }
            super.onPageFinished(view, url);
        }
    }
}
