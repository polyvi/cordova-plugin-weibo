/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
*/

/**
 * 该模块提供新浪微博应用授权功能，关于应用授权(access_token), 请参考<a href="http://open.weibo.com/wiki/OAuth2/access_token">OAuth2/access token</a>
 * @module Weibo
 * @main Weibo
 */

 /**
  * 登录新浪微博并获取应用授权
  * @class Weibo
  * @platform Android, iOS
  * @since 3.0.0
  */

cordova.define("cordova/plugin/Weibo",
  function (require, exports, module) {
    var exec = require('cordova/exec');

    function Weibo() {};

    /**
     * 微博登陆，执行成功会返回授权相关信息
     * @example
            var clientId = "3968026932";
            var redirectUrl = "http://www.igo.cn";

            function WeiboLogin() {
                navigator.weibo.login(clientId, redirectUrl, success, fail);
            };

            function success(result) {
                var loginResult = "";
                loginResult = loginResult + "redirect_url:" + result.redirect_url + "\n";
                loginResult = loginResult + "access_token:" + result.access_token + "\n";
                loginResult = loginResult + "remind_in:" + result.remind_in + "\n";
                loginResult = loginResult + "expires_in:" + result.expires_in + "\n";
                loginResult = loginResult + "uid:" + result.uid;
                document.getElementById('status').innerText = "success";
                document.getElementById('result').innerText = loginResult;
            };

            function fail(result) {
                document.getElementById('status').innerText = "fail";
                document.getElementById('result').innerText = "fail:" + result;
            };

     * @param {String}   clientId       申请应用时分配的AppKey，申请地址 http://open.weibo.com/apps/new
     * @param {String}   redirectUrl    回调地址，参考redirect_uri, 需与注册应用里的回调地址一致
     * @param {Function} successCallback  成功回调函数
     * @param {Function} successCallback.redirect_url  回调地址，需与注册应用里的回调地址一致
     * @param {Function} successCallback.access_token    接口获取授权后的access token
     * @param {Function} successCallback.remind_in       access_token的生命周期（该参数即将废弃，开发者请使用expires_in）
     * @param {Function} successCallback.expires_in      access_token的生命周期，单位是秒数
     * @param {Function} successCallback.uid             当前授权用户的UID
     * @param {Function} errorCallback    失败回调函数
     * @param {Function} errorCallback.errorMsg   失败回调错误信息
     */
    Weibo.prototype.login = function(clientId, redirectUrl, successCallback, errorCallback){
        exec(successCallback, errorCallback, "Weibo", "login", [clientId, redirectUrl]);
    };

    module.exports = new Weibo();
});
