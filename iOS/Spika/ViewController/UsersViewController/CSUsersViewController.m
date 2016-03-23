//
//  UsersViewController.m
//  Prototype
//
//  Created by Ivo Peric on 27/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSUsersViewController.h"
#import "CSResponseModel.h"
#import "CSUserModel.h"
#import "CSUsersTableViewCell.h"
#import "CSConfig.h"
#import "CSCustomConfig.h"

@interface CSUsersViewController () <UITableViewDelegate, UITableViewDataSource>

@end

@implementation CSUsersViewController

-(id)initWithRoomID:(NSString *)roomID andToken:(NSString *)token{
    
    self = [super init];
    if(self){
        self.roomID = roomID;
        self.token = token;
    }
    return self;


}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.parentView.backgroundColor = kAppGrayLight(1);
    self.tableView.backgroundColor = kAppGrayLight(1);
    
    [self.tableView registerNib:[UINib nibWithNibName:NSStringFromClass([CSUsersTableViewCell class]) bundle:nil]
         forCellReuseIdentifier:kAppUsersTable];
    
    [self getUsers];
}

-(void)viewWillAppear:(BOOL)animated{
    self.title = [NSString stringWithFormat:@"Users in %@", self.roomID];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)viewDidLayoutSubviews{
    
    [self.tableView setSeparatorInset:UIEdgeInsetsZero];
    if ([self.tableView respondsToSelector:@selector(setLayoutMargins:)]) {
        [self.tableView setLayoutMargins:UIEdgeInsetsZero];
    }
    
    self.tableView.allowsSelection = NO;
    
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    CSUserModel* user = [self.users objectAtIndex:indexPath.row];

    CSUsersTableViewCell*cell = [tableView dequeueReusableCellWithIdentifier:kAppUsersTable forIndexPath:indexPath];
    cell.user = user;
    
    return cell;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 108;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.users.count;
}

-(void) getUsers{
    
    NSString* url = [NSString stringWithFormat:@"%@%@/%@", [CSCustomConfig sharedInstance].server_url, kAppGetUsersInRoom, self.roomID];
    
    [[CSApiManager sharedManager] apiGETCallWithURL:url
                                        indicatorVC:self
                                       completition:^(CSResponseModel *responseModel) {
        
                                           self.users = [NSMutableArray new];
        
                                           for(NSDictionary* item in (NSArray*)responseModel.data) {
                                               CSUserModel *itemUser = [[CSUserModel alloc] initWithDictionary:item error:nil];
                                               [self.users addObject:itemUser];
                                               [self.tableView reloadData];
                                           }
                                       }];
}

-(void)dealloc {
    NSLog(@"dealloc: UsersViewController");
}

@end
