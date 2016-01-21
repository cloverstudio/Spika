//
//  UserInfoTableViewCell.h
//  Prototype
//
//  Created by Ivo Peric on 25/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CSBaseTableViewCell.h"

@interface CSUserInfoTableViewCell : CSBaseTableViewCell

@property (weak, nonatomic) IBOutlet UILabel *userInfoMessage;
@property (weak, nonatomic) IBOutlet UIView *parentView;
@property (weak, nonatomic) IBOutlet UIView *userInfoBackground;
@property (weak, nonatomic) IBOutlet UILabel *dateLabel;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *dateConstraint;

@end
