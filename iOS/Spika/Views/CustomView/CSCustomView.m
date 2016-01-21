//
//  CustomView.m
//  chaplideviap
//
//  Created by mislav on 23/09/15.
//  Copyright (c) 2015 Zappallas. All rights reserved.
//

#import "CSCustomView.h"

@implementation CSCustomView


// ****** Don't touch this *******

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        [self initWithNib];
    }
    return self;
}

- (instancetype)initWithCoder:(NSCoder *)coder
{
    self = [super initWithCoder:coder];
    if (self) {
        [self initWithNib];
    }
    return self;
}

- (void)initWithNib {
    UIView *view = [[[NSBundle mainBundle] loadNibNamed:NSStringFromClass([self class])
                                                     owner:self
                                                   options:nil] objectAtIndex:0];
    view.frame = self.bounds;
    [self addSubview:view];
    [self customInit];
}

// ****** Don't touch this *******


// ****** Override this *******

- (void)customInit {
}

@end
