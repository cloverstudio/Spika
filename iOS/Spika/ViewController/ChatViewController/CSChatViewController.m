//
//  ChatViewController.m
//  Prototype
//
//  Created by Ivo Peric on 28/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSChatViewController.h"
#import "CSModel.h"
#import "CSUserModel.h"
#import "CSMessageModel.h"
#import "CSResponseModel.h"
#import "UIActivityIndicatorView+AFNetworking.h"
#import "CSLoginResult.h"
#import "CSGetMessages.h"
#import "CSConfig.h"
#import "CSTypingModel.h"
#import "CSMyTextMessageTableViewCell.h"
#import "CSUserInfoTableViewCell.h"
#import "CSYourTextMessageTableViewCell.h"
#import "CSUsersViewController.h"
#import "CSYourImageMessageTableViewCell.h"
#import "CSMyImageMessageTableViewCell.h"
#import "CSYourMediaMessageTableViewCell.h"
#import "CSMyMediaMessageTableViewCell.h"
#import "CSMessageInfoViewController.h"
#import "CSImagePreviewView.h"
#import "CSVideoPreviewView.h"
#import "CSAudioPreviewView.h"
#import "CSDownloadManager.h"
#import "CSUploadImagePreviewViewController.h"
#import "CSEmitJsonCreator.h"
#import "CSUploadVideoPreviewViewController.h"
#import "CSRecordAudioViewController.h"
#import "CSMapViewController.h"
#import "CSUploadManager.h"
#import "CSSocketController.h"
#import "CSCustomConfig.h"
#import <AddressBook/AddressBook.h>
#import <AddressBookUI/AddressBookUI.h>
#import "UIAlertView+Blocks.h"
#import "CSTitleView.h"
#import "CSNotificationOverlayView.h"
#import "CSChatErrorCodes.h"

@interface CSChatViewController () <UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate, CSChatSettingsViewDelegate, CSCellClickedDelegate, UIDocumentInteractionControllerDelegate, CSMenuViewDelegate, UIImagePickerControllerDelegate, UINavigationControllerDelegate, CSChatSocketControllerDelegate, ABPeoplePickerNavigationControllerDelegate>

@property (nonatomic, strong) NSDictionary *parameters;
@property (nonatomic) BOOL isLoading;
@property (nonatomic, strong) CSTitleView *titleView;
@property (nonatomic, strong) UIActivityIndicatorView *indicator;

@end

@implementation CSChatViewController

-(instancetype)initWithParameters:(NSDictionary *)parameters {
    if (self = [super init]) {
        self.parameters = parameters;
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
        
//    self.navigationController.navigationBar.translucent = NO;
//    self.navigationController.navigationBar.barTintColor = kAppDefaultColor(1);
    self.navigationController.navigationBar.tintColor = kAppBubbleRightColor;
//    [self.navigationController.navigationBar setTitleTextAttributes:@{NSForegroundColorAttributeName : [UIColor whiteColor]}];
    
    self.tableView.estimatedRowHeight = 44;
    [self.tableView setTransform:CGAffineTransformMakeRotation(-M_PI)];
    
    [self.tableView registerNib:[UINib nibWithNibName:NSStringFromClass([CSMyTextMessageTableViewCell class]) bundle:nil]
         forCellReuseIdentifier:kAppMyTextMessageCell];
    [self.tableView registerNib:[UINib nibWithNibName:NSStringFromClass([CSUserInfoTableViewCell class]) bundle:nil]
         forCellReuseIdentifier:kAppUserInfoMessageCell];
    [self.tableView registerNib:[UINib nibWithNibName:NSStringFromClass([CSYourTextMessageTableViewCell class]) bundle:nil]
         forCellReuseIdentifier:kAppYourTextMessageCell];
    [self.tableView registerNib:[UINib nibWithNibName:NSStringFromClass([CSYourImageMessageTableViewCell class]) bundle:nil]
         forCellReuseIdentifier:kAppYourImageMessageCell];
    [self.tableView registerNib:[UINib nibWithNibName:NSStringFromClass([CSMyImageMessageTableViewCell class]) bundle:nil]
         forCellReuseIdentifier:kAppMyImageMessageCell];
    [self.tableView registerNib:[UINib nibWithNibName:NSStringFromClass([CSYourMediaMessageTableViewCell class]) bundle:nil]
         forCellReuseIdentifier:kAppYourMediaMessageCell];
    [self.tableView registerNib:[UINib nibWithNibName:NSStringFromClass([CSMyMediaMessageTableViewCell class]) bundle:nil]
         forCellReuseIdentifier:kAppMyMediaMessageCell];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(deleteMessageInChat:)
                                                 name:kAppDeleteMessageNotification
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(sendFileMessage:)
                                                 name:kAppFileUploadedNotification
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(sendLocationMessage:)
                                                 name:kAppLocationSelectedNotification
                                               object:nil];
    
    self.indicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
    [self.indicator setFrame:CGRectMake(0, 0, 44, 44)];
    [self.indicator setHidesWhenStopped:YES];
    self.tableView.tableFooterView = self.indicator;

    
    self.tableView.backgroundColor = [UIColor whiteColor];
    
    self.messages = [[NSMutableArray alloc]init];
    self.tempSentMessagesLocalId = [[NSMutableArray alloc] init];
    self.typingUsers = [[NSMutableArray alloc] init];
    self.lastDataLoadedFromNet = [[NSMutableArray alloc] init];
    
    NSError *error;
    self.activeUser = [[CSUserModel alloc] initWithDictionary:self.parameters error:&error];
    [self login:self.parameters];    
}



