//
//  CSApiManager.h
//  ios-v2-spika-enterprise
//
//  Created by mislav on 10/03/16.
//  Copyright © 2016 Clover Studio. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CSResponseModel.h"
@class CSBaseViewController;

typedef void(^apiCallFinish)(CSResponseModel *responseModel);

@interface CSApiManager : NSObject

@property (nonatomic, strong) NSString *accessToken;

+ (instancetype)sharedManager;

-(void) apiGETCallWithURL:(NSString*)url completition:(apiCallFinish)finish;
-(void) apiGETCallWithURL:(NSString*)url indicatorVC:(CSBaseViewController*)vc completition:(apiCallFinish)finish;
-(void) apiGETCallWithURL:(NSString*)url
              indicatorVC:(CSBaseViewController*)vc
          toShowIndicator:(BOOL)toShow
          toHideIndicator:(BOOL)toHide
             completition:(apiCallFinish)finish;

-(void) apiPOSTCallWithUrl:(NSString*)url parameters:(NSDictionary*)params completition:(apiCallFinish)finish;
-(void) apiPOSTCallWithUrl:(NSString*)url parameters:(NSDictionary*)params indicatorVC:(CSBaseViewController*)vc completition:(apiCallFinish)finish;
-(void) apiPOSTCallWithUrl:(NSString*)url
                parameters:(NSDictionary*)params
               indicatorVC:(CSBaseViewController*)vc
           toShowIndicator:(BOOL)toShow
           toHideIndicator:(BOOL)toHide
              completition:(apiCallFinish)finish;
@end
