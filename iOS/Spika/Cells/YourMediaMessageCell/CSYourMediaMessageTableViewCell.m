//
//  YourMediaMessageTableViewCell.m
//  Prototype
//
//  Created by Ivo Peric on 28/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSYourMediaMessageTableViewCell.h"
#import "CSConfig.h"
#import "CSUtils.h"

@implementation CSYourMediaMessageTableViewCell
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
    
    if ([message.type intValue] == kAppLocationMessageType) {
        self.nameOfFile.text = message.message;
        self.size.text = @"Show location on map.";
        self.download.text = @"";
    }
    else if ([message.type intValue] == kAppContactMessageType) {
        NSArray *subStrings = [message.message componentsSeparatedByCharactersInSet:[NSCharacterSet characterSetWithCharactersInString:@"\n"]];
        for (NSString *subString in subStrings) {
            if ([subString hasPrefix:@"FN"]) {
                self.nameOfFile.text = [subString substringFromIndex:3];
            }
            else {
                self.nameOfFile.text = @"no name";
            }
        }
        self.size.text = @"";
        self.download.text = @"";
    }
    else {
        self.nameOfFile.text = message.file.file.name;
        self.size.text = [CSUtils readableFileSize:message.file.file.size];
        self.download.text = @"Download";
    }
    
    self.nameOfFile.numberOfLines = 1;
    self.nameOfFile.textColor = kAppMessageFontColor;
    
    self.size.numberOfLines = 1;
    self.size.textColor = kAppMessageFontColor;
    
    self.download.numberOfLines = 1;
    self.download.textColor = kAppMessageFontColor;
        
    if ([message.type intValue] == kAppLocationMessageType){
        self.labelImage.image = [UIImage imageNamed:@"location_color"];
    }
    else if ([CSUtils isMessageAVideo:message]){
        self.labelImage.image = [UIImage imageNamed:@"video_color"];
    }
    else if([CSUtils isMessageAAudio:message]){
        self.labelImage.image = [UIImage imageNamed:@"audio_color"];
    }
    else if ([message.type intValue] == kAppContactMessageType) {
        self.labelImage.image = [UIImage imageNamed:@"contact_color"];
    }
    else {
        self.labelImage.image = [UIImage imageNamed:@"file_color"];
    }
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"H:mm"];
    NSDate* date = [NSDate dateWithTimeIntervalSince1970:[message.created longLongValue] / 1000];
    NSString* dateCreated = [NSString stringWithFormat:@"%@", [formatter stringFromDate:date]];
    
    self.timeLabel.text = [NSString stringWithFormat:@"%@", dateCreated];
    
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
        [menuController setMenuItems:[NSArray arrayWithObjects:it1, nil]];
        
        [menuController setTargetRect:self.labelImage.frame inView:self.labelImage];
        [menuController setMenuVisible:YES animated:YES];
    }
}

@end
