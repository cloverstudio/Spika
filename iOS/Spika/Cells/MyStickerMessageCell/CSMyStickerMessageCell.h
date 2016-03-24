//
//  CSMyStickerMessageCell.h
//  Spika
//
//  Created by mislav on 24/03/16.
//  Copyright © 2016 Clover Studio. All rights reserved.
//

#import "CSBaseTableViewCell.h"

@interface CSMyStickerMessageCell : CSBaseTableViewCell

@property (weak, nonatomic) IBOutlet UIView *backView;
@property (weak, nonatomic) IBOutlet UIImageView *image;
@property (weak, nonatomic) IBOutlet UIView *parentView;
@property (weak, nonatomic) IBOutlet CSAvatarView *avatar;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *loadingIndicator;
@property (weak, nonatomic) IBOutlet UIImageView *peak;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *nameConstraint;
@property (weak, nonatomic) IBOutlet UILabel *dateLabel;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *dateConstraint;

-(void) manageLoadingIndicatorToShow:(BOOL) toShow;

@end
