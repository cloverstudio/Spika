//
//  UserInfoTableViewCell.m
//  Prototype
//
//  Created by Ivo Peric on 25/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSUserInfoTableViewCell.h"
#import "CSConfig.h"

@implementation CSUserInfoTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

-(void)setMessage:(CSMessageModel *)message{

    [super setMessage:message];
    
    self.userInfoMessage.numberOfLines = 0;
    self.userInfoMessage.textColor = kAppInfoUserMessageColor(1);
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"dd/MM/yyyy HH:mm:ss"];
    NSDate* date = [NSDate dateWithTimeIntervalSince1970:[message.created longLongValue] / 1000];
    NSString* dateCreated = [NSString stringWithFormat:@"%@", [formatter stringFromDate:date]];
    
    NSString* textMessage = @"";
    if([message.type integerValue] == kAppNewUserMessageType){
        textMessage = [NSString stringWithFormat:@"%@\n%@ joined to conversation.", dateCreated, message.user.name];
    }else if ([message.type integerValue] == kAppLeaveUserMessageType){
        textMessage = [NSString stringWithFormat:@"%@\n%@ left conversation.", dateCreated, message.user.name];
    }
    
    self.userInfoMessage.text = textMessage;
    
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

@end
