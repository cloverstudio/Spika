//
//  BaseViewController.h
//  Prototype
//
//  Created by Ivo Peric on 27/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AFNetworking/AFNetworking.h>
#import "CSUtils.h"
#import "CSResponseModel.h"

typedef void(^apiCallFinish)(CSResponseModel *responseModel);

@interface CSBaseViewController : UIViewController

@property (nonatomic, strong) AFHTTPRequestOperationManager *manager;
@property (nonatomic, strong) CSUserModel *activeUser;

-(void) apiGETCallWithURL: (NSString*) url completition: (apiCallFinish) finish;
-(void) apiGETCallWithURL: (NSString*) url toShowIndicator: (BOOL) toShow toHideIndicator: (BOOL) toHide completition: (apiCallFinish) finish;
-(void) apiPOSTCallWithUrl: (NSString*) url parameters: (NSDictionary*) params completition: (apiCallFinish) finish;
-(void) apiPOSTCallWithUrl: (NSString*) url parameters: (NSDictionary*) params toShowIndicator: (BOOL) toShow toHideIndicator: (BOOL) toHide completition: (apiCallFinish) finish;
-(void) setSelfViewSizeFromViewController: (CGRect) frame;
@end
