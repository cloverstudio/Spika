//
//  MessageModel.h
//  Prototype
//
//  Created by mislav on 14/07/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSModel.h"
#import "CSFileModel.h"
#import "CSUserModel.h"
#import "CSLocationModel.h"

@protocol CSMessageModel
@end

@interface CSMessageModel : CSModel

@property (nonatomic, strong) NSString *_id;
@property (nonatomic, strong) NSString *userID;
@property (nonatomic, strong) NSString *roomID;
@property (nonatomic, strong) CSUserModel *user;
@property (nonatomic, strong) NSNumber *type;
@property (nonatomic, strong) NSString *message;
@property (nonatomic, strong) NSNumber *created;
@property (nonatomic, strong) CSFileModel *file;
@property (nonatomic, strong) NSString *localID;
@property (nonatomic, strong) CSLocationModel *location;
@property (nonatomic, strong) NSArray *seenBy;
@property (nonatomic, strong) NSNumber *deleted;

// local property
@property (nonatomic) NSInteger status;

+(instancetype)createMessageWithUser:(CSUserModel *)user andMessage:(NSString *)message andType:(NSNumber*) type andFile: (CSFileModel*) file andLocation: (CSLocationModel*) location;
-(void) updateMessageWithData: (CSMessageModel*) data;

@end
