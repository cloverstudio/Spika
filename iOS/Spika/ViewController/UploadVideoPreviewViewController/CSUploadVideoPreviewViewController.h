//
//  UploadVideoPreviewViewController.h
//  Prototype
//
//  Created by Ivo Peric on 07/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MediaPlayer/MediaPlayer.h>

@interface CSUploadVideoPreviewViewController : UIViewController

- (IBAction)onGalleryClicked:(id)sender;
- (IBAction)onCameraClicked:(id)sender;
- (IBAction)onCancelClicked:(id)sender;
- (IBAction)onOkClicked:(id)sender;
@property (weak, nonatomic) IBOutlet UIView *firstButtonsView;
@property (weak, nonatomic) IBOutlet UIView *secondButtonsView;
@property (weak, nonatomic) IBOutlet UIView *videoView;
@property (weak, nonatomic) IBOutlet UIButton *okButton;

@property (nonatomic, strong) NSString* filePath;
@property (nonatomic, strong) MPMoviePlayerController* moviePlayer;
@property (nonatomic, strong) NSData *videoData;
@property (nonatomic, strong) NSString *fileName;
@property (nonatomic, strong) NSString *mimeType;

@end
