//
//  UploadImagePreviewViewController.h
//  Prototype
//
//  Created by Ivo Peric on 02/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CSUploadImagePreviewViewController : UIViewController

@property (weak, nonatomic) IBOutlet UIView *imageControllerBackground;
@property (weak, nonatomic) IBOutlet UIImageView *imageView;
- (IBAction)onCancel:(id)sender;
- (IBAction)onOk:(id)sender;

-(id) initWithType: (int) type;
@property (nonatomic, strong) UIImage* imageToUpload;
@property (nonatomic, strong) UIImage* scaledImageToUpload;
@property (nonatomic, strong) NSString* mimeType;
@property (nonatomic, strong) NSString* fileName;
@property (nonatomic) int type;

@end
