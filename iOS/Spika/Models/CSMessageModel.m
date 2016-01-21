//
//  MessageModel.m
//  Prototype
//
//  Created by mislav on 14/07/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSMessageModel.h"

@implementation CSMessageModel

+(instancetype)createMessageWithUser:(CSUserModel *)user andMessage:(NSString *)message andType:(NSNumber*) type andFile: (CSFileModel*) file andLocation: (CSLocationModel*) location{

    CSMessageModel* messageForReturn = [[CSMessageModel alloc] init];
    
    messageForReturn.user = user;
    messageForReturn.userID = user.userID;
    messageForReturn.roomID = user.roomID;
    messageForReturn.type = type;
    messageForReturn.status = 1;
    messageForReturn.message = message;
    messageForReturn.localID = [messageForReturn generateLocalIDwithLength:32];
    
    if(file){
        messageForReturn.file = file;
    }
    
    if(location){
        messageForReturn.location = location;
    }
    
    return messageForReturn;

}

-(NSString*) generateLocalIDwithLength: (int) length{

    NSString* AB = @"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";
    
    NSMutableString *randomString = [NSMutableString stringWithCapacity: length];
    
    for (int i=0; i<length; i++) {
        [randomString appendFormat: @"%C", [AB characterAtIndex: arc4random_uniform([AB length])]];
    }
    
    return randomString;
}

-(void)updateMessageWithData:(CSMessageModel *)data{
    
    self.seenBy = data.seenBy;
    self.deleted = data.deleted;
    
}

@end
