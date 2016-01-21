//
//  UploadManager.h
//  Prototype
//
//  Created by Ivo Peric on 02/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <AFNetworking/AFNetworking.h>

typedef void(^fileUploadFinished)(id responseObject);

@interface CSUploadManager : NSObject

-(void) uploadImage: (UIImage*) imageToPost fileName:(NSString*) fileName mimeType:(NSString*) mimeType viewForLoading: (UIView*) parentView completition:(fileUploadFinished) finished;
-(void) uploadFile: (NSData*) data fileName:(NSString*) fileName mimeType:(NSString*) mimeType viewForLoading: (UIView*) parentView completition:(fileUploadFinished) finished;

@end