-(void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    self.titleView = [CSTitleView new];
    [self.titleView setTitle:self.activeUser.roomID];
    [self.titleView setSubtitle:self.activeUser.name];
    
    self.navigationItem.titleView = self.titleView;
    
    UIBarButtonItem *anotherButton = [[UIBarButtonItem alloc] initWithTitle:@"•••" style:UIBarButtonItemStylePlain target:self action:@selector(onSettingsClicked)];
    self.navigationItem.rightBarButtonItem = anotherButton;
    
    NSArray* array = kAppMenuSettingsArray;
    [self.chatsettingsView setItems:array];
    self.chatsettingsView.hidden = YES;
    self.chatsettingsView.delegate = self;
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardIsMoving:)
                                                 name:UIKeyboardWillChangeFrameNotification
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(applicationDidBecomeActive:)
                                                 name:UIApplicationDidBecomeActiveNotification
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(applicationDidEnterBackground:)
                                                 name:UIApplicationDidEnterBackgroundNotification
                                               object:nil];
}

-(void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:UIKeyboardWillChangeFrameNotification
                                                  object:nil];
    
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:UIApplicationDidBecomeActiveNotification
                                                  object:nil];
    
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:UIApplicationDidEnterBackgroundNotification
                                                  object:nil];
}



-(void)viewDidLayoutSubviews{
    [super viewDidLayoutSubviews];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return YES;
}

-(void) keyboardIsMoving: (NSNotification*) aNotification{
    
    NSDictionary* info = [aNotification userInfo];
    CGRect kbSize = [[info objectForKey:UIKeyboardFrameEndUserInfoKey] CGRectValue];
    
    if(kbSize.origin.y == kAppHeight){
        [UIView animateWithDuration:0.3 animations:^{
            self.bottomMargin.constant = 0;
        }];
    }else{
        [UIView animateWithDuration:0.3 animations:^{
            self.bottomMargin.constant = kbSize.size.height;
        }];
    }
}

-(void)applicationDidBecomeActive: (NSNotification*) notification {
    if (self.token.length) {
        [self connectToSocket];
        
        CSMessageModel* lastMessage = [self.messages firstObject];
        [self getLatestMessages:lastMessage._id];
    }
}

-(void)applicationDidEnterBackground: (NSNotification*) notification {
    [[CSSocketController sharedController] close];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

#pragma mark - socket method
-(void) connectToSocket {
    NSDictionary *parametersLogin = [self.activeUser toDictionary];
    [[CSSocketController sharedController] registerForChat:parametersLogin withChatSocketDelegate:self];
}

-(void) didReceiveNewMessage: (CSMessageModel *) message {
    
    if (self.tableView.contentOffset.y) {
        float verticalPosition = self.navigationController.navigationBar.frame.size.height
        + [UIApplication sharedApplication].statusBarFrame.size.height
        + self.footerView.frame.origin.y;
        
        [CSNotificationOverlayView notificationOverlayFromChatWithMessage:@"new message"
                                                         verticalPosition:verticalPosition
                                                                 callback:^(BOOL wasSelected) {
                                                                     if (wasSelected) {
                                                                         [self.tableView scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:0 inSection:0] atScrollPosition:UITableViewScrollPositionTop animated:YES];
                                                                     }
                                                                 }];
    }
    
    if ([self.tempSentMessagesLocalId containsObject:message.localID]){
        
        [self.tempSentMessagesLocalId removeObject:message.localID];
        
        for(CSMessageModel* item in self.messages){
            
            if(item.localID != nil && [item.localID isEqualToString:message.localID]){
                
                item.status = kAppMessageStatusDelivered;
                item._id = message._id;
                item.created = message.created;
                break;
                
            }
            
        }
        
        [self.tableView reloadData];
        
    }
    else {
        if (self.tableView.contentOffset.y) {
            [self.tableView beginUpdates];
            [self.messages insertObject:message atIndex:0];
            [self.tableView insertRowsAtIndexPaths:@[[NSIndexPath indexPathForRow:0 inSection:0]] withRowAnimation:UITableViewRowAnimationTop];
            [self.tableView endUpdates];
        }
        else {
            [self.messages insertObject:message atIndex:0];
            [self.tableView reloadData];
        }
        
        if(![message.user.userID isEqualToString:self.activeUser.userID]){
            NSMutableArray* openMessage = [NSMutableArray new];
            [openMessage addObject:message._id];
            [self sendOpenMessages:openMessage];
        }
    }
}

-(void) didReceiveTyping: (CSTypingModel*) typing{
    
    if([typing.user.userID isEqualToString:self.activeUser.userID]){
        return;
    }
    
    if([typing.type intValue] == kAppTypingStatusOFF){
        [self isUserAlreadyTyping:typing.user];
        [self generateTypingLabel];
    }else{
        [self.typingUsers addObject:typing.user];
        [self generateTypingLabel];
    }
    
}

-(void) didUserLeft: (CSUserModel*) user{
    
    [self isUserAlreadyTyping:user];
    [self generateTypingLabel];
    
}

