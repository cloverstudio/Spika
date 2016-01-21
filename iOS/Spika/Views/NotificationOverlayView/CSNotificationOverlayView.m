//
//  CSNotificationOverlayView.m
//  SpikaEnterprise
//
//  Created by Josip Bernat on 15/07/14.
//  Copyright (c) 2014 Clover-Studio. All rights reserved.
//

#import "CSNotificationOverlayView.h"
#import "CSNotificationOverlayQueue.h"
#import "CSConfig.h"

CGFloat const CSNotificationOverlayViewHeight               = 68.0f;

@interface CSNotificationOverlayView ()

@property (nonatomic, strong) NSString *message;
@property (nonatomic, readwrite) BOOL wasAlreadyTapped;

@property (weak, nonatomic) IBOutlet UIView *contentView;
@property (weak, nonatomic) IBOutlet UILabel *messageLabel;
@property (weak, nonatomic) IBOutlet UIButton *contentButton;
@property (weak, nonatomic) IBOutlet UIImageView *iconImageView;

#pragma mark - Button Selectors
- (IBAction)onContent:(id)sender;
- (IBAction)onContentCancel:(id)sender;

@end

@implementation CSNotificationOverlayView

#pragma mark - Initialization

+ (instancetype)notificationOverlayWithMessage:(NSString *)message
                                    identifier:(NSString *)identifier
                                      delegate:(id<CSNotificationOverlayViewDelegate>)delegate {
    
    NSParameterAssert(message);
    NSParameterAssert(identifier);
    
    CSNotificationOverlayView *view = [[self alloc] initWithFrame:CGRectMake(0, -CSNotificationOverlayViewHeight,
                                                                             CGRectGetWidth([[UIScreen mainScreen] bounds]),
                                                                             CSNotificationOverlayViewHeight)];
    view.message = message;
    view.identifier = identifier;
    view.delegate = delegate;
    
    [[CSNotificationOverlayQueue sharedQueue] enqueueOverlayView:view];
    
    return view;
}

+ (instancetype)notificationOverlayWithMessage:(NSString *)message
                                    identifier:(NSString *)identifier
                                      callback:(CSNotificationOverlayCallback)block {

    NSParameterAssert(message);
    NSParameterAssert(identifier);
    
    CSNotificationOverlayView *view = [[self alloc] initWithFrame:CGRectMake(0, -CSNotificationOverlayViewHeight,
                                                                             CGRectGetWidth([[UIScreen mainScreen] bounds]),
                                                                             CSNotificationOverlayViewHeight)];
    view.message = message;
    view.identifier = identifier;
    view.callbackHandler = block;
    
    [[CSNotificationOverlayQueue sharedQueue] enqueueOverlayView:view];
    
    return view;
}

+ (instancetype)notificationOverlayFromChatWithMessage:(NSString *)message
                                      verticalPosition:(NSInteger)verticalPosition
                                              callback:(CSNotificationOverlayCallback)block {
    
    NSParameterAssert(message);
    
    CSNotificationOverlayView *view = [[self alloc] initWithFrame:CGRectMake(0, 0,
                                                                             CGRectGetWidth([[UIScreen mainScreen] bounds]),
                                                                             CSNotificationOverlayViewHeight)];
    view.message = message;
    view.identifier = @"message_from_chat";
    view.callbackHandler = block;
    view.verticalPosition = verticalPosition;
    
    [[CSNotificationOverlayQueue sharedQueue] enqueueOverlayView:view];
    
    return view;
}

- (id)initWithCoder:(NSCoder *)aDecoder {
    
    if (self = [super initWithCoder:aDecoder]) {
        [self initialize];
    }
    return self;
}

- (id)initWithFrame:(CGRect)frame {
    
    if (self = [super initWithFrame:frame]) {
        [self initialize];
    }
    return self;
}

- (void)initialize {
    
    NSArray *array = [[NSBundle mainBundle] loadNibNamed:NSStringFromClass([self class])
                                                   owner:self
                                                 options:nil];
    [self addSubview:array[0]];
    
    self.messageLabel.text = self.message;
    
    self.contentButton.backgroundColor = kAppBlackColor(0.5f);
    self.contentView.autoresizingMask = (UIViewAutoresizingFlexibleWidth |
                                         UIViewAutoresizingFlexibleHeight);
}

#pragma mark - Apperance

- (void)awakeFromNib {
    
    [super awakeFromNib];
    
    self.messageLabel.text = self.message;
    
    self.contentView.autoresizingMask = (UIViewAutoresizingFlexibleWidth |
                                         UIViewAutoresizingFlexibleHeight);
}

- (void)didMoveToSuperview {
    
    [super didMoveToSuperview];
    
    _messageLabel.text = self.message;
    
//    self.messageLabel.hidden = YES;
//    self.iconImageView.hidden = YES;
}

- (void)layoutSubviews {

    [super layoutSubviews];
    _contentView.frame = self.bounds;
}

- (void)setFrame:(CGRect)frame {

    [super setFrame:frame];
    _contentView.frame = frame;
}

#pragma mark - Button Selectors

- (IBAction)onContent:(id)sender {
    
    if (self.wasAlreadyTapped) {return;}
    self.wasAlreadyTapped = YES;
    
    if (self.previewTimeExpired) {
        [[CSNotificationOverlayQueue sharedQueue] cancelOverlayWithIdentifier:[self identifier]];
        return;
    }
    
    if ([(NSObject *)_delegate respondsToSelector:@selector(notificationOverlayView:didDismissAsSelected:)]) {
        [_delegate notificationOverlayView:self didDismissAsSelected:YES];
    }
    
    if (self.callbackHandler) {
        self.callbackHandler(YES);
    }
    self.callbackHandler = nil;
    
    [[CSNotificationOverlayQueue sharedQueue] cancelOverlayWithIdentifier:[self identifier]];
}

- (IBAction)onContentCancel:(id)sender {

    if (self.wasAlreadyTapped) { return; }
    
    [[CSNotificationOverlayQueue sharedQueue] cancelOverlayWithIdentifier:[self identifier]];
}

#pragma mark - Getters

- (BOOL)isSelected {
    return _contentButton.isTouchInside;
}

#pragma mark - Apperance

- (void)overlayDidDismiss:(BOOL)canceled {

    if (!canceled) {
        if ([(NSObject *)_delegate respondsToSelector:@selector(notificationOverlayView:didDismissAsSelected:)]) {
            [_delegate notificationOverlayView:self didDismissAsSelected:NO];
        }
        
        if (self.callbackHandler) {
            self.callbackHandler(NO);
        }
        self.callbackHandler = nil;
    }
    self.delegate = nil;
}

@end
