//
//  CSStickerModel.h
//  Spika
//
//  Created by mislav on 23/03/16.
//  Copyright © 2016 Clover Studio. All rights reserved.
//

#import "CSModel.h"

@protocol CSStickerModel
@end

@interface CSStickerModel : CSModel

@property NSString *fullPic;
@property NSString *smallPic;

@end
