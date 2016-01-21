//
//  BaseTableViewCell.m
//  Prototype
//
//  Created by Ivo Peric on 31/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSBaseTableViewCell.h"

@implementation CSBaseTableViewCell

- (void)awakeFromNib {
    // Initialization code
    self.selectionStyle = UITableViewCellSelectionStyleNone;
    
    self.layer.shouldRasterize = YES;
    self.layer.rasterizationScale = [[UIScreen mainScreen] scale];
    
    self.lpgr = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(handleLongPressGestures:)];
    
    [self addGestureRecognizer:self.lpgr];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)handleLongPressGestures:(UILongPressGestureRecognizer *)sender {
    if (sender.state == UIGestureRecognizerStateBegan) {
        [sender.view becomeFirstResponder];
        UIMenuController *menuController = [UIMenuController sharedMenuController];
        UIMenuItem *it = [[UIMenuItem alloc] initWithTitle:@"Details" action:@selector(handleDetails:)];
        [menuController setMenuItems:[NSArray arrayWithObjects:it, nil]];
    }
}

- (void)handleDetails:(id)sender
{
    NSLog(@"Action triggered, however need some way to refer the tapped cell");
    if ([(NSObject *)self.delegate respondsToSelector:@selector(onInfoClicked:)]) {
        [self.delegate onInfoClicked:self.message];
    }
}

-(BOOL)canBecomeFirstResponder {
    return YES;
}

- (BOOL)canPerformAction:(SEL)action withSender:(id)sender
{
    if ([self.message.deleted longLongValue] > 0) {
        return NO;
    }
    if (action == @selector(handleDetails:)) {
        return YES;
    }
    return NO;
}

@end
