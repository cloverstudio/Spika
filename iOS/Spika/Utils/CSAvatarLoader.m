//
//  CSImageLoaderDelegate.m
//  Spika
//
//  Created by mislav on 10/12/15.
//  Copyright © 2015 Clover Studio. All rights reserved.
//

#import "CSAvatarLoader.h"

@interface CSAvatarLoader ()

@property (strong, nonatomic) SDWebImageManager *avatarImageManager;

@end

@implementation CSAvatarLoader

+ (instancetype)sharedImageLoader
{
    static CSAvatarLoader* loader = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        loader = [[CSAvatarLoader alloc] init];
        loader.avatarImageManager = [[SDWebImageManager alloc] init];
        loader.avatarImageManager.delegate = loader;
    });
    
    return loader;
}

- (UIImage *)imageManager:(SDWebImageManager *)imageManager
 transformDownloadedImage:(UIImage *)image
                  withURL:(NSURL *)imageURL
{
    // Place your image size here
    CGFloat width = 256.0f;
    CGFloat height = 256.0f;
    
    if (image.size.width < width && image.size.height < height) {
        return image;
    }
    
    CGSize imageSize = CGSizeMake(width, height);
    
    UIGraphicsBeginImageContext(imageSize);
    [image drawInRect:CGRectMake(0, 0, width, height)];
    image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return image;
}

+(void)setImageWithURL:(NSURL *)url inImageView:(UIImageView *)imageView {
        [[CSAvatarLoader sharedImageLoader].avatarImageManager downloadImageWithURL:url
                                                                            options:0
                                                                           progress:nil
                                                                          completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, BOOL finished, NSURL *imageURL) {
                                                                              imageView.image = image;
                                                                          }];
}

@end
