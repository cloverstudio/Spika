//
//  CSCustomConfig.m
//  Spika
//
//  Created by mislav on 11/12/15.
//  Copyright © 2015 Clover Studio. All rights reserved.
//

#import "CSCustomConfig.h"
#import "CSConfig.h"

@implementation CSCustomConfig

+ (instancetype)sharedInstance {
    static CSCustomConfig *sharedInstance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

- (id)init {
    if (self = [super init]) {
        _server_url = kAppBaseUrl;
        _socket_url = kAppSocketURL;
    }
    return self;
}


@end
