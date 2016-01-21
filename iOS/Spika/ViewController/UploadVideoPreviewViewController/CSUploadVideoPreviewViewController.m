//
//  UploadVideoPreviewViewController.m
//  Prototype
//
//  Created by Ivo Peric on 07/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSUploadVideoPreviewViewController.h"
#import <MobileCoreServices/MobileCoreServices.h>
#import "CSConfig.h"
#import "CSUploadManager.h"
#import "CSUtils.h"

@interface CSUploadVideoPreviewViewController () <UIImagePickerControllerDelegate, UINavigationControllerDelegate>

@end

@implementation CSUploadVideoPreviewViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.videoView.backgroundColor = kAppDefaultColor(1);
    self.videoView.layer.cornerRadius = 10;
    self.videoView.layer.masksToBounds = YES;
    
    self.edgesForExtendedLayout = UIRectEdgeNone;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)viewDidDisappear:(BOOL)animated{

    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:MPMoviePlayerPlaybackDidFinishNotification
                                                  object:_moviePlayer];
    
    [super viewDidDisappear:animated];
}

-(void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info{
    
    NSURL *videoUrl = (NSURL *)[info valueForKey:UIImagePickerControllerMediaURL];
    NSString *path = [videoUrl path];
    _videoData = [[NSFileManager defaultManager] contentsAtPath:path];
    
    _mimeType = @"video/mp4";
//    _fileName = [path lastPathComponent];
    _fileName = [NSString stringWithFormat:@"%@_%ld.%@", @"video", (long)[[NSDate date] timeIntervalSince1970], @"mp4"];
    
    _filePath = path;
    
    [self changeButtonsLayout];
    
    [picker dismissViewControllerAnimated:YES completion:^{
    }];
    
    [self playVideo];
    
}

-(void) changeButtonsLayout{
    self.firstButtonsView.hidden = YES;
    self.secondButtonsView.hidden = NO;
    
    NSString* sizeString = [NSString stringWithFormat:@"%d", _videoData.length];
    [self.okButton setTitle:[NSString stringWithFormat:@"Ok, %@", [CSUtils readableFileSize:sizeString]] forState:UIControlStateNormal];
}

-(void) playVideo{
    
    _moviePlayer = [[MPMoviePlayerController alloc] initWithContentURL:[NSURL fileURLWithPath:_filePath]];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(moviePlaybackComplete:)
                                                 name:MPMoviePlayerPlaybackDidFinishNotification
                                               object:_moviePlayer];
    
    if ([_moviePlayer respondsToSelector:@selector(setAllowsAirPlay:)])	//Allow airplay if availabe
        [_moviePlayer setAllowsAirPlay:YES];
    
    CGRect videoViewRect = self.videoView.frame;
    videoViewRect.origin.x = videoViewRect.origin.x + 20;
    videoViewRect.origin.y = videoViewRect.origin.y + 20;
    videoViewRect.size.height = videoViewRect.size.height - 60;
    videoViewRect.size.width = videoViewRect.size.width - 60;
    
    _moviePlayer.view.frame = videoViewRect;
    [self.videoView addSubview:_moviePlayer.view];
    
    [_moviePlayer play];
    
}

- (void)moviePlaybackComplete:(NSNotification *)notification
{
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:MPMoviePlayerPlaybackDidFinishNotification
                                                  object:_moviePlayer];
    
}

- (IBAction)onGalleryClicked:(id)sender {
    UIImagePickerController *picker= [[UIImagePickerController alloc] init];
    picker.delegate = self;
    picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    picker.mediaTypes = [NSArray arrayWithObjects:(NSString *)kUTTypeMovie, nil];
    
    [self presentViewController:picker animated:YES completion:nil];
}

- (IBAction)onCameraClicked:(id)sender {
    UIImagePickerController *picker= [[UIImagePickerController alloc] init];
    picker.delegate = self;
    picker.sourceType = UIImagePickerControllerSourceTypeCamera;
    picker.mediaTypes = [NSArray arrayWithObjects:(NSString *)kUTTypeMovie, nil];
    
    [self presentViewController:picker animated:YES completion:nil];
}

- (IBAction)onCancelClicked:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onOkClicked:(id)sender {
    CSUploadManager* manager = [CSUploadManager new];
    [manager uploadFile:_videoData fileName:_fileName mimeType:_mimeType viewForLoading:self.view completition:^(id responseObject){
    
        [[NSNotificationCenter defaultCenter] postNotificationName:kAppFileUploadedNotification
                                                            object:nil
                                                          userInfo:[NSDictionary dictionaryWithObject:responseObject forKey:paramResponseObject]];
        [self.navigationController popViewControllerAnimated:YES];

        
    }];
}
@end
