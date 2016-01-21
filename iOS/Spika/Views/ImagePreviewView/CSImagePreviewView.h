//
//  ImagePreviewView.h
//  Prototype
//
//  Created by Ivo Peric on 01/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CSConfig.h"
#import "CSBasePreviewView.h"

@interface CSImagePreviewView : CSBasePreviewView

@property (weak, nonatomic) IBOutlet UIButton *closeButton;
@property (weak, nonatomic) IBOutlet UIView *backGroundView;
@property (weak, nonatomic) IBOutlet UIImageView *image;
- (IBAction)onClose:(id)sender;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *loadingIndicator;

@end
