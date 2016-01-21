//
//  SocketController.m
//  SpikaV2
//
//  Created by mislav on 07/12/15.
//  Copyright © 2015 Clover Studio. All rights reserved.
//

#import "CSSocketController.h"
#import "CSConfig.h"
#import "CSCustomConfig.h"

@interface CSSocketController()

@property (nonatomic, strong) SIOSocket *socket;
@property (nonatomic, strong) NSMutableArray *queue;
@property (nonatomic) BOOL isConnected;
@property (nonatomic) BOOL isConnecting;

@end

@implementation CSSocketController

+ (instancetype)sharedController {
    static CSSocketController *sharedController = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedController = [[self alloc] init];
    });
    
    if (!sharedController.isConnected && !sharedController.isConnecting) {
        [sharedController connect];
    }
    
    return sharedController;
}

- (id)init {
    if (self = [super init]) {
        // init code
        self.isConnected = NO;
        self.isConnecting = NO;
        self.queue = [NSMutableArray new];
    }
    return self;
}

- (void)connect {
    self.isConnecting = YES;
    [SIOSocket socketWithHost:[CSCustomConfig sharedInstance].socket_url response: ^(SIOSocket *socket) {
        
        [socket setOnConnect:^{
            self.isConnected = YES;
            self.isConnecting = NO;
            NSLog(@"connect socket");
            
            for (void (^block)(void) in self.queue) {
                block();
            }
            
            [self.queue removeAllObjects];
        }];
        
        [socket setOnError:^(NSDictionary *error) {
            NSLog(@"Error socket %@", error);
        }];
        
        [socket setOnDisconnect:^{
            self.isConnected = NO;
            NSLog(@"disconnect socket");
        }];
        
        [socket setOnReconnect:^(NSInteger number) {
            NSLog(@"reconnect socket: %d", (int)number);
        }];
        
        [socket setOnReconnectionAttempt:^(NSInteger number) {
            NSLog(@"reconnect attempt socket: %d", (int)number);
        }];
        
        [socket setOnReconnectionError:^(NSDictionary *error) {
            NSLog(@"reconnect error socket: %@", error);
        }];
        
        self.socket = socket;
    }];
}

- (void)registerForChat:(NSDictionary *)parametersLogin withChatSocketDelegate:(id<CSChatSocketControllerDelegate>) delegate {
    
    __weak id<CSChatSocketControllerDelegate> weakDelegate = delegate;
    
    void (^block)(void) = ^{
        NSLog(@"registerForChat block execute after connection");
        
        NSArray *array = [NSArray arrayWithObject:parametersLogin];
        
        [self.socket emit:kAppSocketLogin args:array];
        
        [self.socket on:kAppSocketNewMessage callback:^(SIOParameterArray *args){
            
            NSDictionary *response = [args firstObject];
            
            NSError *error;
            CSMessageModel *message;
            message = [[CSMessageModel alloc] initWithDictionary:response error:&error];
            
            [weakDelegate socketDidReceiveNewMessage:message];
        }];
        
        [self.socket on:kAppSocketSendTyping callback:^(SIOParameterArray *args){
            
            NSDictionary *response = [args firstObject];
            
            NSError *error;
            CSTypingModel *typing;
            typing = [[CSTypingModel alloc] initWithDictionary:response error:&error];
            
            [weakDelegate socketDidReceiveTyping:typing];
        }];
        
        [self.socket on:kAppSocketUserLeft callback:^(SIOParameterArray *args){
            
            NSDictionary *response = [args firstObject];
            
            NSError *error;
            CSUserModel *userLeft;
            userLeft = [[CSUserModel alloc] initWithDictionary:response error:&error];
            
            [weakDelegate socketDidReceiveUserLeft:userLeft];
        }];
        
        [self.socket on:kAppSocketMessageUpdated callback:^(SIOParameterArray *args){
            
            NSDictionary *response = [args firstObject];
            
            NSError *error;
            NSMutableArray* updatedMessages = [NSMutableArray new];
            
            for(NSDictionary *dict in response){
                CSMessageModel* message = [[CSMessageModel alloc] initWithDictionary:dict error:&error];
                [updatedMessages addObject:message];
            }
            
            [weakDelegate socketDidReceiveMessageUpdated:updatedMessages];
        }];
        
        [self.socket on:kAppSocketError callback:^(SIOParameterArray *args) {
            
            NSDictionary *response = [args firstObject];
            [weakDelegate socketDidReceiveError:response[@"code"]];
        }];
    };
    
    if (!self.isConnected) {
        [self.queue addObject:block];
    }
    else {
        block();
    }
}

-(void)emit:(NSString *)event {
    [self.socket emit:event];
}

-(void)emit:(NSString *)event args:(SIOParameterArray *)args {
    [self.socket emit:event args:args];
}

-(void)close {
    NSLog(@"close socket");
    self.isConnected = NO;
    [self.socket close];
}

- (void)dealloc {
    // Should never be called, but just here for clarity really.
}

@end
