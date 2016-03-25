//
//  CSStickerCategoryCollectionViewCell.m
//  Spika
//
//  Created by mislav on 24/03/16.
//  Copyright © 2016 Clover Studio. All rights reserved.
//

#import "CSStickerCategoryCollectionViewCell.h"

@implementation CSStickerCategoryCollectionViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
}

-(void)setSelected:(BOOL)selected {
    if (selected) {
        self.backgroundColor = [UIColor whiteColor];
    }
    else {
        self.backgroundColor = [UIColor clearColor];
    }
}

@end
