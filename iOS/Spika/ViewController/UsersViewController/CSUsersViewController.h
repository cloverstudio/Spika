//
//  UsersViewController.h
//  Prototype
//
//  Created by Ivo Peric on 27/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CSBaseViewController.h"

@interface CSUsersViewController : CSBaseViewController

@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (nonatomic, strong) NSString* roomID;
@property (nonatomic, strong) NSString* token;
@property (nonatomic, strong) NSMutableArray* users;
@property (strong, nonatomic) IBOutlet UIView *parentView;

-(id) initWithRoomID: (NSString *) roomID andToken:(NSString*)token;

@end
