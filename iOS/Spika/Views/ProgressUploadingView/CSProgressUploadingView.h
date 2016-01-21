//
//  ProgressUploadingView.h
//  Prototype
//
//  Created by Ivo Peric on 07/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CSProgressUploadingView : UIView

@property (weak, nonatomic) IBOutlet UIProgressView *progressView;
@property (weak, nonatomic) IBOutlet UILabel *progressLabel;
@property (weak, nonatomic) IBOutlet UILabel *uploadingLabel;
@property (weak, nonatomic) IBOutlet UIView *backgroundView;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *loadingIndicator;

-(void) changeProgressLabel: (NSString*) progress max: (NSString*) max;
-(void) changeProgressView: (NSString*) progress;
-(void) chageLabelToWaiting;

@end