-(void) didSentMessage: (CSMessageModel*) sentMessage{
    
    [self.tableView beginUpdates];
    [self.messages insertObject:sentMessage atIndex:0];
    [self.tableView insertRowsAtIndexPaths:@[[NSIndexPath indexPathForRow:0 inSection:0]] withRowAnimation:UITableViewRowAnimationTop];
    [self.tableView endUpdates];

}

-(void) didMessageUpdated:(NSArray*) updatedMessages{

    for(CSMessageModel *item in self.messages){
        for(CSMessageModel *itemNew in updatedMessages){
            if([item._id isEqualToString:itemNew._id]){
                [item updateMessageWithData:itemNew];
                continue;
            }
        }
    }
    [self.tableView reloadData];

}

- (IBAction)sendMessage:(id)sender {
    
    if(!self.isTyping){
        [self openMenu];
        return;
    }
    
    NSString *message = [NSString alloc];
    message = self.etMessage.text;
    
    CSMessageModel* messageModel = [CSMessageModel createMessageWithUser:self.activeUser andMessage:message andType:[NSNumber numberWithInt:kAppTextMessageType] andFile:nil andLocation:nil];
    [self.tempSentMessagesLocalId addObject:messageModel.localID];
    
    [self didSentMessage:messageModel];
    [self.etMessage setText:@""];
    [self changeEditing:self.etMessage];
    self.isTyping = NO;
    
    NSDictionary *parameters = [CSEmitJsonCreator createEmitSendMessage:messageModel andUser:self.activeUser andMessage:message andFile:nil andLocation:nil];
    
    NSArray *array = [NSArray arrayWithObject:parameters];
    [[CSSocketController sharedController] emit:kAppSocketSendMessage args:array];
}

- (void)sendContact:(NSString *)vCard {
    CSMessageModel* messageModel = [CSMessageModel createMessageWithUser:self.activeUser andMessage:vCard andType:[NSNumber numberWithInt:kAppContactMessageType] andFile:nil andLocation:nil];
    [self.tempSentMessagesLocalId addObject:messageModel.localID];
    [self didSentMessage:messageModel];
    
    NSDictionary *parameters = [CSEmitJsonCreator createEmitSendMessage:messageModel andUser:self.activeUser andMessage:vCard andFile:nil andLocation:nil];
    NSArray *array = [NSArray arrayWithObject:parameters];
    [[CSSocketController sharedController] emit:kAppSocketSendMessage args:array];
}

-(void)sendFileMessage:(NSNotification*) notificationData{
    
    [self.menuView animateHide];
    
    NSDictionary *dict = notificationData.userInfo;
    
    NSError* error;
    
    NSDictionary* response = [dict objectForKey:paramResponseObject];
    CSResponseModel *responseModel = [[CSResponseModel alloc] initWithDictionary:response error:&error];
    CSFileModel* resultModel = [[CSFileModel alloc] initWithDictionary:responseModel.data error:&error];
    
    CSMessageModel* messageModel = [CSMessageModel createMessageWithUser:self.activeUser andMessage:@"" andType:[NSNumber numberWithInt:kAppFileMessageType] andFile:resultModel andLocation:nil];
    [self.tempSentMessagesLocalId addObject:messageModel.localID];
    
    [self didSentMessage:messageModel];
    
    NSDictionary *parameters = [CSEmitJsonCreator createEmitSendMessage:messageModel andUser:self.activeUser andMessage:@"" andFile:resultModel andLocation:nil];
    
    NSArray *array = [NSArray arrayWithObject:parameters];
    [[CSSocketController sharedController] emit:kAppSocketSendMessage args:array];
}

-(void)sendLocationMessage:(NSNotification*) notificationData{
    
    [self.menuView animateHide];
    
    NSDictionary *dict = notificationData.userInfo;
    
    CSLocationModel* response = [dict objectForKey:paramLocation];
    NSString* address = [dict objectForKey:paramAddress];
    
    CSMessageModel* messageModel = [CSMessageModel createMessageWithUser:self.activeUser andMessage:address andType:[NSNumber numberWithInt:kAppLocationMessageType] andFile:nil andLocation:response];
    [self.tempSentMessagesLocalId addObject:messageModel.localID];
    
    [self didSentMessage:messageModel];
    
    NSDictionary *parameters = [CSEmitJsonCreator createEmitSendMessage:messageModel andUser:self.activeUser andMessage:address andFile:nil andLocation:response];
    
    NSArray *array = [NSArray arrayWithObject:parameters];
    [[CSSocketController sharedController] emit:kAppSocketSendMessage args:array];
}


-(void)deleteMessageInChat:(NSNotification*)notificationData{
    
    NSDictionary *dict = notificationData.userInfo;
    CSMessageModel* messageToDelete = [dict objectForKey:paramMessage];

    NSDictionary *parameters = @{paramUserID : self.activeUser.userID,
                                 paramMessageID : messageToDelete._id};
    
    NSArray *array = [NSArray arrayWithObject:parameters];
    [[CSSocketController sharedController] emit:kAppSocketDeleteMessage args:array];
}

-(void) sendTypingWithType:(NSString*) type{
    
    NSDictionary *parameters = @{paramRoomID : self.activeUser.roomID,
                                 paramUserID : self.activeUser.userID,
                                 paramType : type};
    
    NSArray *array = [NSArray arrayWithObject:parameters];
    [[CSSocketController sharedController] emit:kAppSocketSendTyping args:array];
}

