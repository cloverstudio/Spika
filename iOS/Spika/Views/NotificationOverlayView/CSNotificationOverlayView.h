//
//  CSNotificationOverlayView.h
//  SpikaEnterprise
//
//  Created by Josip Bernat on 15/07/14.
//  Copyright (c) 2014 Clover-Studio. All rights reserved.
//

#import <UIKit/UIKit.h>

extern CGFloat const CSNotificationOverlayViewHeight;

@class CSNotificationOverlayView;

@protocol CSNotificationOverlayViewDelegate <NSObject>

- (void)notificationOverlayView:(CSNotificationOverlayView *)view
            didDismissAsSelected:(BOOL)wasSelected;

@end

typedef void (^CSNotificationOverlayCallback)(BOOL wasSelected);

@interface CSNotificationOverlayView : UIView

@property (weak, nonatomic) id<CSNotificationOverlayViewDelegate> delegate;
@property (copy, nonatomic) CSNotificationOverlayCallback callbackHandler;
@property (strong, nonatomic) NSString *identifier;
@property (readwrite, nonatomic, getter=isVisible) BOOL visible;
@property (readonly, nonatomic, getter=isSelected) BOOL selected;
@property (readwrite, nonatomic) BOOL previewTimeExpired;
@property (nonatomic) NSInteger verticalPosition;

#pragma mark - Initialization
+ (instancetype)notificationOverlayWithMessage:(NSString *)message
                                    identifier:(NSString *)identifier
                                      delegate:(id<CSNotificationOverlayViewDelegate>)delegate;

+ (instancetype)notificationOverlayWithMessage:(NSString *)message
                                    identifier:(NSString *)identifier
                                      callback:(CSNotificationOverlayCallback)block;

+ (instancetype)notificationOverlayFromChatWithMessage:(NSString *)message
                                      verticalPosition:(NSInteger)verticalPosition
                                              callback:(CSNotificationOverlayCallback)block;

#pragma mark - Apperance
- (void)overlayDidDismiss:(BOOL)canceled;

@end



