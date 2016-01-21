//
//  SeenByModel.h
//  Prototype
//
//  Created by Ivo Peric on 31/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSUserModel.h"

@interface CSSeenByModel : CSModel

@property (nonatomic, strong) NSNumber* at;
@property (nonatomic, strong) CSUserModel* user;

@end
