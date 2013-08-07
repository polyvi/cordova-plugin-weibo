//
//  CDVWeibo.h
//  CordovaLib
//
//  Created by YTB on 13-5-21.
//
//

#import <Cordova/CDVPlugin.h>

@interface CDVWeibo : CDVPlugin <UIWebViewDelegate>

//请求新浪微博授权的重定向连接
@property (nonatomic, copy) NSString* redirectUrl;
//当前请求的callbackId
@property (nonatomic, copy) NSString* callbackId;

- (void)login:(CDVInvokedUrlCommand*)command;

@end
