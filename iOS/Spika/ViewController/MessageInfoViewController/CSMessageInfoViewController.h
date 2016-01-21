//
//  MessageInfoViewController.h
//  Prototype
//
//  Created by Ivo Peric on 31/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CSBaseViewController.h"

@interface CSMessageInfoViewController : CSBaseViewController

@property (weak, nonatomic) IBOutlet UILabel *senderValue;
@property (weak, nonatomic) IBOutlet UILabel *sentAtValue;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
- (IBAction)deleteMessage:(id)sender;
@property (weak, nonatomic) IBOutlet UIButton *deleteMessageButton;

@property (nonatomic, strong) CSMessageModel* message;
-(id)initWithMessage:(CSMessageModel *)message andUser: (CSUserModel*) user;

@end
