//
//  CSStickerPageModel.h
//  Spika
//
//  Created by mislav on 23/03/16.
//  Copyright © 2016 Clover Studio. All rights reserved.
//

#import "CSModel.h"
#import "CSStickerModel.h"

@protocol CSStickerPageModel
@end

@interface CSStickerPageModel : CSModel

@property NSString *mainPic;
@property NSArray <CSStickerModel> *list;

@end
