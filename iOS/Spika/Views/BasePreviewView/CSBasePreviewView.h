//
//  BasePreviewView.h
//  Prototype
//
//  Created by Ivo Peric on 02/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CSMessageModel.h"

typedef void(^dismissPreview)(void);

@interface CSBasePreviewView : UIView

-(void) initializeViewWithMessage: (CSMessageModel*) message navigationAndStatusBarHeight:(float) size dismiss: (dismissPreview) dismiss;
@property (nonatomic, strong) dismissPreview dismiss;
@property (nonatomic, strong) CSMessageModel* message;

@end
