
//
//  RecordAudioViewController.h
//  Prototype
//
//  Created by Ivo Peric on 07/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>
#import "SE2AudioPlayerView.h"

@interface CSRecordAudioViewController : UIViewController

@property (weak, nonatomic) IBOutlet UIView *audioView;
@property (weak, nonatomic) IBOutlet UIButton *okButton;
@property (weak, nonatomic) IBOutlet UIView *expandingGreenBackgroundView;
@property (weak, nonatomic) IBOutlet UIView *pulastingView;
@property (weak, nonatomic) IBOutlet UIButton *playButton;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *expandingGreenHeight;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *expandingGreenWidth;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *pulsatingHight;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *pulsatingWidth;
@property (weak, nonatomic) IBOutlet UIView *audioPlayerView;
@property (weak, nonatomic) IBOutlet UIImageView *playImageView;
@property (weak, nonatomic) IBOutlet UILabel *playLabel;

- (IBAction)onRecord:(id)sender;
- (IBAction)onCancelClicked:(id)sender;
- (IBAction)onOkClicked:(id)sender;

@end
