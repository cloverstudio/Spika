//
//  TypingModel.h
//  Prototype
//
//  Created by Ivo Peric on 25/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CSModel.h"
#import "CSUserModel.h"

@interface CSTypingModel : CSModel

@property(nonatomic, strong) NSString* userID;
@property(nonatomic, strong) NSNumber* type;
@property(nonatomic, strong) NSString* roomID;
@property(nonatomic, strong) CSUserModel* user;

@end