-(void) sendOpenMessages: (NSArray*) messageIds{
    NSDictionary *parameters = @{paramMessageIDs : messageIds,
                                 paramUserID : self.activeUser.userID};
    
    NSArray *array = [NSArray arrayWithObject:parameters];
    [[CSSocketController sharedController] emit:kAppSocketOpenMessage args:array];
}

#pragma mark - api data
-(void) login: (NSDictionary*) parameters{

    NSString* urlLogin = [NSString stringWithFormat:@"%@%@", [CSCustomConfig sharedInstance].server_url, kAppLogin];
    
    [[CSApiManager sharedManager] apiPOSTCallWithUrl:urlLogin
                                          parameters:parameters
                                         indicatorVC:self
                                     toShowIndicator:YES
                                     toHideIndicator:NO
                                        completition:^(CSResponseModel *responseModel) {
    
                                            CSLoginResult *login = [[CSLoginResult alloc] initWithDictionary:responseModel.data error:nil];
        
                                            self.token = [NSString alloc];
                                            self.token = login.token;
                                            
                                            [CSApiManager sharedManager].accessToken = self.token;
        
                                            [self connectToSocket];
                                            [self getMessages];
                                        }];
}

-(void) getMessages {
    
    NSString* url = [NSString stringWithFormat:@"%@%@/%@/0", [CSCustomConfig sharedInstance].server_url, kAppGetMessages, self.activeUser.roomID];
    
    [[CSApiManager sharedManager] apiGETCallWithURL:url
                                        indicatorVC:self
                                    toShowIndicator:NO
                                    toHideIndicator:YES
                                       completition:^(CSResponseModel *responseModel){
        
                                           CSGetMessages *messages = [[CSGetMessages alloc] initWithDictionary:responseModel.data error:nil];
        
                                           [self.lastDataLoadedFromNet removeAllObjects];
                                           [self.lastDataLoadedFromNet addObjectsFromArray:messages.messages];
        
                                           [self.messages addObjectsFromArray:messages.messages];
        
                                           [self sortMessages];
                                           [self.tableView reloadData];
        
                                           //send unseen messages
                                           NSArray* unSeenMessages = [CSUtils generateUnSeenMessageIdsFrom:messages.messages andActiveUser:self.activeUser];
                                           [self sendOpenMessages:unSeenMessages];
                                       }];
    
}

-(void) getMessagesWithLastMessageId:(NSString *) lastMessageId {
    
    NSString* url = [NSString stringWithFormat:@"%@%@/%@/%@", [CSCustomConfig sharedInstance].server_url, kAppGetMessages, self.activeUser.roomID, lastMessageId];
    
    [[CSApiManager sharedManager] apiGETCallWithURL:url
                                        indicatorVC:self
                                       completition:^(CSResponseModel *responseModel) {
        
                                           CSGetMessages *messages = [[CSGetMessages alloc] initWithDictionary:responseModel.data error:nil];
        
                                           [self.lastDataLoadedFromNet removeAllObjects];
                                           [self.lastDataLoadedFromNet addObjectsFromArray:messages.messages];
                                           [self.messages addObjectsFromArray:messages.messages];
        
                                           [self sortMessages];
                                           [self.tableView reloadData];
        
                                           self.isLoading = NO;
                                           [self.indicator stopAnimating];
                                       }];
}

-(void) getLatestMessages:(NSString *) lastMessageId {
    
    NSString* url = [NSString stringWithFormat:@"%@%@/%@/%@", [CSCustomConfig sharedInstance].server_url, kAppGetLatestMessages, self.activeUser.roomID, lastMessageId];
    
    [[CSApiManager sharedManager] apiGETCallWithURL:url
                                        indicatorVC:self
                                    toShowIndicator:NO
                                    toHideIndicator:YES
                                       completition:^(CSResponseModel *responseModel) {
        
                                           CSGetMessages *messages = [[CSGetMessages alloc] initWithDictionary:responseModel.data error:nil];
        
                                           [self.lastDataLoadedFromNet addObjectsFromArray:messages.messages];
                                           [self.messages addObjectsFromArray:messages.messages];
        
                                           [self sortMessages];
                                           [self.tableView reloadData];
        
                                           //send unseen messages
                                           NSArray* unSeenMessages = [CSUtils generateUnSeenMessageIdsFrom:messages.messages andActiveUser:self.activeUser];
                                           [self sendOpenMessages:unSeenMessages];
                                       }];
}

-(void) sortMessages{
    NSSortDescriptor *sortDescriptor = [[NSSortDescriptor alloc] initWithKey:paramCreated ascending:NO];
    [self.messages sortUsingDescriptors:[NSArray arrayWithObject:sortDescriptor]];
    [self.lastDataLoadedFromNet sortUsingDescriptors:[NSArray arrayWithObject:sortDescriptor]];
}

#pragma mark - UI HELPERS

-(void) generateTypingLabel{
    
    if (self.typingUsers.count < 1) {
        [self.titleView setSubtitle:self.activeUser.name];
    }
    else if (self.typingUsers.count == 1) {
        CSUserModel* user = [self.typingUsers objectAtIndex:0];
        [self.titleView setSubtitle:[NSString stringWithFormat:NSLocalizedStringFromTable(@"%@ is typing...", @"CSChatLocalization", nil), user.name]];
    }
    else {
        NSString* text = @"";
        for(CSUserModel* item in self.typingUsers){
            text = [text stringByAppendingFormat:@"%@, ", item.name];
        }
        text = [text substringToIndex:text.length - 2];
        [self.titleView setSubtitle:[NSString stringWithFormat:NSLocalizedStringFromTable(@"%@ are typing...", @"CSChatLocalization", nil), text]];
    }
}

