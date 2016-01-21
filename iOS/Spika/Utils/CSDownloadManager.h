//
//  DownloadManager.h
//  Prototype
//
//  Created by Ivo Peric on 01/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

typedef void(^fileDownloadFinished)(BOOL success);

@interface CSDownloadManager : NSObject

-(void) downloadFileWithUrl:(NSURL*) url destination: (NSURL*) destination viewForLoading:(UIView*) parentView completition: (fileDownloadFinished) finished;

@end
