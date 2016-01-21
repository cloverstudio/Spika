//
//  YourImageMessageTableViewCell.m
//  Prototype
//
//  Created by Ivo Peric on 28/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSYourImageMessageTableViewCell.h"
#import "CSConfig.h"
#import "CSUtils.h"

@implementation CSYourImageMessageTableViewCell
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
    self.backView.backgroundColor = kAppBubbleLeftColor;
        
    [self.loadingIndicator startAnimating];
        
    [self manageLoadingIndicatorToShow:NO];

    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"H:mm"];
    NSDate* date = [NSDate dateWithTimeIntervalSince1970:[message.created longLongValue] / 1000];
    NSString* dateCreated = [NSString stringWithFormat:@"%@", [formatter stringFromDate:date]];
    
    self.timeLabel.text = [NSString stringWithFormat:@"%@", dateCreated];
    
    self.timeLabel.textColor = kAppMessageFontColor;
    
    self.image.layer.cornerRadius = 8;
    self.image.layer.masksToBounds = YES;
    [self.image sd_setImageWithURL:[NSURL URLWithString:[CSUtils generateDownloadURLFormFileId:message.file.thumb.id]]];
    
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
        [menuController setMenuItems:[NSArray arrayWithObjects:it1, nil]];
        
        [menuController setTargetRect:self.image.frame inView:self.image];
        [menuController setMenuVisible:YES animated:YES];
    }
}
@end
