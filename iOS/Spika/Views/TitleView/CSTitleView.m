//
//  CSTitleView.m
//  Spika
//
//  Created by mislav on 14/01/16.
//  Copyright © 2016 Clover Studio. All rights reserved.
//

#import "CSTitleView.h"

@interface CSTitleView()

@property (nonatomic, strong) NSString* title;
@property (nonatomic, strong) NSString* subTitle;

@end

@implementation CSTitleView

- (instancetype)init
{
    self = [super init];
    if (self) {
        self.numberOfLines = 2;
        self.textAlignment = NSTextAlignmentCenter;
        self.textColor = [UIColor blackColor];
    }
    return self;
}

-(void)setTitle:(NSString *)title {
    _title = title;
    
    [self generateTitleAndSubtitle];
}

-(void)setSubtitle:(NSString *)subTitle {
    _subTitle = subTitle;
    
    [self generateTitleAndSubtitle];
}

-(void)generateTitleAndSubtitle {
    NSMutableAttributedString *titleAndSubtitle = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%@\n%@", self.title, self.subTitle]];
    if (self.title) {
        NSRange titleRange = [titleAndSubtitle.mutableString rangeOfString:self.title];
        [titleAndSubtitle addAttribute:NSFontAttributeName
                                 value:[UIFont systemFontOfSize:20.0]
                                 range:titleRange];
    }
    if (self.subTitle) {
        NSRange subTitleRange = [titleAndSubtitle.mutableString rangeOfString:self.subTitle];
        [titleAndSubtitle addAttribute:NSFontAttributeName
                                 value:[UIFont systemFontOfSize:10.0]
                                 range:subTitleRange];
    }
    self.attributedText = titleAndSubtitle;
    [self sizeToFit];
    [self.superview layoutSubviews];
}

@end
