//
//  ResponseModel.h
//  Prototype
//
//  Created by mislav on 23/07/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSModel.h"

@interface CSResponseModel : CSModel

@property NSDictionary *data;
@property NSNumber *code;

@end
