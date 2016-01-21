
//
//  RecordAudioViewController.h
//  Prototype
//
//  Created by Ivo Peric on 07/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>

@interface CSRecordAudioViewController : UIViewController

@property (weak, nonatomic) IBOutlet UIView *audioView;
@property (weak, nonatomic) IBOutlet UIView *controlBackground;
@property (weak, nonatomic) IBOutlet UIButton *recButton;
@property (weak, nonatomic) IBOutlet UIButton *okButton;
@property (weak, nonatomic) IBOutlet UIImageView *viewForAnimate;
@property (weak, nonatomic) IBOutlet UIButton *playPauseButton;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet UISlider *slider;
@property (weak, nonatomic) IBOutlet UILabel *recordTime;
- (IBAction)onSeek:(id)sender;
- (IBAction)onPlay:(id)sender;
- (IBAction)onRecord:(id)sender;
- (IBAction)onCancelClicked:(id)sender;
- (IBAction)onOkClicked:(id)sender;

@end
