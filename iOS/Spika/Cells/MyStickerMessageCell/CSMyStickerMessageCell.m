//
//  CSMyStickerMessageCell.m
//  Spika
//
//  Created by mislav on 24/03/16.
//  Copyright © 2016 Clover Studio. All rights reserved.
//

#import "CSMyStickerMessageCell.h"
#import "CSConfig.h"
#import "CSUtils.h"
#import "UIAlertView+Blocks.h"

@implementation CSMyStickerMessageCell
@synthesize delegate;

- (void)awakeFromNib {
    [super awakeFromNib];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    
    // Configure the view for the selected state
}

-(void)setMessage:(CSMessageModel *)message{
    
    [super setMessage:message];
    
    self.backView.layer.cornerRadius = 8;
    self.backView.layer.masksToBounds = YES;
    self.backView.backgroundColor = kAppBubbleRightColor;
    
    [self.loadingIndicator startAnimating];
    
    [self manageLoadingIndicatorToShow:NO];
    
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
    
    self.timeLabel.textColor = kAppMessageFontColor;
    
    self.image.layer.cornerRadius = 8;
    self.image.layer.masksToBounds = YES;
    
    [self.image sd_setImageWithURL:[NSURL URLWithString:message.message]];
    
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

-(void)manageLoadingIndicatorToShow:(BOOL)toShow{
    if(toShow){
        [self.loadingIndicator startAnimating];
        self.loadingIndicator.hidden = NO;
        
    }else{
        [self.loadingIndicator stopAnimating];
        self.loadingIndicator.hidden = YES;
    }
}

- (void)handleLongPressGestures:(UILongPressGestureRecognizer *)sender {
    if (sender.state == UIGestureRecognizerStateBegan) {
        [sender.view becomeFirstResponder];
        UIMenuController *menuController = [UIMenuController sharedMenuController];
        UIMenuItem *it1 = [[UIMenuItem alloc] initWithTitle:@"Details" action:@selector(handleDetails:)];
        UIMenuItem *it2 = [[UIMenuItem alloc] initWithTitle:@"Delete" action:@selector(handleDelete:)];
        [menuController setMenuItems:[NSArray arrayWithObjects:it1, it2, nil]];
        
        [menuController setTargetRect:self.image.frame inView:self.image];
        [menuController setMenuVisible:YES animated:YES];
    }
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
    else if (action == @selector(handleDelete:)) {
        return YES;
    }
    return NO;
}

@end
