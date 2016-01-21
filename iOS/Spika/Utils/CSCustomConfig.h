//
//  CSCustomConfig.h
//  Spika
//
//  Created by mislav on 11/12/15.
//  Copyright © 2015 Clover Studio. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CSCustomConfig : NSObject

+ (instancetype)sharedInstance;
@property (nonatomic, strong) NSString *server_url;
@property (nonatomic, strong) NSString *socket_url;

@end
