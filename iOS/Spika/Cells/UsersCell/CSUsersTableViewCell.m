//
//  UsersTableViewCell.m
//  Prototype
//
//  Created by Ivo Peric on 27/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSUsersTableViewCell.h"
#import "CSConfig.h"
#import "UIImageView+WebCache.h"

@implementation CSUsersTableViewCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

-(void)setUser:(CSUserModel *)user{

    _user = user;
    self.name.text = user.name;
    
    self.parentView.backgroundColor = kAppGrayLight(1);
    
    self.name.numberOfLines = 1;
    self.background.layer.cornerRadius = 10;
    self.background.layer.masksToBounds = YES;
    self.avatar.layer.cornerRadius = self.avatar.frame.size.width / 2;
    self.avatar.layer.masksToBounds = YES;
    [self.avatar.layer setBorderColor: [kAppDefaultColor(1) CGColor]];
    [self.avatar.layer setBorderWidth: 1.0];
    
    [self.avatar sd_setImageWithURL:[NSURL URLWithString:user.avatarURL]];
}

@end