-(BOOL) isUserAlreadyTyping: (CSUserModel*) user{
    for(CSUserModel* item in self.typingUsers){
        if([item.userID isEqualToString:user.userID]){
            [self.typingUsers removeObject:item];
            return YES;
        }
    }
    return NO;
}

- (IBAction)changedTextInTextField:(id)sender {
//    if(self.etMessage.text.length > 0 && self.isTyping == NO){
//        self.isTyping = YES;
//        [self sendTypingWithType:[NSString stringWithFormat:@"%d", kAppTypingStatusON]];
//    }else if(self.etMessage.text.length == 0 && self.isTyping == YES){
//        self.isTyping = NO;
//        [self sendTypingWithType:[NSString stringWithFormat:@"%d", kAppTypingStatusOFF]];
//    }
}

- (IBAction)changeEditing:(id)sender {
    if(self.etMessage.text.length > 0 && self.isTyping == NO){
        self.isTyping = YES;
        [self sendTypingWithType:[NSString stringWithFormat:@"%d", kAppTypingStatusON]];
        [self.sendButton setImage:[UIImage imageNamed:@"send_btn"] forState:UIControlStateNormal];
    }else if(self.etMessage.text.length == 0 && self.isTyping == YES){
        self.isTyping = NO;
        [self sendTypingWithType:[NSString stringWithFormat:@"%d", kAppTypingStatusOFF]];
        [self.sendButton setImage:[UIImage imageNamed:@"attach_btn"] forState:UIControlStateNormal];
    }

}

