//
//  SeenByTableViewCell.m
//  Prototype
//
//  Created by Ivo Peric on 31/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSSeenByTableViewCell.h"
#import "UIImageView+WebCache.h"

@implementation CSSeenByTableViewCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

-(void)setSeenBy:(CSSeenByModel *)seenBy{

    _seenBy = seenBy;
    
    float widthWindow = self.window.frame.size.width;
    float widthName = 32.f / 100.f * widthWindow;  //32% of cell
    float widthTime = 53.f / 100.f * widthWindow;  //53% of cell

    self.nameWidth.constant = widthName;
    self.name.text = seenBy.user.name;
    
    self.timeWidth.constant = widthTime;
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"yyyy/MM/dd HH:mm:ss"];
    NSDate* date = [NSDate dateWithTimeIntervalSince1970:[seenBy.at longLongValue] / 1000];
    NSString* dateSeen = [NSString stringWithFormat:@"%@", [formatter stringFromDate:date]];
    self.time.text = dateSeen;
    
    self.avatar.layer.cornerRadius = self.avatar.frame.size.width / 2;
    self.avatar.layer.masksToBounds = YES;
    [self.avatar.layer setBorderColor: [[UIColor whiteColor] CGColor]];
    [self.avatar.layer setBorderWidth: 1.0];
    
    [self.avatar sd_setImageWithURL:[NSURL URLWithString:_seenBy.user.avatarURL]];
}

@end
