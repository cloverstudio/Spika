//
//  LoginResult.h
//  Prototype
//
//  Created by Ivo Peric on 24/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSModel.h"
#import "CSUserModel.h"
#import <Foundation/Foundation.h>

@interface CSLoginResult : CSModel

@property (nonatomic, strong) NSString* token;
@property (nonatomic, strong) CSUserModel* user;

@end
