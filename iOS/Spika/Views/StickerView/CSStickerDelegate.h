//
//  CSStickerDelegate.h
//  Spika
//
//  Created by mislav on 24/03/16.
//  Copyright © 2016 Clover Studio. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol CSStickerDelegate <NSObject>

@required
-(void) onSticker:(NSString*)stickerUrl;

@end
