//
//  RecordAudioViewController.m
//  Prototype
//
//  Created by Ivo Peric on 07/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSRecordAudioViewController.h"
#import "CSConfig.h"
#import "CSUploadManager.h"
#import "CSUtils.h"

@interface CSRecordAudioViewController () <AVAudioRecorderDelegate, AVAudioPlayerDelegate>

@property (nonatomic, strong) AVAudioRecorder *recorder;
@property (nonatomic, strong) AVAudioPlayer *player;
@property (nonatomic, strong) NSTimer* updateTimer;
@property (nonatomic, strong) NSTimer* recordTimer;
@property (nonatomic, strong) NSData* audioData;

@end

@implementation CSRecordAudioViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    NSArray *pathComponents = [NSArray arrayWithObjects:
                               [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) lastObject],
                               @"MyAudioMemo.m4a",
                               nil];
    NSURL *outputFileURL = [NSURL fileURLWithPathComponents:pathComponents];
    
    // Setup audio session
    AVAudioSession *session = [AVAudioSession sharedInstance];
    [session setCategory:AVAudioSessionCategoryPlayAndRecord error:nil];
    
    // Define the recorder setting
    NSMutableDictionary *recordSetting = [[NSMutableDictionary alloc] init];
    
    [recordSetting setValue:[NSNumber numberWithInt:kAudioFormatMPEG4AAC] forKey:AVFormatIDKey];
    [recordSetting setValue:[NSNumber numberWithFloat:44100.0] forKey:AVSampleRateKey];
    [recordSetting setValue:[NSNumber numberWithInt: 2] forKey:AVNumberOfChannelsKey];
    
    // Initiate and prepare the recorder
    _recorder = [[AVAudioRecorder alloc] initWithURL:outputFileURL settings:recordSetting error:NULL];
    _recorder.delegate = self;
    _recorder.meteringEnabled = YES;
    [_recorder prepareToRecord];
    
    self.audioView.backgroundColor = kAppDefaultColor(1);
    self.audioView.layer.cornerRadius = 10;
    self.audioView.layer.masksToBounds = YES;
    
    self.controlBackground.backgroundColor = [UIColor whiteColor];
    self.controlBackground.layer.cornerRadius = 10;
    self.controlBackground.layer.masksToBounds = YES;
    
    self.timeLabel.textColor = kAppDefaultColor(1);
    self.timeLabel.text = @"00:00";
    self.recordTime.text = @"00:00";
    
    self.playPauseButton.enabled = NO;
    self.slider.minimumValue = 0;
    self.slider.value = 0;
    
    self.edgesForExtendedLayout = UIRectEdgeNone;
    
    self.okButton.enabled = NO;
}

-(void)viewWillDisappear:(BOOL)animated{
    
    [self.updateTimer invalidate];
    self.updateTimer = nil;
    [self.recordTimer invalidate];
    self.recordTimer = nil;

    [super viewWillDisappear:animated];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) initAudio{
    
    _audioData = [NSData dataWithContentsOfURL:_recorder.url];
    NSString* sizeString = [NSString stringWithFormat:@"%i", _audioData.length];
    [self.okButton setTitle:[NSString stringWithFormat:@"Ok, %@", [CSUtils readableFileSize:sizeString]] forState:UIControlStateNormal];
    self.okButton.enabled = YES;
    
    _player = [[AVAudioPlayer alloc] initWithContentsOfURL:_recorder.url error:nil];
    _player.delegate = self;
    
    self.slider.maximumValue = self.player.duration;
    self.playPauseButton.enabled = YES;
    
}

-(void) animateView{

    self.viewForAnimate.hidden = NO;
    self.viewForAnimate.alpha = 0.0f;
    
    [UIView animateWithDuration:1.0
                          delay:0.0
                        options:UIViewAnimationOptionRepeat  | UIViewAnimationOptionAutoreverse
                     animations:^{
                         self.viewForAnimate.alpha = 1.0f;
                     }
                     completion:nil];

}

