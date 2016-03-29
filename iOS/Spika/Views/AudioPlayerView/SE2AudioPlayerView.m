//
//  SE2AudioPlayerView.m
//  ios-v2-spika-enterprise
//
//  Created by CloverField on 10/03/16.
//  Copyright © 2016 Clover Studio. All rights reserved.
//

#import "SE2AudioPlayerView.h"
#import "CSUtils.h"
#import "CSConfig.h"
#import "CSDownloadManager.h"
#import <AudioToolbox/AudioToolbox.h>
#import <AVFoundation/AVFoundation.h>

@interface SE2AudioPlayerView () <AVAudioPlayerDelegate>

@property (strong, nonatomic) SE2MessageModel *message;
@property ( nonatomic) CGRect rect;
@property (nonatomic, strong) NSString* filePath;
@property (nonatomic, strong) AVAudioPlayer* audioPlayer;
@property (nonatomic, strong) NSTimer* updateTimer;
@property (nonatomic, strong) NSString *url;

@end

@implementation SE2AudioPlayerView

-(id)initWithFrame:(CGRect)frame url:(NSString*)url
{
    if (self = [super initWithFrame:frame]) {
        
        self = [[[NSBundle mainBundle] loadNibNamed:[NSString stringWithFormat:@"%@", [self class]] owner:self options:nil] objectAtIndex:0];
        _url = url;
        _rect = frame;
        [self initWithNib];
    }
    return self;
}

- (IBAction)onViewInChat:(id)sender
{
    if ([_delegate respondsToSelector:@selector(viewInChatSelected)]) {
        [_delegate viewInChatSelected];
    }
}

- (void)layoutSubviews {
    [super layoutSubviews];
    self.frame = _rect;
}

-(void)initWithNib
{
    self.layer.cornerRadius = 5.0f;
    self.layer.borderWidth = 1.0f;
    self.layer.borderColor = kAppDefaultColor(1).CGColor;
    self.clipsToBounds = YES;
    
    self.playButton.alpha = 0;
    
    self.durationHolderView.layer.cornerRadius = self.durationHolderView.frame.size.height/2;
    self.durationHolderView.clipsToBounds = YES;
    self.durationHolderView.backgroundColor = kAppGrayLight(1);
    
    [self.durationSlider setMinimumTrackTintColor:[UIColor lightGrayColor]];
    [self.durationSlider setMaximumTrackTintColor:kAppGrayLight(1)];
    self.durationLabel.textColor = [UIColor grayColor];
    
    self.separator.backgroundColor = kAppGrayLight(1);
    
    _filePath = _url;
            
    [self initAudio];
}

- (void) initAudio
{
    [UIView animateWithDuration:0.2 animations:^{
        self.playButton.alpha = 1;
    }completion:^(BOOL finished) {
    }];

    NSURL* soundFile = [NSURL fileURLWithPath:_filePath];
    _audioPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:soundFile error:nil];
    _audioPlayer.delegate = self;
    
    self.durationSlider.maximumValue = self.audioPlayer.duration;
    
}

- (IBAction)onPlay:(id)sender
{
    if([_audioPlayer isPlaying]){
        [self pausePlaying];
    }else{
        [self startPlaying];
    }
}

- (IBAction)onSlider:(id)sender
{
    self.audioPlayer.currentTime = self.durationSlider.value;
    
    int currentTime = (int) self.audioPlayer.currentTime;
    NSString *time = [NSString stringWithFormat:@"%01d:%02d", currentTime / 60, currentTime % 60];
    self.durationLabel.text = time;

}

-(void)audioPlayerDidFinishPlaying:(AVAudioPlayer *)player successfully:(BOOL)flag{
    
    [self stopPlaying];
    
}

-(void)pausePlaying
{
    [_audioPlayer pause];
    [_updateTimer invalidate];
    _updateTimer = nil;
    self.playButton.selected = NO;
}

-(void) stopPlaying{
    
    [_audioPlayer stop];
    self.durationLabel.text = @"0:00";
    self.durationSlider.value = 0;
    [self.updateTimer invalidate];
    self.updateTimer = nil;
    self.playButton.selected = NO;
}

-(void) startPlaying{
    
    [_audioPlayer play];
    self.updateTimer = [NSTimer scheduledTimerWithTimeInterval:0.1 target:self selector:@selector(updateSlider) userInfo:nil repeats:YES];
    self.playButton.selected = YES;
}

- (void)updateSlider{
    float progress = self.audioPlayer.currentTime;
    [self.durationSlider setValue:progress];
    
    int currentTime = (int) progress;
    NSString *time = [NSString stringWithFormat:@"%01d:%02d", currentTime / 60, currentTime % 60];
    self.durationLabel.text = time;
}

@end
