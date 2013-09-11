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

#import "CDVWeibo.h"

#define HEAD_URL        @"https://open.weibo.cn/oauth2/authorize?display=mobile&forcelogin=true"
#define CLIENT_ID       @"&client_id="
#define RESPONSE_TYPE   @"&response_type=token"
#define REDIRECT_URI    @"&redirect_uri="
#define ERROR_MSG       @"clientID or redirectUrl is empty"

#define kTokenRegEx     @"access_token=(.*?)&"
#define kToken          @"access_token"

#define kBackBarTag     1223
#define kWeiboViewTag   1224

@implementation CDVWeibo

@synthesize redirectUrl;
@synthesize callbackId;

- (void)login:(CDVInvokedUrlCommand*)command
{
    NSString* clinetID = command.arguments[0];
    NSString* redirector = command.arguments[1];
    if ([clinetID isKindOfClass:[NSNull class]] || [redirector isKindOfClass:[NSNull class]]
        || 0 == [clinetID length] || 0 == [redirector length])
    {
        NSLog(ERROR_MSG);
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:ERROR_MSG];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        return;
    }

    NSString *url = [NSString stringWithFormat:@"%@%@%@%@%@%@",HEAD_URL,CLIENT_ID,clinetID,RESPONSE_TYPE,REDIRECT_URI,redirector];
    self.redirectUrl = redirector;
    self.callbackId = command.callbackId;

    [self showWeiboLogin:url];
}

- (void)showWeiboLogin:(NSString*)weiboUrl
{
    CGRect barFrame = self.webView.frame;
    barFrame.size.height = 45;
    UINavigationBar* bar = [[UINavigationBar alloc] initWithFrame:barFrame];
    bar.tag = kBackBarTag;
    UINavigationItem* item = [[UINavigationItem alloc] initWithTitle:@"新浪微博"];
    UIBarButtonItem* back = [[UIBarButtonItem alloc] initWithBarButtonSystemItem: UIBarButtonSystemItemCancel target:self action:@selector(cancel:)];
    [item setLeftBarButtonItem:back];
    [bar setItems:@[item]];

    CGRect weiboFrame = self.webView.frame;
    weiboFrame.origin.y = 45;
    UIWebView *weiboView = [[UIWebView alloc] initWithFrame:weiboFrame];
    weiboView.delegate = self;
    weiboView.scalesPageToFit = YES;
    weiboView.tag = kWeiboViewTag;
    weiboView.scalesPageToFit = YES;

    NSURL *requestUrl = [NSURL URLWithString:weiboUrl];
    NSURLRequest *request = [NSURLRequest requestWithURL:requestUrl];
    [weiboView loadRequest:request];
    [self.webView addSubview:weiboView];
    [self.webView addSubview:bar];
}

- (void)removeSubview
{
    for (UIView *v in [self.webView subviews]) {
        if (v.tag == kBackBarTag || v.tag == kWeiboViewTag) {
            [v removeFromSuperview];
        }
    }
}

-(void)cancel:(id)sender
{
    [self removeSubview];
}

- (NSString *)token:(NSString *)stringUrl
{
    NSRange range = [stringUrl rangeOfString:kTokenRegEx options:NSCaseInsensitiveSearch | NSRegularExpressionSearch];
    range.location += [kToken length] + 1;
    range.length -= [kToken length] + 2;

    NSString* tokenCode = range.length <= 0 ? nil : [stringUrl substringWithRange:range];
    return tokenCode;
}

#pragma mark UIWebViewDelegate

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    NSURL *requestUrl = [request URL];
    NSString* stringUrl = [requestUrl absoluteString];
    if ([stringUrl hasPrefix:self.redirectUrl])
    {
        CDVPluginResult* pluginResult = nil;

        NSString* token = [self token:stringUrl];
        if (token.length == 0)
        {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"can't get tokenCode"];
        }
        else
        {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{kToken:token}];
        }

        [self.commandDelegate sendPluginResult:pluginResult callbackId:self.callbackId];

        return NO;

        [self removeSubview];
    }
    return YES;
}

- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error
{
    if (kCFURLErrorCancelled == error.code) {
        //当频繁点击注册按钮,UIWebView会取消前一次请求再发起一次
        //在这里cancel事件并不是一个错误
        return;
    }

    NSLog(@"get sina weibo token request failed: %@", error);

    [self removeSubview];
}

@end
