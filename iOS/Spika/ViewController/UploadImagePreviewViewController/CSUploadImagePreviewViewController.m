//
//  UploadImagePreviewViewController.m
//  Prototype
//
//  Created by Ivo Peric on 02/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSUploadImagePreviewViewController.h"
#import "CSConfig.h"
#import "CSUploadManager.h"
#import "CSUtils.h"

@interface CSUploadImagePreviewViewController() <UIImagePickerControllerDelegate, UINavigationControllerDelegate, UIAlertViewDelegate>

@end

@interface CSUploadImagePreviewViewController ()

@end

@implementation CSUploadImagePreviewViewController

-(id) initWithType: (int) type{
    if (self = [super init]) {
        self.type = type;
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.imageControllerBackground.backgroundColor = kAppDefaultColor(1);
    self.imageControllerBackground.layer.cornerRadius = 10;
    self.imageControllerBackground.layer.masksToBounds = YES;
    
    if(self.type == kAppGalleryType){
        [self getImageFromGallery];
    }else{
        [self getImageFromCamera];
    }
    
    self.edgesForExtendedLayout = UIRectEdgeNone;
}

-(void)getImageFromGallery{
    UIImagePickerController *imagePickerController = [[UIImagePickerController alloc] init];
    imagePickerController.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    imagePickerController.delegate = self;
    [self presentViewController:imagePickerController animated:YES completion:nil];
}

-(void)getImageFromCamera{
    UIImagePickerController *imagePickerController = [[UIImagePickerController alloc] init];
    imagePickerController.sourceType = UIImagePickerControllerSourceTypeCamera;
    imagePickerController.delegate = self;
    [self presentViewController:imagePickerController animated:YES completion:nil];
}

-(void)viewDidAppear:(BOOL)animated{
    self.title = @"Preview";
}

-(void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info{
    
    NSURL *imageUrl = (NSURL *)[info valueForKey:UIImagePickerControllerReferenceURL];
    NSString* extensions = [imageUrl pathExtension];
    
    if([extensions isEqualToString:@"JPG"] || [extensions isEqualToString:@"jpg"]){
        _mimeType = kAppImageJPG;
    }else if([extensions isEqualToString:@"PNG"] || [extensions isEqualToString:@"png"]){
        _mimeType = kAppImagePNG;
    }else  if([extensions isEqualToString:@"GIF"] || [extensions isEqualToString:@"gif"]){
        _mimeType = kAppImageGIF;
    }else{
        _mimeType = kAppImageJPG;
    }
    
    _fileName = [NSString stringWithFormat:@"%@_%ld.%@", @"image", (long)[[NSDate date] timeIntervalSince1970], extensions];


    UIImage * image = [info valueForKey:UIImagePickerControllerOriginalImage];
    _imageToUpload = image;
    self.imageView.image = image;
    self.imageView.contentMode = UIViewContentModeScaleAspectFit;
    
    if(image.size.width > kAppMaxImageScaledSize || image.size.height > kAppMaxImageScaledSize){
        
        CGFloat imageWidth = image.size.width;
        CGFloat imageHeight = image.size.height;
        
        if(imageWidth >= imageHeight){
            CGFloat newHeight = kAppMaxImageScaledSize / (imageWidth / imageHeight);
            CGSize newSize = CGSizeMake(kAppMaxImageScaledSize, newHeight);
            _scaledImageToUpload = [CSUtils resizeImage:image toSize:newSize];
        }else{
            CGFloat newWidth = kAppMaxImageScaledSize / (imageHeight / imageWidth);
            CGSize newSize = CGSizeMake(newWidth, kAppMaxImageScaledSize);
            _scaledImageToUpload = [CSUtils resizeImage:image toSize:newSize];
        }
        
    }
    
    [picker dismissViewControllerAnimated:YES completion:^{
    }];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)onCancel:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onOk:(id)sender {
    
    if(_scaledImageToUpload){
        
        NSData *originalImage = [[NSData alloc] initWithData:UIImageJPEGRepresentation(_imageToUpload, 1)];
        long originalImageSize = (long)originalImage.length;
        NSString *originalSizeString = [CSUtils readableFileSize:[NSString stringWithFormat: @"%ld", originalImageSize]];
        NSString *originalButtonTitle = [NSString stringWithFormat:@"NO, %@", originalSizeString];
        
        NSData *scaledImage = [[NSData alloc] initWithData:UIImageJPEGRepresentation(_scaledImageToUpload, 1)];
        long scaledImageSize = (long)scaledImage.length;
        NSString *scaledSizeString = [CSUtils readableFileSize:[NSString stringWithFormat: @"%ld", scaledImageSize]];
        NSString *scaledButtonTitle = [NSString stringWithFormat:@"YES, %@", scaledSizeString];
        
        UIAlertView* alertView = [[UIAlertView alloc] initWithTitle:@"Compress image" message:@"Do You want to scale image?" delegate:self cancelButtonTitle:@"CANCEL" otherButtonTitles:scaledButtonTitle, originalButtonTitle, nil];
        [alertView show];
        
    }else{
        [self uploadImage:_imageToUpload];
    }
    
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if(buttonIndex == 1){
        [self uploadImage:_scaledImageToUpload];
    }else if(buttonIndex == 2){
        [self uploadImage:_imageToUpload];
    }
}

-(void) uploadImage: (UIImage*) image{
    CSUploadManager* uploadManager = [CSUploadManager new];
    [uploadManager uploadImage:image fileName:_fileName mimeType:_mimeType viewForLoading:self.view completition:^(id responseObject){
        
        [[NSNotificationCenter defaultCenter] postNotificationName:kAppFileUploadedNotification
                                                            object:nil
                                                          userInfo:[NSDictionary dictionaryWithObject:responseObject forKey:paramResponseObject]];
        [self.navigationController popViewControllerAnimated:YES];
        
    }];
}

@end
