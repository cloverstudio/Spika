//
//  ProgressLoadingView.m
//  Prototype
//
//  Created by Ivo Peric on 01/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSProgressLoadingView.h"
#import "CSConfig.h"
#import "CSUtils.h"

@implementation CSProgressLoadingView

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


    self.downloadingLabel.textColor = kAppDefaultColor(1);
    self.progressLabel.textColor = kAppDefaultColor(1);
    
    self.progressView.progress = 0.0;
    
    self.backgroundView.layer.cornerRadius = 10;
    self.backgroundView.layer.masksToBounds = YES;

}

-(void) changeProgressLabelOnMainThread: (NSString*) progress max: (NSString*) max{
    
    NSArray *array = [NSArray arrayWithObjects:progress, max, nil];
    [self performSelectorOnMainThread:@selector(changeProgressLabel:) withObject:array waitUntilDone:NO];
    
}

-(void) changeProgressLabel:(NSArray*) progressArray{

    self.progressLabel.text = [NSString stringWithFormat:@"%@/%@", [CSUtils readableFileSize:progressArray[0]], [CSUtils readableFileSize:progressArray[1]]];

}

-(void) changeProgressViewOnMainThread: (NSString*) progress{

    [self performSelectorOnMainThread:@selector(changeProgressValue:) withObject:progress waitUntilDone:NO];

}

-(void) changeProgressValue:(NSString*) progress{

    self.progressView.progress = [progress floatValue];

}

@end