#pragma mark - table view methods

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.messages.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    CSMessageModel *message = [self.messages objectAtIndex:indexPath.row];
    CSBaseTableViewCell *cell;
    
    if ([message.deleted longLongValue] > 0) {
        if ([message.user.userID isEqualToString:self.activeUser.userID]) {
            cell = [self.tableView dequeueReusableCellWithIdentifier:kAppMyTextMessageCell forIndexPath:indexPath];
        }
        else {
            cell = [self.tableView dequeueReusableCellWithIdentifier:kAppYourTextMessageCell forIndexPath:indexPath];
        }
    }
    else if ([message.user.userID isEqualToString:self.activeUser.userID] && ([message.type intValue] == kAppTextMessageType)) {
        cell = [self.tableView dequeueReusableCellWithIdentifier:kAppMyTextMessageCell forIndexPath:indexPath];
    }
    else if ([message.user.userID isEqualToString:self.activeUser.userID] && ([message.type intValue] == kAppFileMessageType || [message.type intValue] == kAppLocationMessageType || [message.type intValue] == kAppContactMessageType)){
        if([CSUtils isMessageAnImage:message]) {
            cell = [self.tableView dequeueReusableCellWithIdentifier:kAppMyImageMessageCell forIndexPath:indexPath];
        }
        else {
            cell = [self.tableView dequeueReusableCellWithIdentifier:kAppMyMediaMessageCell forIndexPath:indexPath];
        }
    }
    else if ([message.type intValue] == kAppNewUserMessageType || [message.type intValue] == kAppLeaveUserMessageType) {
        cell = [self.tableView dequeueReusableCellWithIdentifier:kAppUserInfoMessageCell forIndexPath:indexPath];
    }
    else if ([message.type intValue] == kAppTextMessageType) {
        cell = [self.tableView dequeueReusableCellWithIdentifier:kAppYourTextMessageCell forIndexPath:indexPath];
    }
    else if ([message.type intValue] == kAppFileMessageType || [message.type intValue] == kAppLocationMessageType || [message.type intValue] == kAppContactMessageType) {
        if ([CSUtils isMessageAnImage:message]) {
            cell = [self.tableView dequeueReusableCellWithIdentifier:kAppYourImageMessageCell forIndexPath:indexPath];
        }
        else {
            cell = [self.tableView dequeueReusableCellWithIdentifier:kAppYourMediaMessageCell forIndexPath:indexPath];
        }
    }
    
    CSMessageModel *messageAfter = [self.messages objectAtIndex:MAX(indexPath.row - 1, 0)];
    cell.shouldShowAvatar = ![message.userID isEqualToString:messageAfter.userID] || indexPath.row == 0 || [messageAfter.type intValue] == kAppNewUserMessageType || [messageAfter.type intValue] == kAppLeaveUserMessageType ? YES : NO;
    
    CSMessageModel *messageBefore = [self.messages objectAtIndex:MAX(MIN(indexPath.row + 1, self.messages.count - 1), 0)];
    cell.shouldShowName = ![message.userID isEqualToString:messageBefore.userID] || indexPath.row == self.messages.count - 1 || [messageBefore.type intValue] == kAppNewUserMessageType || [messageBefore.type intValue] == kAppLeaveUserMessageType ? YES : NO;
    
    NSDate *date = [NSDate dateWithTimeIntervalSince1970:message.created.longLongValue / 1000];
    NSDate *dateBefore = [NSDate dateWithTimeIntervalSince1970:messageBefore.created.longLongValue / 1000];
    cell.shouldShowDate =  (![[NSCalendar currentCalendar] isDate:date inSameDayAsDate:dateBefore] || indexPath.row == self.messages.count - 1) && message.created.intValue ? YES : NO;
    
    cell.message = message;
    cell.delegate = self;
    [cell setTransform:CGAffineTransformMakeRotation(M_PI)];
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{

    CSMessageModel* message = [self.messages objectAtIndex:indexPath.row];
    
    if([message.deleted longLongValue] > 0) {
        return;
    }
    
    if ([message.type intValue] == kAppFileMessageType) {
        
        self.navigationController.navigationBar.userInteractionEnabled = NO;
        
        float navigationHeight = self.navigationController.navigationBar.frame.size.height;
        float statusHeight = [UIApplication sharedApplication].statusBarFrame.size.height;
        
        if([CSUtils isMessageAnImage:message]){
            CSImagePreviewView* previewView = [[CSImagePreviewView alloc] init];
            [self.view addSubview:previewView];
            
            
            [previewView initializeViewWithMessage:message navigationAndStatusBarHeight:(statusHeight + navigationHeight) dismiss:^(void){
                
                [previewView removeFromSuperview];
                self.navigationController.navigationBar.userInteractionEnabled = YES;
                
            }];
        }else if([CSUtils isMessageAVideo:message]){
            
            CSVideoPreviewView* previewView = [[CSVideoPreviewView alloc] init];
            [self.view addSubview:previewView];
            
            [previewView initializeViewWithMessage:message navigationAndStatusBarHeight:(statusHeight + navigationHeight) dismiss:^(void){
                
                [previewView removeFromSuperview];
                self.navigationController.navigationBar.userInteractionEnabled = YES;
                
            }];
        }else if([CSUtils isMessageAAudio:message]){
            
            CSAudioPreviewView* previewView = [[CSAudioPreviewView alloc] init];
            [self.view addSubview:previewView];
            
            [previewView initializeViewWithMessage:message navigationAndStatusBarHeight:(statusHeight + navigationHeight) dismiss:^(void){
                
                [previewView removeFromSuperview];
                self.navigationController.navigationBar.userInteractionEnabled = YES;
                
            }];
        }else{
        
            NSString* filePath = [CSUtils getFileFromFileModel:message.file];
            if(![CSUtils isFileExistsWithFileName:filePath]){
                
                CSDownloadManager* downloadManager = [CSDownloadManager new];
                [downloadManager downloadFileWithUrl:[NSURL URLWithString:[CSUtils generateDownloadURLFormFileId:message.file.file.id]] destination:[NSURL URLWithString:filePath] viewForLoading:self.view completition:^(BOOL succes){
                    
                    [self openDocument:filePath];
                    
                }];
                
            }else{
                [self openDocument:filePath];
            }
            
        }
    
    }
    else if ([message.type intValue] == kAppLocationMessageType) {
        CSMapViewController* controller = [[CSMapViewController alloc] initWithMessage:message];
        [self.navigationController pushViewController:controller animated:YES];
    }
    else if ([message.type intValue] == kAppContactMessageType) {
        ABAddressBookRef addressBookRef = ABAddressBookCreateWithOptions(NULL, NULL);
        
        if (ABAddressBookGetAuthorizationStatus() == kABAuthorizationStatusNotDetermined) {
            ABAddressBookRequestAccessWithCompletion(addressBookRef, ^(bool granted, CFErrorRef error) {
                if (granted) {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        [self addContactToAddressBook:message.message];
                    });
                }
            });
        }
        else if (ABAddressBookGetAuthorizationStatus() == kABAuthorizationStatusAuthorized) {
            [self addContactToAddressBook:message.message];
        }
        else {
            [UIAlertView showWithTitle:NSLocalizedStringFromTable(@"Privacy Error", @"CSChatLocalization", nil)
                               message:NSLocalizedStringFromTable(@"Go to\nSettings > Privacy > Contacts\nand enable access", @"CSChatLocalization", nil)
                     cancelButtonTitle:@"OK"
                     otherButtonTitles:nil
                              tapBlock:nil
             ];
        }
    }
}

-(void)scrollViewDidScroll:(UIScrollView *)scrollView {
    if ((scrollView.contentOffset.y + scrollView.frame.size.height) >= scrollView.contentSize.height) {
        if (!self.isLoading) {
            [self onRefresh];
        }
    }
}

@synthesize documentController;
-(void) openDocument:(NSString*) filePath{
    self.documentController = [UIDocumentInteractionController interactionControllerWithURL:[NSURL fileURLWithPath:filePath]];
    self.documentController.delegate = self;
    [self.documentController presentOpenInMenuFromRect:CGRectZero inView:self.view animated:YES];
}

-(void) onRefresh {
    
    NSString* lastMessageId = @"0";
    if(self.lastDataLoadedFromNet.count > 0){
        
        self.isLoading = YES;
        [self.indicator startAnimating];
//        self.tableView.tableFooterView.hidden = NO;
        
        CSMessageModel* lastMessage = [self.lastDataLoadedFromNet lastObject];
        lastMessageId = lastMessage._id;
        
        [self getMessagesWithLastMessageId:lastMessageId];
    }
}

-(void) onSettingsClicked{
    [self.chatsettingsView animateTableViewToOpen:self.chatsettingsView.hidden withMenu:self.chatsettingsView];
}

