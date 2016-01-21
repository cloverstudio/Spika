//
//  ChatSettingsView.h
//  Prototype
//
//  Created by Ivo Peric on 27/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ChatSettingsViewDelegate.h"

@interface CSChatSettingsView : UIView

@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (nonatomic, strong) NSArray *items;
@property (weak, nonatomic) IBOutlet UIView *contentView;
@property (strong, nonatomic) UIToolbar *backgroundView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *widthOfTable;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightOfTable;
@property (weak, nonatomic) IBOutlet UIView *viewForDismiss;

@property (nonatomic, weak) id <CSChatSettingsViewDelegate> delegate;

- (CGFloat)menuHeight;
-(void) animateTableViewToOpen:(BOOL) toOpen withMenu:(CSChatSettingsView*) menu;

@property (nonatomic) CGRect originalRect;

@end
