//
//  CSChatErrorCodes.m
//  Spika
//
//  Created by mislav on 20/01/16.
//  Copyright © 2016 Clover Studio. All rights reserved.
//

#import "CSChatErrorCodes.h"

@implementation CSChatErrorCodes

+ (NSString *)errorForCode:(NSNumber *)code {
    
    static NSDictionary *codesDictionary = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        codesDictionary = @{
                 @1000001 : NSLocalizedStringFromTable(@"Login No Name", @"CSChatErrorCodes", nil),
                 @1000002 : NSLocalizedStringFromTable(@"Login No Room ID", @"CSChatErrorCodes", nil),
                 @1000003 : NSLocalizedStringFromTable(@"Login No User ID", @"CSChatErrorCodes", nil),
                 @1000004 : NSLocalizedStringFromTable(@"User List No Room ID", @"CSChatErrorCodes", nil),
                 @1000005 : NSLocalizedStringFromTable(@"Message List No Room ID", @"CSChatErrorCodes", nil),
                 @1000006 : NSLocalizedStringFromTable(@"Message List No Last Message ID", @"CSChatErrorCodes", nil),
                 @1000007 : NSLocalizedStringFromTable(@"Send Message No File", @"CSChatErrorCodes", nil),
                 @1000008 : NSLocalizedStringFromTable(@"Send Message No Room ID", @"CSChatErrorCodes", nil),
                 @1000009 : NSLocalizedStringFromTable(@"Send Message No User ID", @"CSChatErrorCodes", nil),
                 @1000010 : NSLocalizedStringFromTable(@"Send Message No Type", @"CSChatErrorCodes", nil),
                 @1000011 : NSLocalizedStringFromTable(@"File Upload No File", @"CSChatErrorCodes", nil),
                 @1000012 : NSLocalizedStringFromTable(@"Socket Unknown Error", @"CSChatErrorCodes", nil),
                 @1000013 : NSLocalizedStringFromTable(@"Socket Delete Message No User ID", @"CSChatErrorCodes", nil),
                 @1000014 : NSLocalizedStringFromTable(@"Socket Delete Message No Message ID", @"CSChatErrorCodes", nil),
                 @1000015 : NSLocalizedStringFromTable(@"Socket Send Message No Room ID", @"CSChatErrorCodes", nil),
                 @1000016 : NSLocalizedStringFromTable(@"Socket Send Message No User Id", @"CSChatErrorCodes", nil),
                 @1000017 : NSLocalizedStringFromTable(@"Socket Send Message No Type", @"CSChatErrorCodes", nil),
                 @1000018 : NSLocalizedStringFromTable(@"Socket Send Message No Message", @"CSChatErrorCodes", nil),
                 @1000019 : NSLocalizedStringFromTable(@"Socket Send Message No Location", @"CSChatErrorCodes", nil),
                 @1000020 : NSLocalizedStringFromTable(@"Socket Send Message Fail", @"CSChatErrorCodes", nil),
                 @1000021 : NSLocalizedStringFromTable(@"Socket Typing No User ID", @"CSChatErrorCodes", nil),
                 @1000022 : NSLocalizedStringFromTable(@"Socket Typing No Room ID", @"CSChatErrorCodes", nil),
                 @1000023 : NSLocalizedStringFromTable(@"Socket Typing No Type", @"CSChatErrorCodes", nil),
                 @1000024 : NSLocalizedStringFromTable(@"Socket Typing Faild", @"CSChatErrorCodes", nil),
                 @1000025 : NSLocalizedStringFromTable(@"Socket Login No User ID", @"CSChatErrorCodes", nil),
                 @1000026 : NSLocalizedStringFromTable(@"Socket Login No Room ID", @"CSChatErrorCodes", nil)
                 };
    });
    
    return [codesDictionary objectForKey:code];
}

@end
