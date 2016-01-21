//
//  UserModel.h
//  Prototype
//
//  Created by mislav on 14/07/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSModel.h"

@interface CSUserModel : CSModel

@property (nonatomic, strong) NSString *userID;
@property (nonatomic, strong) NSString *roomID;
@property (nonatomic, strong) NSString *avatarURL;
@property (nonatomic, strong) NSString *name;
@property (nonatomic, strong) NSString *pushToken;

@end
