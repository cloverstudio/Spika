//
//  VideoPreviewView.h
//  Prototype
//
//  Created by Ivo Peric on 01/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CSConfig.h"
#import <MediaPlayer/MediaPlayer.h>
#import "CSBasePreviewView.h"

@interface CSVideoPreviewView : CSBasePreviewView

- (IBAction)onCloseView:(id)sender;
@property (weak, nonatomic) IBOutlet UIButton *closeButton;
@property (weak, nonatomic) IBOutlet UIView *backGroundView;

@property (nonatomic, strong) NSString* filePath;

@property (nonatomic, strong) MPMoviePlayerController* moviePlayer;

@end
