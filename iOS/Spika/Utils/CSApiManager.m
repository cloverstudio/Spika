//
//  CSApiManager.m
//  ios-v2-spika-enterprise
//
//  Created by mislav on 10/03/16.
//  Copyright © 2016 Clover Studio. All rights reserved.
//

#import "CSApiManager.h"
#import "AFHTTPRequestOperationManager.h"
#import "CSBaseViewController.h"
#import "CSChatErrorCodes.h"

@interface CSApiManager ()

@property (nonatomic, strong) AFHTTPRequestOperationManager *manager;

@end

@implementation CSApiManager

+ (instancetype)sharedManager {
    static CSApiManager *sharedManager = nil;
    
    @synchronized(self) {
        if (sharedManager == nil) {
            sharedManager = [[self alloc] init];
        }
    }
    return sharedManager;
}

- (id)init {
    if (self = [super init]) {
        // init code
        _manager = [AFHTTPRequestOperationManager manager];
        _manager.responseSerializer = [AFJSONResponseSerializer serializer];
        _manager.requestSerializer = [AFJSONRequestSerializer serializer];
        [_manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
        [_manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    }
    return self;
}

-(void)setAccessToken:(NSString *)accessToken {
    _accessToken = accessToken;
    [self.manager.requestSerializer setValue:_accessToken forHTTPHeaderField:@"access-token"];
}

-(void) apiGETCallWithURL:(NSString*)url completition:(apiCallFinish)finish {
    [self apiGETCallWithURL:url indicatorVC:nil toShowIndicator:NO toHideIndicator:NO completition:finish];
}

-(void) apiGETCallWithURL:(NSString*)url indicatorVC:(CSBaseViewController*)vc completition:(apiCallFinish)finish{
    [self apiGETCallWithURL:url indicatorVC:vc toShowIndicator:YES toHideIndicator:YES completition:finish];
}

-(void) apiGETCallWithURL:(NSString*)url
              indicatorVC:(CSBaseViewController*)vc
          toShowIndicator:(BOOL)toShow
          toHideIndicator:(BOOL)toHide
             completition:(apiCallFinish)finish {
    
    if(toShow) {
        [vc showIndicator];
    }
        
    [self.manager GET:url
           parameters:nil
              success:^(AFHTTPRequestOperation *operation, id responseObject) {
                  
                  if(toHide){
                      [vc hideIndicator];
                  }
                  
                  CSResponseModel *responseModel = [[CSResponseModel alloc] initWithDictionary:responseObject error:nil];
                  
                  if (responseModel.code.intValue > 1) {
                      NSLog(@"fail client");
                      UIAlertView* alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                                      message:[CSChatErrorCodes errorForCode:responseModel.code]
                                                                     delegate:nil
                                                            cancelButtonTitle:@"OK"
                                                            otherButtonTitles:nil, nil];
                      [alert show];
                      return;
                  }
                  NSLog(@"success %@", url);
                  
                  finish(responseModel);
                  
              } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
                  [vc hideIndicator];
                  
                  UIAlertView* alert = [[UIAlertView alloc] initWithTitle:@"Error" message:error.localizedDescription delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                  [alert show];
                  NSLog(@"fail %@", url);
              }];
}

-(void) apiPOSTCallWithUrl:(NSString*)url parameters:(NSDictionary*)params completition:(apiCallFinish)finish {
    [self apiPOSTCallWithUrl:url parameters:params indicatorVC:nil toShowIndicator:NO toHideIndicator:NO completition:finish];
}

-(void) apiPOSTCallWithUrl:(NSString*)url parameters:(NSDictionary*)params indicatorVC:(CSBaseViewController*)vc completition:(apiCallFinish)finish {
    [self apiPOSTCallWithUrl:url parameters:params indicatorVC:vc toShowIndicator:NO toHideIndicator:NO completition:finish];
}

-(void) apiPOSTCallWithUrl:(NSString*)url
                parameters:(NSDictionary*)params
               indicatorVC:(CSBaseViewController*)vc
           toShowIndicator:(BOOL)toShow
           toHideIndicator:(BOOL)toHide
              completition:(apiCallFinish)finish {
    
    if (toShow) {
        [vc showIndicator];
    }
    
    [self.manager POST:url
            parameters:params
               success:^(AFHTTPRequestOperation *operation, id responseObject) {
                   
                   if (toHide) {
                       [vc hideIndicator];
                   }
                   
                   CSResponseModel *responseModel = [[CSResponseModel alloc] initWithDictionary:responseObject error:nil];
                   
                   if (responseModel.code.intValue > 1) {
                       NSLog(@"fail client");
                       UIAlertView* alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                                       message:[CSChatErrorCodes errorForCode:responseModel.code]
                                                                      delegate:nil
                                                             cancelButtonTitle:@"OK"
                                                             otherButtonTitles:nil, nil];
                       [alert show];
                       return;
                   }
                   NSLog(@"success %@", url);
                   
                   finish(responseModel);
                   
                   
               } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
                   
                   [vc hideIndicator];
                   
                   UIAlertView* alert = [[UIAlertView alloc] initWithTitle:@"Error" message:error.localizedDescription delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                   [alert show];
                   
                   NSLog(@"fail %@", url);
               }];
}

@end
