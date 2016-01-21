//
//  CSNotificationOverlayQueue.h
//  SpikaEnterprise
//
//  Created by Josip Bernat on 17/10/14.
//  Copyright (c) 2014 Clover-Studio. All rights reserved.
//

#import <Foundation/Foundation.h>

@class CSNotificationOverlayView;

@interface CSNotificationOverlayQueue : NSObject

+ (instancetype)sharedQueue;

- (void)enqueueOverlayView:(CSNotificationOverlayView *)view;

- (void)cancelOverlayWithIdentifier:(NSString *)identifier;

@end
