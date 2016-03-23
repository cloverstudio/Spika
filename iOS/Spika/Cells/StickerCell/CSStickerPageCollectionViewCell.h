//
//  CSStickerPageCollectionViewCell.h
//  Spika
//
//  Created by mislav on 23/03/16.
//  Copyright © 2016 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CSStickerPageModel.h"

@interface CSStickerPageCollectionViewCell : UICollectionViewCell

@property (nonatomic, strong) CSStickerPageModel *model;

@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;

@end