- (IBAction)onSeek:(id)sender {
    self.player.currentTime = self.slider.value;
    
    int currentTime = (int) self.player.currentTime;
    NSString *time = [NSString stringWithFormat:@"%02d:%02d", currentTime / 60, currentTime % 60];
    self.timeLabel.text = time;
}

- (IBAction)onPlay:(id)sender {
    if([_player isPlaying]){
        [self stopPlaying];
    }else{
        [self startPlaying];
    }
}

-(void)audioPlayerDidFinishPlaying:(AVAudioPlayer *)player successfully:(BOOL)flag{
    
    [self stopPlaying];
    
}

-(void) stopPlaying{
    
    [_player stop];
    [self.playPauseButton setImage:[UIImage imageNamed:@"play"] forState:UIControlStateNormal];
    self.timeLabel.text = @"00:00";
    self.slider.value = 0;
    [self.updateTimer invalidate];
    self.updateTimer = nil;
    
}

-(void) startPlaying{
    
    [_player play];
    [self.playPauseButton setImage:[UIImage imageNamed:@"pause"] forState:UIControlStateNormal];
    self.updateTimer = [NSTimer scheduledTimerWithTimeInterval:0.1 target:self selector:@selector(updateSlider) userInfo:nil repeats:YES];
    
}

- (void) audioRecorderDidFinishRecording:(AVAudioRecorder *)avrecorder successfully:(BOOL)flag{
    
    [self initAudio];
}

- (void)updateSlider{
    float progress = self.player.currentTime;
    [self.slider setValue:progress];
    
    int currentTime = (int) progress;
    NSString *time = [NSString stringWithFormat:@"%02d:%02d", currentTime / 60, currentTime % 60];
    self.timeLabel.text = time;
    
}

- (void)updateRecTime{
    float progress = self.recorder.currentTime;
    
    int currentTime = (int) progress;
    NSString *time = [NSString stringWithFormat:@"%02d:%02d", currentTime / 60, currentTime % 60];
    self.recordTime.text = time;
    
}

- (IBAction)onRecord:(id)sender {
    if (!_recorder.recording) {
        [self animateView];
        
        AVAudioSession *session = [AVAudioSession sharedInstance];
        [session setActive:YES error:nil];
        
        // Start recording
        [_recorder record];
        
        self.recordTimer = [NSTimer scheduledTimerWithTimeInterval:0.1 target:self selector:@selector(updateRecTime) userInfo:nil repeats:YES];
        self.recordTime.hidden = NO;
        self.controlBackground.hidden = YES;

        
    } else {
        
        self.viewForAnimate.hidden = YES;
        
        // Pause recording
        [_recorder stop];
        
        AVAudioSession *audioSession = [AVAudioSession sharedInstance];
        [audioSession setActive:NO error:nil];
        
        self.recordTime.hidden = YES;
        self.controlBackground.hidden = NO;
        self.recordTime.text = @"00:00";
        [self.recordTimer invalidate];
        self.recordTimer = nil;

    }
    
}

- (IBAction)onCancelClicked:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onOkClicked:(id)sender {
    NSString* mimeType = @"audio/wav";
    NSString* fileName = [NSString stringWithFormat:@"audio_%@.%@", [NSString stringWithFormat:@"%d", (int)[[NSDate date] timeIntervalSince1970]], @"wav"];
    
    NSData* data = [NSData dataWithContentsOfURL:_recorder.url];
    
    CSUploadManager* manager = [CSUploadManager new];
    [manager uploadFile:data fileName:fileName mimeType:mimeType viewForLoading:self.view completition:^(id responseObject){
    
        [[NSNotificationCenter defaultCenter] postNotificationName:kAppFileUploadedNotification
                                                            object:nil
                                                          userInfo:[NSDictionary dictionaryWithObject:responseObject forKey:paramResponseObject]];
        [self.navigationController popViewControllerAnimated:YES];
        
    }];
}
@end
