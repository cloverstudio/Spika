//
//  DownloadManager.m
//  Prototype
//
//  Created by Ivo Peric on 01/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSDownloadManager.h"
#import <AFNetworking/AFNetworking.h>
#import "UIKit+AFNetworking.h"
#import "CSProgressLoadingView.h"

@interface CSDownloadManager()

@property CSProgressLoadingView* progressView;

@end

@implementation CSDownloadManager

-(void) downloadFileWithUrl:(NSURL*) url destination: (NSURL*) destination viewForLoading:(UIView*) parentView completition:(fileDownloadFinished)finished{
    
    if(parentView){
        _progressView = [[CSProgressLoadingView alloc] init];
        [parentView addSubview:_progressView];
    }
    
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    AFURLConnectionOperation *operation =   [[AFHTTPRequestOperation alloc] initWithRequest:request];
    
    operation.outputStream = [NSOutputStream outputStreamToFileAtPath:destination.path append:NO];
    
    [operation setDownloadProgressBlock:^(NSUInteger bytesRead, long long totalBytesRead, long long totalBytesExpectedToRead) {
        
        if(parentView){
            [_progressView changeProgressLabelOnMainThread:[NSString stringWithFormat:@"%lld", totalBytesRead] max:[NSString stringWithFormat:@"%lld", totalBytesExpectedToRead]];
            [_progressView changeProgressViewOnMainThread:[NSString stringWithFormat:@"%f", (float)((float)totalBytesRead / (float)totalBytesExpectedToRead)]];
        }
        
    }];
    
    [operation setCompletionBlock:^{
        if(parentView){
            [_progressView removeFromSuperview];
        }
        finished(YES);
        
    }];
    [operation start];
    
}

@end
