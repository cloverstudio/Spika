//
//  BaseTableViewCell.h
//  Prototype
//
//  Created by Ivo Peric on 31/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIImageView+WebCache.h"
#import "CSMessageModel.h"
#import "CSCellClickedDelegate.h"
#import "CSAvatarView.h"

@interface CSBaseTableViewCell : UITableViewCell

@property (nonatomic, strong) CSMessageModel* message;
@property (nonatomic, weak) id <CSCellClickedDelegate> delegate;
@property (nonatomic) BOOL shouldShowAvatar;
@property (nonatomic) BOOL shouldShowName;
@property (nonatomic) BOOL shouldShowDate;

@property (nonatomic,strong) UILongPressGestureRecognizer *lpgr;
- (void)handleLongPressGestures:(UILongPressGestureRecognizer *)sender;
- (void)handleDetails:(id)sender;

@end
