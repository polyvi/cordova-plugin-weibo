//
//  CDVWeibo.m
//  CordovaLib
//
//  Created by YTB on 13-5-21.
//
//

#import "CDVWeibo.h"

#define HEAD_URL        @"https://api.weibo.com/oauth2/authorize?display=mobile"
#define CLIENT_ID       @"&client_id="
#define RESPONSE_TYPE   @"&response_type=code"
#define REDIRECT_URI    @"&redirect_uri="
#define ERROR_MSG       @"clientID or redirectUrl is empty"
#define CODE            @"/?code="

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
    CGRect weiboFrame = self.webView.frame;
    weiboFrame.origin.y = 0;
    UIWebView *weiboView = [[UIWebView alloc] initWithFrame:weiboFrame];
    weiboView.delegate = self;
    weiboView.scalesPageToFit = YES;
    NSURL *requestUrl = [NSURL URLWithString:weiboUrl];
    NSURLRequest *request = [NSURLRequest requestWithURL:requestUrl];
    [weiboView loadRequest:request];
    [self.webView addSubview:weiboView];
}

#pragma mark UIWebViewDelegate

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    NSURL *requestUrl = [request URL];
    NSString* stringUrl = [requestUrl absoluteString];
    if ([stringUrl hasPrefix:self.redirectUrl])
    {
        CDVPluginResult* pluginResult = nil;
        if (NSNotFound == [stringUrl rangeOfString:CODE].location)
        {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"can't get tokenCode"];
        }
        else
        {
            NSString* tokenCode = [stringUrl substringFromIndex:([stringUrl rangeOfString:@"="].location + 1)];
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:tokenCode];
        }

        [self.commandDelegate sendPluginResult:pluginResult callbackId:self.callbackId];

        [webView removeFromSuperview];
        return NO;
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

    NSLog(@"get sina weibo token request failed %d", error.code);
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"get sina weibo token request failed"];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.callbackId];

    [webView removeFromSuperview];
}


@end
