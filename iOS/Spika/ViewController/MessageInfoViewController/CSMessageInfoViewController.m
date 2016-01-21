//
//  MessageInfoViewController.m
//  Prototype
//
//  Created by Ivo Peric on 31/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSMessageInfoViewController.h"
#import "CSSeenByModel.h"
#import "CSSeenByTableViewCell.h"
#import "CSConfig.h"

@interface CSMessageInfoViewController () <UITableViewDelegate, UITableViewDataSource, UIAlertViewDelegate>

@end

@implementation CSMessageInfoViewController

-(id)initWithMessage:(CSMessageModel *)message andUser: (CSUserModel*) user{

    self = [super init];
    if(self){
        self.message = message;
        self.activeUser = user;
    }
    return self;

}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self.tableView registerNib:[UINib nibWithNibName:NSStringFromClass([CSSeenByTableViewCell class]) bundle:nil]
         forCellReuseIdentifier:kAppSeenByTable];
    
    [self setData];
    
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

-(void)viewWillAppear:(BOOL)animated{
    self.title = @"Info";
}

- (IBAction)deleteMessage:(id)sender {
    
    UIAlertView* alertView = [[UIAlertView alloc] initWithTitle:@"Delete" message:@"Are You sure?" delegate:self cancelButtonTitle:@"NO" otherButtonTitles:@"Delete", nil];
    [alertView show];
    
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    NSString *title = [alertView buttonTitleAtIndex:buttonIndex];
    if([title isEqualToString:@"Delete"]){
        [[NSNotificationCenter defaultCenter] postNotificationName:kAppDeleteMessageNotification
                                                            object:nil
                                                          userInfo:[NSDictionary dictionaryWithObject:self.message forKey:paramMessage]];
        [self.navigationController popViewControllerAnimated:YES];
    }
}

-(void) setData{

    self.senderValue.text = self.message.user.name;
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"yyyy/MM/dd HH:mm:ss"];
    NSDate* date = [NSDate dateWithTimeIntervalSince1970:[self.message.created longLongValue] / 1000];
    NSString* dateSent = [NSString stringWithFormat:@"%@", [formatter stringFromDate:date]];
    self.sentAtValue.text = dateSent;
    
    if(![self.activeUser.userID isEqualToString:self.message.user.userID]
            || [self.message.type intValue] == 1000
            || [self.message.type intValue] == 1001){
        self.deleteMessageButton.hidden = YES;
    }
    
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    NSDictionary* dict = [self.message.seenBy objectAtIndex:indexPath.row];
    NSError* error;
    CSSeenByModel* seenBy = [[CSSeenByModel alloc] initWithDictionary:dict error:&error];
    
    CSSeenByTableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:kAppSeenByTable forIndexPath:indexPath];
    cell.seenBy = seenBy;
    
    return cell;
    
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.message.seenBy.count;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 38;
}

@end
