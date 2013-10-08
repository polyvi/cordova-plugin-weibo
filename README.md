
#Weibo

Plugin for weibo, the popular twitter-like service in China. Help user get oauth2/access_token to further access weibo content.

Please refer to [weibo oauth2](ttp://open.weibo.com/wiki/OAuth2/access_token)

##Install 

1. To install this plugin, try

   ```cordova plugin add https://github.com/polyvi/cordova-plugin-weibo.git```

   and further follow the [Command-line Interface Guide](http://cordova.apache.org/docs/en/edge/guide_cli_index.md.html#The%20Command-line%20Interface).

2. If you are not using the Cordova Command-line Interface, follow [Using Plugman to Manage Plugins](http://cordova.apache.org/docs/en/edge/guide_plugin_ref_plugman.md.html).

##Methods

  ```Weibo.login(clientId, redirectUrl, success, fail);```

* login
   * clientId, the AppKey assigned by weibo service when you apply a new application
   * redirectUrl, the callback url same as in your app registration
   * success, success callback
   * fail, error callback

##Supported Platforms

- Android
- iOS

##Quick Example

    var clientId = "12312312313";
    var redirectUrl = "http://www.server.com";

    // ... later on ...

    Weibo.login(clientId, redirectUrl, success, fail);

