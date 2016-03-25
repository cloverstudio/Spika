//
//  CSStickerView.h
//  Spika
//
//  Created by mislav on 23/03/16.
//  Copyright © 2016 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CSStickerListModel.h"
#import "CSStickerDelegate.h"

typedef void(^dismissStickerView)(void);

@interface CSStickerView : UIView

@property (nonatomic, strong) CSStickerListModel *stickerList;

@property (nonatomic, strong) dismissStickerView dismiss;
@property (nonatomic, weak) id <CSStickerDelegate> delegate;
@property (weak, nonatomic) IBOutlet UIView *backgroundView;
@property (strong, nonatomic) IBOutlet UIView *rootView;
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;
@property (weak, nonatomic) IBOutlet UICollectionView *categoryCollectionView;

-(void) initializeInView:(UIView *)parentView dismiss:(dismissStickerView)dismiss;

- (IBAction)onCancel:(id)sender;

@end
