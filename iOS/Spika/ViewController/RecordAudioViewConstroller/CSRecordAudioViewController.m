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
@property (nonatomic, strong) NSTimer* updateTimer;
@property (nonatomic, strong) NSTimer* recordTimer;
@property (nonatomic, strong) NSData* audioData;
@property (nonatomic, strong) NSTimer* pulseTimer;
@property (nonatomic, strong) SE2AudioPlayerView *audioPlayer;

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
    
    self.audioView.backgroundColor = [UIColor whiteColor];
    self.audioView.layer.masksToBounds = YES;
    
    self.playButton.layer.cornerRadius = self.playButton.frame.size.height/2;
    self.playButton.layer.borderWidth = 2.0f;
    self.playButton.layer.borderColor = kAppDefaultColor(1).CGColor;
    
    self.playLabel.textColor = kAppDefaultColor(1);
    self.playLabel.alpha = 0.0f;
    
    self.expandingGreenBackgroundView.layer.cornerRadius = self.expandingGreenBackgroundView.frame.size.height/2;
    self.expandingGreenBackgroundView.backgroundColor = kAppDefaultColor(1);
    
    self.pulastingView.layer.cornerRadius = self.pulastingView.frame.size.height/2;
    self.pulastingView.layer.borderWidth = 1/((self.audioView.frame.size.height+50)/150);
    self.pulastingView.layer.borderColor = [UIColor whiteColor].CGColor;
    self.pulastingView.backgroundColor = [UIColor clearColor];
    
    self.audioPlayerView.layer.cornerRadius = 5.0f;
    self.audioPlayerView.layer.borderColor = kAppDefaultColor(1).CGColor;
    self.audioPlayerView.layer.borderWidth = 1.0f;
    self.audioPlayerView.alpha = 0;
    
    self.edgesForExtendedLayout = UIRectEdgeNone;
    
    self.okButton.enabled = NO;
}

-(void)viewWillDisappear:(BOOL)animated{
    
    [self.updateTimer invalidate];
    self.updateTimer = nil;
    [self.recordTimer invalidate];
    self.recordTimer = nil;
    [_pulseTimer invalidate];
    _pulseTimer = nil;
    
    [super viewWillDisappear:animated];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) initAudio{
    
    _audioData = [NSData dataWithContentsOfURL:_recorder.url];
    NSString* sizeString = [NSString stringWithFormat:@"%ld", (unsigned long)_audioData.length];
    [self.okButton setTitle:[NSString stringWithFormat:@"Ok, %@", [CSUtils readableFileSize:sizeString]] forState:UIControlStateNormal];
    self.okButton.enabled = YES;
}

-(void) animateView
{
    [UIView animateWithDuration:0.7
                     animations:^{
                         
                         const CGFloat scale = sqrt(pow(self.audioView.frame.size.height / 2, 2) + pow(self.audioView.frame.size.width / 2, 2)) / 25;
                         [self.expandingGreenBackgroundView setTransform:CGAffineTransformMakeScale(scale, scale)];
                         
                         self.playLabel.alpha = 1.0f;
                         [self.playImageView setTransform:CGAffineTransformMakeScale(0.8f, 0.8f)];
                         
                         [self.view layoutIfNeeded];
                         
                         
                     } completion:^(BOOL finished) {
                         [self pulseAnimation];
                         [self pulseTimer];
                         _pulseTimer = [NSTimer scheduledTimerWithTimeInterval:1
                                                                        target:self
                                                                      selector:@selector(pulseAnimation)
                                                                      userInfo:nil
                                                                       repeats:YES];
                     }];
}

-(void) pulseAnimation
{
    [UIView animateWithDuration:0.7
                     animations:^{
                         
                         const CGFloat scale = sqrt(pow(self.audioView.frame.size.height / 2, 2) + pow(self.audioView.frame.size.width / 2, 2)) / 25;
                         [self.pulastingView setTransform:CGAffineTransformMakeScale(scale, scale)];
                         
                     } completion:^(BOOL finished) {
                         
                         self.pulastingView.alpha = 0;
                         const CGFloat scale = 1;
                         [self.pulastingView setTransform:CGAffineTransformMakeScale(scale, scale)];
                         self.pulastingView.alpha = 1;
                         
                     }];
}

-(void)animateReverseColor
{
    [UIView animateWithDuration:0.2
                     animations:^{
                         
                         const CGFloat scale = 1;
                         [self.expandingGreenBackgroundView setTransform:CGAffineTransformMakeScale(scale, scale)];
                         
                         self.playLabel.alpha = 0.0f;
                         [self.playImageView setTransform:CGAffineTransformMakeScale(1.0f, 1.0f)];
                         
                         
                     } completion:^(BOOL finished) {
                         [self addAudioPlayer];
                     }];
    
}

-(void)addAudioPlayer
{
    _audioPlayer = [[SE2AudioPlayerView alloc] initWithFrame:CGRectMake(0,
                                                                        0,
                                                                        self.audioPlayerView.frame.size.width,
                                                                        self.audioPlayerView.frame.size.height)
                                                         url:[_recorder.url path]];
    [self.audioPlayerView addSubview:_audioPlayer];
    
    [self animateAudioPlayer:YES];
    
    
}

-(void)animateAudioPlayer:(BOOL)boolValue
{
    [UIView animateWithDuration:0.2
                     animations:^{
                         
                         self.audioPlayerView.alpha = boolValue ? 1 : 0;
                         
                     } completion:^(BOOL finished) {
                         
                         if (!boolValue) {
                             _audioPlayer = nil;
                         }
                         
                     }];
}

- (void) audioRecorderDidFinishRecording:(AVAudioRecorder *)avrecorder successfully:(BOOL)flag{
    
    [self initAudio];
}

- (void)updateSlider{
    
}

- (void)updateRecTime{
    
}

- (IBAction)onRecord:(id)sender
{
    if (!_recorder.recording) {
        [self animateView];
        [self animateAudioPlayer:NO];
        
        AVAudioSession *session = [AVAudioSession sharedInstance];
        [session setActive:YES error:nil];
        
        // Start recording
        [_recorder record];
        
    } else {
        
        // Pause recording
        [_recorder stop];
        
        AVAudioSession *audioSession = [AVAudioSession sharedInstance];
        [audioSession setActive:NO error:nil];
        
        [self.recordTimer invalidate];
        self.recordTimer = nil;
        [_pulseTimer invalidate];
        _pulseTimer = nil;
        
        [self animateReverseColor];
        
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
