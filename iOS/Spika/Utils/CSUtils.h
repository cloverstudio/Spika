//
//  Utils.h
//  Prototype
//
//  Created by Ivo Peric on 27/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreGraphics/CoreGraphics.h>
#import <UIKit/UIKit.h>
#import "CSMessageModel.h"

@interface CSUtils : NSObject

+(CGFloat)getWidthForLabel:(UILabel*)label withFrameSizeWidth: (CGFloat) maxWidth;
+(CGFloat)getWidthOneLineForLabel:(UILabel*)label withFrameSizeWidth: (CGFloat) maxWidth;
+(NSString*)generateDownloadURLFormFileId: (NSString*) fileId;
+(BOOL)isMessageAnImage: (CSMessageModel*)message;
+(BOOL)isMessageAVideo:(CSMessageModel *)message;
+(BOOL)isMessageAAudio:(CSMessageModel *)message;
+(NSArray*)generateUnSeenMessageIdsFrom: (NSArray *) messages andActiveUser: (CSUserModel*) user;
+(NSString*) readableFileSize: (NSString*) size;
+(NSString*) getFileFromFileModel: (CSFileModel*) model;
+(BOOL) isFileExistsWithFileName: (NSString*) file;
+(UIImage*) resizeImage : (UIImage*) image toSize: (CGSize) newSize;

@end
