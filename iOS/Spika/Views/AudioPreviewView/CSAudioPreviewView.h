//
//  AudioPreviewView.h
//  Prototype
//
//  Created by Ivo Peric on 02/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CSMessageModel.h"
#import "CSBasePreviewView.h"
#import <AudioToolbox/AudioToolbox.h>
#import <AVFoundation/AVFoundation.h>

@interface CSAudioPreviewView : CSBasePreviewView

@property (nonatomic, strong) NSString* filePath;
@property (nonatomic, strong) AVAudioPlayer* audioPlayer;
@property (nonatomic, strong) NSTimer* updateTimer;

- (IBAction)onPlayClicked:(id)sender;
- (IBAction)onCloseView:(id)sender;
- (IBAction)seekTime:(id)sender;
@property (weak, nonatomic) IBOutlet UIButton *closeButton;
@property (weak, nonatomic) IBOutlet UIView *backgroundView;
@property (weak, nonatomic) IBOutlet UIButton *playButton;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet UISlider *slider;
@property (weak, nonatomic) IBOutlet UILabel *fileName;

@end
