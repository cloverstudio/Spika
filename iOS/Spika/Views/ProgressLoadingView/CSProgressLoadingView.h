//
//  ProgressLoadingView.h
//  Prototype
//
//  Created by Ivo Peric on 01/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CSProgressLoadingView : UIView

@property (weak, nonatomic) IBOutlet UIProgressView *progressView;
@property (weak, nonatomic) IBOutlet UILabel *progressLabel;
@property (weak, nonatomic) IBOutlet UILabel *downloadingLabel;
@property (weak, nonatomic) IBOutlet UIView *backgroundView;

-(void) changeProgressViewOnMainThread: (NSString*) progress;
-(void) changeProgressLabelOnMainThread: (NSString*) progress max: (NSString*) max;

@end
