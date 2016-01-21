//
//  YourTextMessageTableViewCell.m
//  Prototype
//
//  Created by Ivo Peric on 26/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSYourTextMessageTableViewCell.h"
#import "CSConfig.h"
#import "CSUtils.h"

@interface CSYourTextMessageTableViewCell () <SDWebImageManagerDelegate>

@end

@implementation CSYourTextMessageTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

-(void)setMessage:(CSMessageModel *)message{
    
    [super setMessage:message];
    
    self.yourBackground.layer.cornerRadius = 8;
    self.yourBackground.layer.masksToBounds = YES;
    self.yourBackground.backgroundColor = kAppBubbleLeftColor;
            
    if([message.deleted longLongValue] > 0){
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        [formatter setDateFormat:@"yyyy/MM/dd HH:mm:ss"];
        NSDate* date = [NSDate dateWithTimeIntervalSince1970:[message.deleted longLongValue] / 1000];
        NSString* dateDeleted = [NSString stringWithFormat:@"%@", [formatter stringFromDate:date]];
        self.yourTextMessage.text = [NSString stringWithFormat:@"Message deleted at %@", dateDeleted];
    }else{
        self.yourTextMessage.text = message.message;
    }
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"H:mm"];
    NSDate* date = [NSDate dateWithTimeIntervalSince1970:[message.created longLongValue] / 1000];
    NSString* dateCreated = [NSString stringWithFormat:@"%@", [formatter stringFromDate:date]];
    
    self.timeLabel.text = [NSString stringWithFormat:@"%@", dateCreated];
    
    self.yourTextMessage.textColor = kAppMessageFontColor;
    self.timeLabel.textColor = kAppMessageFontColor;
    
    if (self.shouldShowAvatar) {
        [self.avatar setHidden:NO];
        [self.avatar setImageWithURL:[NSURL URLWithString:message.user.avatarURL]];
        [self.peak setHidden:NO];
    }
    else {
        [self.avatar setHidden:YES];
        [self.peak setHidden:YES];
    }
    
    if (self.shouldShowName) {
        self.nameLabel.text = message.user.name;
        self.nameConstraint.constant = 20.0f;
    }
    else {
        self.nameLabel.text = @"";
        self.nameConstraint.constant = 0.0f;
    }
    
    if (self.shouldShowDate) {
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        [formatter setDateFormat:@"d MMMM yyyy"];
        self.dateLabel.text = [NSString stringWithFormat:@" %@ ", [formatter stringFromDate:date]];
        self.dateConstraint.constant = 20.0f;
    }
    else {
        self.dateLabel.text = @"";
        self.dateConstraint.constant = 0.0f;
    }
}

- (void)handleLongPressGestures:(UILongPressGestureRecognizer *)sender {
    if (sender.state == UIGestureRecognizerStateBegan) {
        [sender.view becomeFirstResponder];
        UIMenuController *menuController = [UIMenuController sharedMenuController];
        UIMenuItem *it1 = [[UIMenuItem alloc] initWithTitle:@"Details" action:@selector(handleDetails:)];
        UIMenuItem *it2 = [[UIMenuItem alloc] initWithTitle:@"Copy" action:@selector(handleCopy:)];
        [menuController setMenuItems:[NSArray arrayWithObjects:it1, it2, nil]];
        
        [menuController setTargetRect:self.yourTextMessage.frame inView:self.yourTextMessage];
        [menuController setMenuVisible:YES animated:YES];
    }
}

- (void) handleCopy:(id)sender {
    UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
    pasteboard.string = self.message.message;
}

- (BOOL)canPerformAction:(SEL)action withSender:(id)sender
{
    if ([self.message.deleted longLongValue] > 0) {
        return NO;
    }
    if (action == @selector(handleDetails:)) {
        return YES;
    }
    else if (action == @selector(handleCopy:)) {
        return YES;
    }
    return NO;
}

@end
