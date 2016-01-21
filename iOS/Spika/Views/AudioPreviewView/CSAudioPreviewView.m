//
//  AudioPreviewView.m
//  Prototype
//
//  Created by Ivo Peric on 02/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSAudioPreviewView.h"
#import "CSUtils.h"
#import "CSConfig.h"
#import "CSDownloadManager.h"

@interface CSAudioPreviewView() <AVAudioPlayerDelegate>

@end

@implementation CSAudioPreviewView

-(void) initializeViewWithMessage: (CSMessageModel*) message navigationAndStatusBarHeight:(float) size dismiss: (dismissPreview) dismiss{
    
    self.dismiss = dismiss;
    self.message = message;
    
    CGRect viewRect = [[UIScreen mainScreen] bounds];
    viewRect.size.height = viewRect.size.height - size;
    
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
    
    self.closeButton.layer.cornerRadius = self.closeButton.frame.size.width / 2;
    self.closeButton.layer.masksToBounds = YES;
    self.closeButton.backgroundColor = kAppDefaultColor(0.8);
    
    self.timeLabel.textColor = kAppDefaultColor(0.8);
    self.fileName.textColor = kAppDefaultColor(0.8);
    
    self.backgroundView.layer.cornerRadius = 10;
    self.backgroundView.layer.masksToBounds = YES;
    
    self.fileName.text = self.message.file.file.name;
    self.timeLabel.text = @"00:00";
    
    _filePath = [CSUtils getFileFromFileModel:message.file];
    
    self.playButton.enabled = NO;
    self.slider.minimumValue = 0;
    self.slider.value = 0;
    
    if (![CSUtils isFileExistsWithFileName:_filePath]) {
        
        CSDownloadManager* downloadManager = [CSDownloadManager new];
        [downloadManager downloadFileWithUrl:[NSURL URLWithString:[CSUtils generateDownloadURLFormFileId:message.file.file.id]] destination:[NSURL URLWithString:_filePath] viewForLoading:self completition:^(BOOL success){
            
            [self initAudio];
            
        }];
    }else{
        
        [self initAudio];
        
    }
    
}

- (void) initAudio{

    NSURL* soundFile = [NSURL fileURLWithPath:_filePath];
    _audioPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:soundFile error:nil];
    _audioPlayer.delegate = self;
    
    self.slider.maximumValue = self.audioPlayer.duration;
    self.playButton.enabled = YES;
    
}

- (IBAction)onPlayClicked:(id)sender {
    if([_audioPlayer isPlaying]){
        [self stopPlaying];
    }else{
        [self startPlaying];
    }
}

-(void)audioPlayerDidFinishPlaying:(AVAudioPlayer *)player successfully:(BOOL)flag{

    [self stopPlaying];
    
}

-(void) stopPlaying{

    [_audioPlayer stop];
    [self.playButton setImage:[UIImage imageNamed:@"play"] forState:UIControlStateNormal];
    self.timeLabel.text = @"00:00";
    self.slider.value = 0;
    [self.updateTimer invalidate];
    self.updateTimer = nil;
    
}

-(void) startPlaying{

    [_audioPlayer play];
    [self.playButton setImage:[UIImage imageNamed:@"pause"] forState:UIControlStateNormal];
    self.updateTimer = [NSTimer scheduledTimerWithTimeInterval:0.1 target:self selector:@selector(updateSlider) userInfo:nil repeats:YES];

}

- (void)updateSlider{
    float progress = self.audioPlayer.currentTime;
    [self.slider setValue:progress];
    
    int currentTime = (int) progress;
    NSString *time = [NSString stringWithFormat:@"%02d:%02d", currentTime / 60, currentTime % 60];
    self.timeLabel.text = time;
   
}

- (IBAction)seekTime:(id)sender {
    self.audioPlayer.currentTime = self.slider.value;
    
    int currentTime = (int) self.audioPlayer.currentTime;
    NSString *time = [NSString stringWithFormat:@"%02d:%02d", currentTime / 60, currentTime % 60];
    self.timeLabel.text = time;
}


- (IBAction)onCloseView:(id)sender {
    [self stopPlaying];
    self.dismiss();
}


@end
