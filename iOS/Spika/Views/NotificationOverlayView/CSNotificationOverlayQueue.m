//
//  CSNotificationOverlayQueue.m
//  SpikaEnterprise
//
//  Created by Josip Bernat on 17/10/14.
//  Copyright (c) 2014 Clover-Studio. All rights reserved.
//

#import "CSNotificationOverlayQueue.h"
#import "CSNotificationOverlayView.h"

@interface CSNotificationOverlayQueue ()

@property (nonatomic, strong) NSMutableArray *overlaysQueue;
@property (strong, nonatomic) UIWindow *foregroundWindow;
@property (nonatomic, strong) NSTimer *timer;
@property (nonatomic, strong) CSNotificationOverlayView *visibleView;

@end

@implementation CSNotificationOverlayQueue

#pragma mark - Shared Instance

+ (instancetype)sharedQueue {

    static CSNotificationOverlayQueue *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    return instance;
}

#pragma mark - Initialization

- (id)init {

    if (self = [super init]) {
        
        _overlaysQueue = [[NSMutableArray alloc] init];
    
        _foregroundWindow = [[UIWindow alloc] initWithFrame:CGRectMake(0, 0,
                                                                       CGRectGetWidth([[UIScreen mainScreen] bounds]),
                                                                       CSNotificationOverlayViewHeight)];
        _foregroundWindow.backgroundColor = [UIColor clearColor];
        _foregroundWindow.windowLevel = UIWindowLevelStatusBar;
        _foregroundWindow.hidden = YES;
    }
    return self;
}

#pragma mark - Enqueueing Views

- (void)enqueueOverlayView:(CSNotificationOverlayView *)view {

    NSParameterAssert(view);
    
    if ([self __queueContainsOverlayView:view]) {
        return;
    }
    
    [_overlaysQueue addObject:view];
    
    [self __refreshVisibleOverlay];
}

- (BOOL)__queueContainsOverlayView:(CSNotificationOverlayView *)view {
    
    NSParameterAssert(view);
    
    __block BOOL contains = NO;
    [_overlaysQueue enumerateObjectsUsingBlock:^(id obj, NSUInteger idx, BOOL *stop) {
       
        NSAssert([obj isKindOfClass:[CSNotificationOverlayView class]], @"obj must be CSNotificationOverlayView class");
        CSNotificationOverlayView *overlay = (CSNotificationOverlayView *)obj;
        if ([[overlay identifier] isEqualToString:[view identifier]]) {
            contains = YES;
            *stop = YES;
        }
    }];
    
    return contains;
}

- (void)__refreshVisibleOverlay {

    if (self.visibleView || !_overlaysQueue.count) { return; }
    
    CSNotificationOverlayView *view = _overlaysQueue[0];
    if (![view superview]) {
        [_foregroundWindow addSubview:view];
        [_foregroundWindow setHidden:NO];
    }
    
    self.visibleView = view;
    
    if ([view.identifier isEqualToString:@"message_from_chat"]) {
        view.frame = CGRectMake(0.0f, 0.0f,
                                CGRectGetWidth(_foregroundWindow.frame),
                                CSNotificationOverlayViewHeight);
        view.alpha = 0;
        
        _foregroundWindow.frame = CGRectMake(0,
                                             _visibleView.verticalPosition - CSNotificationOverlayViewHeight,
                                            CGRectGetWidth([[UIScreen mainScreen] bounds]),
                                            CSNotificationOverlayViewHeight);
    }
    else {
        _foregroundWindow.frame = CGRectMake(0,
                                             0,
                                             CGRectGetWidth([[UIScreen mainScreen] bounds]),
                                             CSNotificationOverlayViewHeight);
    }
    
    
    __weak id this = self;
    [UIView animateWithDuration:0.25
                          delay:0.0
                        options:UIViewAnimationOptionCurveEaseInOut
                     animations:^{
                         if ([view.identifier isEqualToString:@"message_from_chat"]) {
                             view.frame = CGRectMake(0.0f, 0.0f,
                                                     CGRectGetWidth(_foregroundWindow.frame),
                                                     CSNotificationOverlayViewHeight);
                             view.alpha = 1;
                         }
                         else {
                             view.frame = CGRectMake(0.0f, 0.0f,
                                                     CGRectGetWidth(_foregroundWindow.frame),
                                                     CSNotificationOverlayViewHeight);

                         }
                         
                     } completion:^(BOOL finished) {

                         __strong typeof(self) strongThis = this;
                         NSTimer *timer = [NSTimer scheduledTimerWithTimeInterval:5.0f
                                                                           target:strongThis
                                                                         selector:@selector(onTimer:)
                                                                         userInfo:nil
                                                                          repeats:NO];
                         strongThis.timer = timer;
                     }];

}

- (void)__dismissVisibleOverlay:(BOOL)canceled
                     completion:(void (^)())completion {

    // We want to dismiss only views which are not selected.
    if ([self.visibleView isSelected] && !canceled) { return; }
    
    [self.timer invalidate];
    self.timer = nil;
    
    __weak id this = self;
    [UIView animateWithDuration:0.25
                          delay:0.0
                        options:UIViewAnimationOptionCurveEaseInOut
                     animations:^{
                         
                         __strong typeof(self) strongThis = this;
                         
                         if ([_visibleView.identifier isEqualToString:@"message_from_chat"]) {
                             strongThis.visibleView.frame = CGRectMake(0,
                                                                       0,
                                                                       CGRectGetWidth(strongThis.foregroundWindow.frame),
                                                                       CSNotificationOverlayViewHeight);
                             _visibleView.alpha = 0;
                         }
                         else {
                             strongThis.visibleView.frame = CGRectMake(0,
                                                                       -CSNotificationOverlayViewHeight,
                                                                       CGRectGetWidth(strongThis.foregroundWindow.frame),
                                                                       CSNotificationOverlayViewHeight);

                         }
                     } completion:^(BOOL finished) {
                         
                         __strong typeof(self) strongThis = this;
                         [strongThis.visibleView overlayDidDismiss:canceled];
                         [strongThis.overlaysQueue removeObject:strongThis.visibleView];
                         [strongThis.visibleView removeFromSuperview];
                         strongThis.visibleView = nil;
                         
                         if (completion) {
                             completion();
                         }
                         
                         if (!strongThis.overlaysQueue.count) {
                             strongThis.foregroundWindow.hidden = YES;
                         }
                         else {
                             [strongThis __refreshVisibleOverlay];
                         }
                         
                     }];
}

- (void)cancelOverlayWithIdentifier:(NSString *)identifier {
    
    NSParameterAssert(identifier);
    
    __weak id this = self;
    void(^completion)() = ^(){
    
        __strong typeof(self) strongThis = this;
        for (int i=0; i<strongThis.overlaysQueue.count; i++) {
            
            id object = strongThis.overlaysQueue[i];
            NSAssert([object isKindOfClass:[CSNotificationOverlayView class]], @"object must be CSNotificationOverlayView class");
            
            CSNotificationOverlayView *view = (CSNotificationOverlayView *)object;
            if ([view.identifier isEqualToString:identifier]) {
                [strongThis.overlaysQueue removeObjectAtIndex:i];
                i--;
            }
        }
    };
    
    [self __dismissVisibleOverlay:YES
                       completion:completion];
}

#pragma mark - Timer Selector

- (void)onTimer:(id)sender {
    
    [self.timer invalidate];
    self.timer = nil;
    
    self.visibleView.previewTimeExpired = YES;
    
    [self __dismissVisibleOverlay:NO
                       completion:nil];
}

@end
