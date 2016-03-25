//
//  ChatViewController.h
//  Prototype
//
//  Created by Ivo Peric on 28/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CSUserModel.h"
#import "CSChatSettingsView.h"
#import "CSBaseViewController.h"
#import "CSMenuView.h"
#import "CSStickerView.h"

@interface CSChatViewController : CSBaseViewController

-(instancetype)initWithParameters:(NSDictionary *)parameters;

@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UIButton *sendButton;
@property (weak, nonatomic) IBOutlet UITextField *etMessage;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottomMargin;
@property (weak, nonatomic) IBOutlet CSChatSettingsView *chatsettingsView;
@property (weak, nonatomic) IBOutlet UIView *footerView;

- (IBAction)changedTextInTextField:(id)sender;
- (IBAction)sendMessage:(id)sender;
- (IBAction)onStickerButton:(id)sender;

@property (nonatomic, strong) NSString* token;
@property (nonatomic, strong) NSMutableArray* messages;
@property (nonatomic, strong) NSMutableArray* tempSentMessagesLocalId;
@property (nonatomic, strong) NSMutableArray* typingUsers;
@property (nonatomic, strong) NSMutableArray* lastDataLoadedFromNet;
@property (nonatomic) BOOL isTyping;

- (IBAction)changeEditing:(id)sender;

@property (retain)UIDocumentInteractionController *documentController;
@property (nonatomic, strong) CSMenuView *menuView;
@property (nonatomic, strong) CSStickerView *stickerView;

@end
