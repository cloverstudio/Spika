//
//  ProgressUploadingView.m
//  Prototype
//
//  Created by Ivo Peric on 07/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSProgressUploadingView.h"
#import "CSConfig.h"
#import "CSUtils.h"

@implementation CSProgressUploadingView

-(instancetype)init{
    
    if (self = [super init]) {
        [self initializeView];
    }
    return self;
    
}

-(void)initializeView{
    
    CGRect viewRect = [[UIScreen mainScreen] bounds];
    
    NSString *className = NSStringFromClass([self class]);
    className = [className stringByReplacingOccurrencesOfString:[[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleExecutable"] withString:@""];
    className = [className stringByReplacingOccurrencesOfString:@"." withString:@""];
    
    NSString *path = [[NSBundle mainBundle] pathForResource:className ofType:@"nib"];
    if (![[NSFileManager defaultManager] fileExistsAtPath:path]) {
        return;
    }
    
    NSArray *array = [[NSBundle mainBundle] loadNibNamed:className
                                                   owner:self
                                                 options:nil];
    NSAssert(array.count == 1, @"Invalid number of nibs");
    
    self.frame = viewRect;
    UIView* backGR = array[0];
    backGR.frame = viewRect;
    [self addSubview:backGR];
    
    
    self.uploadingLabel.textColor = kAppDefaultColor(1);
    self.progressLabel.textColor = kAppDefaultColor(1);
    
    self.progressView.progress = 0.0;
    
    self.backgroundView.layer.cornerRadius = 10;
    self.backgroundView.layer.masksToBounds = YES;
    
    self.loadingIndicator.color = kAppDefaultColor(1);
    self.loadingIndicator.hidden = YES;
    
}

-(void) changeProgressLabel: (NSString*) progress max: (NSString*) max{
    
    self.progressLabel.text = [NSString stringWithFormat:@"%@/%@", [CSUtils readableFileSize:progress], [CSUtils readableFileSize:max]];
    
}

-(void) changeProgressView: (NSString*) progress{
    
    self.progressView.progress = [progress floatValue];
    
}

-(void) chageLabelToWaiting{
    
    self.progressView.hidden = YES;
    self.loadingIndicator.hidden = NO;
    [self.loadingIndicator startAnimating];
    self.progressLabel.text = @"Waiting to response";
    
}

@end