-(void) onSettingsClickedPosition:(NSInteger)position{
    
    if(position == 0){
        CSUsersViewController* viewController = [[CSUsersViewController alloc] initWithRoomID:self.activeUser.roomID andToken:self.token];
        [self.navigationController pushViewController:viewController animated:YES];
    }
}

-(void)onDismissClicked{
    
    [self onSettingsClicked];
    
}



//delegates from cells
-(void)onInfoClicked:(CSMessageModel *)message{
    CSMessageInfoViewController *messageInfo = [[CSMessageInfoViewController alloc] initWithMessage:message andUser:self.activeUser];
    [self.navigationController pushViewController:messageInfo animated:YES];
}

#pragma mark - menu
-(void) openMenu{

    [self.etMessage resignFirstResponder];
    
    if(!self.menuView){
        
        self.menuView = [[CSMenuView alloc] init];
        
        [self.menuView initializeInView:self.view dismiss:^(void){
            [self.menuView removeFromSuperview];
            self.menuView = nil;
        }];
        
        self.menuView.delegate = self;
    }
}

-(void)onCamera{
    CSUploadImagePreviewViewController* controller = [CSUploadImagePreviewViewController new];
    [self.navigationController pushViewController:controller animated:YES];
}

-(void)onGallery{
    CSUploadImagePreviewViewController* controller = [[CSUploadImagePreviewViewController alloc] initWithType:kAppGalleryType];
    [self.navigationController pushViewController:controller animated:YES];
}

-(void)onLocation{
    CSMapViewController* controller = [[CSMapViewController alloc] initToPostLocation];
    [self.navigationController pushViewController:controller animated:YES];
}

-(void)onFile{
    UIImagePickerController *picker= [[UIImagePickerController alloc] init];
    picker.delegate = self;
    picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    picker.mediaTypes = [NSArray arrayWithObjects:(NSString *)kUTTypeMovie, (NSString *) kUTTypeAudio, (NSString*) kUTTypeImage, (NSString*) kUTTypeMP3, nil];
    
    [self presentViewController:picker animated:YES completion:nil];
}

-(void)onVideo{
    CSUploadVideoPreviewViewController* controller = [CSUploadVideoPreviewViewController new];
    [self.navigationController pushViewController:controller animated:YES];
}

-(void)onContact{
    ABPeoplePickerNavigationController *picker = [[ABPeoplePickerNavigationController alloc] init];
    picker.peoplePickerDelegate = self;
    [self presentViewController:picker animated:YES completion:nil];
}

-(void)onAudio {
    CSRecordAudioViewController* controller = [CSRecordAudioViewController new];
    [self.navigationController pushViewController:controller animated:YES];
}

