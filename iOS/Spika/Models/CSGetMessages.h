//
//  GetMessages.h
//  Prototype
//
//  Created by Ivo Peric on 24/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CSModel.h"
#import "CSMessageModel.h"

@interface CSGetMessages : CSModel

@property(nonatomic, strong) NSArray<CSMessageModel> *messages;

@end
