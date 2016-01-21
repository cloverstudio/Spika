//
//  ImagePreviewView.m
//  Prototype
//
//  Created by Ivo Peric on 01/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSImagePreviewView.h"
#import "CSUtils.h"
#import "UIImageView+AFNetworking.h"
#import "CSDownloadManager.h"

@implementation CSImagePreviewView

-(void) initializeViewWithMessage: (CSMessageModel*) message navigationAndStatusBarHeight:(float) size dismiss: (dismissPreview) dismiss{
    
    self.dismiss = dismiss;

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
    
    CSDownloadManager* downloadManager = [[CSDownloadManager alloc] init];
    
    NSString* filePath = [NSString new];
    filePath = [CSUtils getFileFromFileModel:message.file];
    
    if(![CSUtils isFileExistsWithFileName:filePath]){
        
        [self.loadingIndicator startAnimating];
        [downloadManager downloadFileWithUrl:[NSURL URLWithString:[CSUtils generateDownloadURLFormFileId:message.file.file.id]] destination:[NSURL URLWithString:filePath] viewForLoading:nil completition:^(BOOL success) {
        
            [self setImageWithPath:filePath];
            
        }];
        
    }else{
        
        [self setImageWithPath:filePath];
        
    }
    
//    [self.image setImageWithURL:[NSURL URLWithString:[Utils generateDownloadURLFormFileId:message.file.file.id]]];
//    self.image.contentMode = UIViewContentModeScaleAspectFit;
    
}

- (void) setImageWithPath: (NSString*) path{

    UIImage* image = [UIImage imageWithContentsOfFile:path];
    self.image.image = image;
    self.image.contentMode = UIViewContentModeScaleAspectFit;
    
    [self.loadingIndicator stopAnimating];
    self.loadingIndicator.hidden = YES;
    
}

- (IBAction)onClose:(id)sender {
    self.dismiss();
}
@end