#pragma mark - file picker
-(void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info{
    
    NSString* mediaType = [info objectForKey:UIImagePickerControllerMediaType];
    
    if(CFStringCompare ((__bridge CFStringRef) mediaType, kUTTypeImage, 0) == kCFCompareEqualTo){
        [self uploadImage:info];
    }else if (CFStringCompare ((__bridge CFStringRef) mediaType, kUTTypeMovie, 0) == kCFCompareEqualTo){
        [self uploadVideo:info];
    }else if (CFStringCompare ((__bridge CFStringRef) mediaType, kUTTypeVideo, 0) == kCFCompareEqualTo){
        [self uploadVideo:info];
    }else{
        [self uploadOtherFile:info];
    }
    
    [picker dismissViewControllerAnimated:YES completion:^{
    }];
    
}

-(void) uploadImage:(NSDictionary*) data{
    NSURL *imageUrl = (NSURL *)[data valueForKey:UIImagePickerControllerReferenceURL];
    NSString* extensions = [imageUrl pathExtension];
    NSString* mimeType;
    
    if([extensions isEqualToString:@"JPG"] || [extensions isEqualToString:@"jpg"]){
        mimeType = kAppImageJPG;
    }else if([extensions isEqualToString:@"PNG"] || [extensions isEqualToString:@"png"]){
        mimeType = kAppImagePNG;
    }else  if([extensions isEqualToString:@"GIF"] || [extensions isEqualToString:@"gif"]){
        mimeType = kAppImageGIF;
    }else{
        mimeType = kAppImageJPG;
    }
    
    NSString* fileName = [NSString stringWithFormat:@"%@_%ld.%@", @"image", (long)[[NSDate date] timeIntervalSince1970], extensions];
    
    UIImage * image = [data valueForKey:UIImagePickerControllerOriginalImage];
    
    CSUploadManager* uploadManager = [CSUploadManager new];
    [uploadManager uploadImage:image fileName:fileName mimeType:mimeType viewForLoading:self.view completition:^(id responseObject){
        
        NSNotification* note = [[NSNotification alloc] initWithName:kAppFileUploadedNotification object:nil userInfo:[NSDictionary dictionaryWithObject:responseObject forKey:paramResponseObject]];

        [self sendFileMessage:note];
        
    }];
}

-(void) uploadVideo:(NSDictionary*) data{
    NSURL *videoUrl = (NSURL *)[data valueForKey:UIImagePickerControllerMediaURL];
    NSString *path = [videoUrl path];
    NSData *videoData = [[NSFileManager defaultManager] contentsAtPath:path];
    
    NSString *mimeType = @"video/mp4";
    //    _fileName = [path lastPathComponent];
    NSString *fileName = [NSString stringWithFormat:@"%@_%ld.%@", @"video", (long)[[NSDate date] timeIntervalSince1970], @"mp4"];
    
    CSUploadManager* manager = [CSUploadManager new];
    [manager uploadFile:videoData fileName:fileName mimeType:mimeType viewForLoading:self.view completition:^(id responseObject){
        
        NSNotification* note = [[NSNotification alloc] initWithName:kAppFileUploadedNotification object:nil userInfo:[NSDictionary dictionaryWithObject:responseObject forKey:paramResponseObject]];
        
        [self sendFileMessage:note];
        
        
    }];
}

-(void) uploadOtherFile:(NSDictionary*) data{
    NSURL *videoUrl = (NSURL *)[data valueForKey:UIImagePickerControllerMediaURL];
    NSString *path = [videoUrl path];
    NSData *videoData = [[NSFileManager defaultManager] contentsAtPath:path];
    
    NSString *mimeType = @"application/octet-stream";
    //    _fileName = [path lastPathComponent];
    NSString *fileName = [NSString stringWithFormat:@"%ld_%@", (long)[[NSDate date] timeIntervalSince1970], [path lastPathComponent]];
    
    CSUploadManager* manager = [CSUploadManager new];
    [manager uploadFile:videoData fileName:fileName mimeType:mimeType viewForLoading:self.view completition:^(id responseObject){
        
        NSNotification* note = [[NSNotification alloc] initWithName:kAppFileUploadedNotification object:nil userInfo:[NSDictionary dictionaryWithObject:responseObject forKey:paramResponseObject]];
        
        [self sendFileMessage:note];
        
        
    }];
}

#pragma mark - socket delegate

-(void)socketDidReceiveNewMessage:(CSMessageModel *)message {
    dispatch_async(dispatch_get_main_queue(), ^{
        [self didReceiveNewMessage:message];
    });
}

-(void)socketDidReceiveTyping:(CSTypingModel *)typing{
    dispatch_async(dispatch_get_main_queue(), ^{
        [self didReceiveTyping:typing];
    });
}

-(void)socketDidReceiveUserLeft:(CSUserModel *)userLeft{
    dispatch_async(dispatch_get_main_queue(), ^{
         [self didUserLeft:userLeft];
    });
}

-(void)socketDidReceiveMessageUpdated:(NSArray *)updatedMessages{
    dispatch_async(dispatch_get_main_queue(), ^{
        [self didMessageUpdated:updatedMessages];
    });
}

-(void)socketDidReceiveError:(NSNumber *)errorCode {
    dispatch_async(dispatch_get_main_queue(), ^{
        [UIAlertView showWithTitle:@"Socket Error"
                           message:[CSChatErrorCodes errorForCode:errorCode]
                 cancelButtonTitle:@"OK"
                 otherButtonTitles:nil
                          tapBlock:nil
         ];
    });
}

-(void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    NSLog(@"dealloc: ChatViewController");
}

#pragma mark - adress book delegate

-(void)peoplePickerNavigationController:(ABPeoplePickerNavigationController *)peoplePicker didSelectPerson:(ABRecordRef)person {
    CFArrayRef peopleArray = CFArrayCreate(NULL, (void *)&person, 1, &kCFTypeArrayCallBacks);
    NSData *vCardData = (__bridge NSData*) ABPersonCreateVCardRepresentationWithPeople(peopleArray);
    NSString *vCard = [[NSString alloc] initWithData:vCardData encoding:NSUTF8StringEncoding];
    NSLog(@"vCard > %@", vCard );
    
    [self sendContact:vCard];
    
    [self.menuView animateHide];
}


-(void)peoplePickerNavigationControllerDidCancel:(ABPeoplePickerNavigationController *)peoplePicker {
    [self.menuView animateHide];
}

-(void)addContactToAddressBook:(NSString *)vCardString {
    
    [UIAlertView showWithTitle:NSLocalizedStringFromTable(@"Add to contacts", @"CSChatLocalization", nil)
                       message:NSLocalizedStringFromTable(@"Do you want add this contact to address book?", @"CSChatLocalization", nil)
             cancelButtonTitle:NSLocalizedString(@"Cancel", nil)
             otherButtonTitles:@[NSLocalizedString(@"OK", nil)]
                      tapBlock:^(UIAlertView * _Nonnull alertView, NSInteger buttonIndex) {
                          if ([[alertView buttonTitleAtIndex:buttonIndex] isEqualToString:NSLocalizedString(@"OK", nil)]) {
                              CFDataRef vCardData = (__bridge CFDataRef)[vCardString dataUsingEncoding:NSUTF8StringEncoding];
                              ABAddressBookRef book = ABAddressBookCreate();
                              ABRecordRef defaultSource = ABAddressBookCopyDefaultSource(book);
                              CFArrayRef vCardPeople = ABPersonCreatePeopleInSourceWithVCardRepresentation(defaultSource, vCardData);
                              for (CFIndex index = 0; index < CFArrayGetCount(vCardPeople); index++) {
                                  ABRecordRef person = CFArrayGetValueAtIndex(vCardPeople, index);
                                  ABAddressBookAddRecord(book, person, NULL);
                              }
                              
                              CFRelease(vCardPeople);
                              CFRelease(defaultSource);
                              ABAddressBookSave(book, NULL);
                              CFRelease(book);
                          }
                      }
     ];
}

@end
