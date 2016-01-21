//
//  SocketController.h
//  SpikaV2
//
//  Created by mislav on 07/12/15.
//  Copyright © 2015 Clover Studio. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SIOSocket.h"
#import "CSMessageModel.h"
#import "CSTypingModel.h"

@protocol CSChatSocketControllerDelegate;

@interface CSSocketController : NSObject

+ (instancetype)sharedController;

- (void)registerForChat:(NSDictionary *)parametersLogin withChatSocketDelegate:(id<CSChatSocketControllerDelegate>) delegate;
- (void)emit:(NSString *)event;
- (void)emit:(NSString *)event args:(SIOParameterArray *)args;
- (void)close;

@end

@protocol CSChatSocketControllerDelegate <NSObject>

-(void)socketDidReceiveNewMessage:(CSMessageModel *)message;
-(void)socketDidReceiveTyping:(CSTypingModel *)typing;
-(void)socketDidReceiveUserLeft:(CSUserModel *)userLeft;
-(void)socketDidReceiveMessageUpdated:(NSArray *)updatedMessages;
-(void)socketDidReceiveError:(NSNumber *)errorCode;

@end