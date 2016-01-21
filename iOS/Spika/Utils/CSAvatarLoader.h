//
//  CSImageLoaderDelegate.h
//  Spika
//
//  Created by mislav on 10/12/15.
//  Copyright © 2015 Clover Studio. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SDWebImageManager.h"

@interface CSAvatarLoader : NSObject <SDWebImageManagerDelegate>

+ (void)setImageWithURL:(NSURL *)url inImageView:(UIImageView *)imageView;

@end
