package org.apache.cordova.weibo;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;

public class Weibo extends CordovaPlugin{

    private static String HEAD_URL = "https://api.weibo.com/oauth2/authorize?display=mobile";
    private static String CLIENT_ID = "&client_id=";
    private static String RESPONSE_TYPE = "&response_type=token";
    private static String REDIRECT_URI = "&redirect_uri=";
    private static String ERROR_MSG = "clientID or redirect_uri is empty";
    private static final String TAG_WEIBO_LOGIN = "login";
    private CallbackContext mCallbackContext;

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArry of arguments for the plugin.
     * @param callbackContext   The callback id used when calling back into JavaScript.
     * @return                  True if the action was valid, false if not.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        mCallbackContext = callbackContext;
        if (action.equals(TAG_WEIBO_LOGIN)) {
            try {
                weiboLogin(args.getString(0), args.getString(1));
            } catch (JSONException e) {
                mCallbackContext.sendPluginResult(new PluginResult(
                        PluginResult.Status.ERROR, e.getMessage()));
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private void weiboLogin(String clientID, String redirector) {
        if(isEmptyString(clientID) || isEmptyString(redirector)) {
            mCallbackContext.sendPluginResult(new PluginResult(
                    PluginResult.Status.ERROR, ERROR_MSG));
        }
        String url = HEAD_URL + CLIENT_ID + clientID + RESPONSE_TYPE + REDIRECT_URI + redirector;
        String redirectorUrl = redirector;
        final Activity activity = this.cordova.getActivity();
        final WeiboContent weiboContent = new WeiboContent(url, redirectorUrl);
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new WebViewHandler(activity, mCallbackContext, weiboContent);
            }
        });

    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmptyString(String str) {
        return null == str || "".equals(str);
    }
}
