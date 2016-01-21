//
//  VideoPreviewView.m
//  Prototype
//
//  Created by Ivo Peric on 01/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSVideoPreviewView.h"
#import "CSUtils.h"
#import <AFNetworking/AFNetworking.h>
#import "CSDownloadManager.h"

@implementation CSVideoPreviewView

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
    
    self.backGroundView.layer.cornerRadius = 10;
    self.backGroundView.layer.masksToBounds = YES;
    
    
    _filePath = [CSUtils getFileFromFileModel:message.file];
    
    if (![CSUtils isFileExistsWithFileName:_filePath]) {
        
        CSDownloadManager* downloadManager = [CSDownloadManager new];
        [downloadManager downloadFileWithUrl:[NSURL URLWithString:[CSUtils generateDownloadURLFormFileId:message.file.file.id]] destination:[NSURL URLWithString:_filePath] viewForLoading:self completition:^(BOOL success){
        
            [self playVideo];
            
        }];
    }else{
        
        [self playVideo];
        
    }
    
}

-(void) playVideo{

    _moviePlayer = [[MPMoviePlayerController alloc] initWithContentURL:[NSURL fileURLWithPath:_filePath]];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(moviePlaybackComplete:)
                                                 name:MPMoviePlayerPlaybackDidFinishNotification
                                               object:_moviePlayer];
    
    if ([_moviePlayer respondsToSelector:@selector(setAllowsAirPlay:)])	//Allow airplay if availabe
        [_moviePlayer setAllowsAirPlay:YES];
    
    CGRect videoViewRect = self.frame;
    videoViewRect.origin.x = videoViewRect.origin.x + 20;
    videoViewRect.origin.y = videoViewRect.origin.y + 20;
    videoViewRect.size.height = videoViewRect.size.height - 90;
    videoViewRect.size.width = videoViewRect.size.width - 90;
    
    _moviePlayer.view.frame = videoViewRect;
    [self.backGroundView addSubview:_moviePlayer.view];
    
    [_moviePlayer play];

    
}

- (void)moviePlaybackComplete:(NSNotification *)notification
{
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:MPMoviePlayerPlaybackDidFinishNotification
                                                  object:_moviePlayer];
    
}

- (IBAction)onCloseView:(id)sender {
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:MPMoviePlayerPlaybackDidFinishNotification
                                                  object:_moviePlayer];
    self.dismiss();
}

@end
