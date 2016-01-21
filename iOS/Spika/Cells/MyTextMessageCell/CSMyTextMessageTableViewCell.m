//
//  MyTextMessageTableViewCell.m
//  Prototype
//
//  Created by Ivo Peric on 25/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSMyTextMessageTableViewCell.h"
#import "CSConfig.h"
#import "CSUtils.h"
#import "UIAlertView+Blocks.h"

@implementation CSMyTextMessageTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

-(void) setMessage:(CSMessageModel *)message{

    [super setMessage:message];
    
    self.myBackgroundView.layer.cornerRadius = 8;
    self.myBackgroundView.layer.masksToBounds = YES;
    self.myBackgroundView.backgroundColor = kAppBubbleRightColor;
    
    if([message.deleted longLongValue] > 0){
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        [formatter setDateFormat:@"yyyy/MM/dd HH:mm:ss"];
        NSDate* date = [NSDate dateWithTimeIntervalSince1970:[message.deleted longLongValue] / 1000];
        NSString* dateDeleted = [NSString stringWithFormat:@"%@", [formatter stringFromDate:date]];
        self.myTextMessage.text = [NSString stringWithFormat:@"Message deleted at %@", dateDeleted];
    }else{
        self.myTextMessage.text = message.message;
    }
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"H:mm"];
    NSDate* date = [NSDate dateWithTimeIntervalSince1970:[message.created longLongValue] / 1000];
    NSString* dateCreated = [NSString stringWithFormat:@"%@", [formatter stringFromDate:date]];
    
    NSString *messageStatus;
    
    if (message.seenBy.count > 0) {
        messageStatus = @"Seen";
        self.timeLabel.text = [NSString stringWithFormat:@"%@, %@", messageStatus, dateCreated];
    }
    else if (message.status == kAppMessageStatusSent){
        messageStatus = @"Sending...";
        self.timeLabel.text = [NSString stringWithFormat:@"%@", messageStatus];
    }
    else {
        messageStatus = @"Sent";
        self.timeLabel.text = [NSString stringWithFormat:@"%@, %@", messageStatus, dateCreated];
    }
    
    self.myTextMessage.textColor = [UIColor whiteColor];
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
        UIMenuItem *it3 = [[UIMenuItem alloc] initWithTitle:@"Delete" action:@selector(handleDelete:)];
        [menuController setMenuItems:[NSArray arrayWithObjects:it1, it2, it3, nil]];
        
        [menuController setTargetRect:self.myTextMessage.frame inView:self.myTextMessage];
        [menuController setMenuVisible:YES animated:YES];
    }
}

- (void) handleCopy:(id)sender {
    UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
    pasteboard.string = self.message.message;
}

- (void) handleDelete:(id)sender {
    [UIAlertView showWithTitle:@"Delete"
                       message:@"Are You sure?"
             cancelButtonTitle:@"Cancel"
             otherButtonTitles:@[@"OK"]
                      tapBlock:^(UIAlertView * _Nonnull alertView, NSInteger buttonIndex) {
                          if ([[alertView buttonTitleAtIndex:buttonIndex] isEqualToString:@"OK"]) {
                              [[NSNotificationCenter defaultCenter] postNotificationName:kAppDeleteMessageNotification
                                                                                  object:nil
                                                                                userInfo:[NSDictionary dictionaryWithObject:self.message forKey:paramMessage]];
                          }
                      }
     ];
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
    else if (action == @selector(handleDelete:)) {
        return YES;
    }
    return NO;
}

@end
