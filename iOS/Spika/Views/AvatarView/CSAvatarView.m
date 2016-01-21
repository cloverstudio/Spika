//
//  CSAvatarView.m
//  Spika
//
//  Created by mislav on 10/12/15.
//  Copyright © 2015 Clover Studio. All rights reserved.
//

#import "CSAvatarView.h"
#import "CSAvatarLoader.h"
#import "CSConfig.h"

@interface CSAvatarView ()
@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@end

@implementation CSAvatarView

-(void)customInit {
    
}

-(void)layoutSubviews {
    self.avatar.frame = CGRectMake(1, 1, self.frame.size.width - 2, self.frame.size.height - 2);
    
    [super layoutSubviews];
    
    self.layer.backgroundColor = [[UIColor whiteColor] CGColor];
    self.layer.cornerRadius = self.frame.size.width / 2;
    self.layer.masksToBounds = YES;
    
    self.avatar.layer.cornerRadius = self.avatar.frame.size.width / 2;
    self.avatar.layer.masksToBounds = YES;
    
//    NSLog(@"frames: %@ %@", NSStringFromCGRect(self.frame), NSStringFromCGRect(self.avatar.frame));
}

-(void)setImageWithURL:(NSURL *)url {
    [CSAvatarLoader setImageWithURL:url inImageView:self.avatar];
}

@end
