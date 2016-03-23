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
#import "CSApiManager.h"

typedef void(^apiCallFinish)(CSResponseModel *responseModel);

@interface CSBaseViewController : UIViewController

@property (nonatomic, strong) CSUserModel *activeUser;

-(void) setSelfViewSizeFromViewController: (CGRect) frame;

-(void) showIndicator;
-(void) hideIndicator;

@end
