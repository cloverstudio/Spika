//
//  CSStickerView.m
//  Spika
//
//  Created by mislav on 23/03/16.
//  Copyright © 2016 Clover Studio. All rights reserved.
//

#import "CSStickerView.h"
#import "CSStickerCategoryCollectionViewCell.h"
#import "CSStickerPageCollectionViewCell.h"

@interface CSStickerView () <UICollectionViewDataSource, UICollectionViewDelegate, CSStickerDelegate>

@property (nonatomic) CGRect originalRect;
@property (nonatomic) int selectedStickerCollection;

@end

@implementation CSStickerView

-(void) initializeInView:(UIView *)parentView dismiss:(dismissStickerView)dismiss {
    
    self.dismiss = dismiss;
    
    self.frame = CGRectMake(0, 0, parentView.frame.size.width, parentView.frame.size.height);
    UIView *view = [[[NSBundle mainBundle] loadNibNamed:NSStringFromClass([self class])
                                                  owner:self
                                                options:nil] objectAtIndex:0];
    view.frame = self.bounds;
    [self addSubview:view];
    
    self.backgroundView.layer.cornerRadius = 10;
    self.backgroundView.layer.masksToBounds = YES;
    
    self.originalRect = self.backgroundView.frame;
    
    [self.collectionView registerNib:[UINib nibWithNibName:NSStringFromClass([CSStickerPageCollectionViewCell class]) bundle:nil] forCellWithReuseIdentifier:@"page"];
    [self.categoryCollectionView registerNib:[UINib nibWithNibName:NSStringFromClass([CSStickerCategoryCollectionViewCell class]) bundle:nil] forCellWithReuseIdentifier:@"cell"];
    
    self.categoryCollectionView.allowsSelection = YES;
    self.collectionView.allowsSelection = NO;
    
    if (self.stickerList.stickers.count) {
        self.selectedStickerCollection = 0;
        NSIndexPath *selection = [NSIndexPath indexPathForItem:self.selectedStickerCollection
                                                     inSection:0];
        [self.categoryCollectionView selectItemAtIndexPath:selection
                                                  animated:NO
                                            scrollPosition:UICollectionViewScrollPositionCenteredHorizontally];
    }
    
    [parentView addSubview:self];
    [self animateOpen];
}

-(void)setStickerList:(CSStickerListModel *)stickerList {
    _stickerList = stickerList;
    [self.collectionView reloadData];
    [self.categoryCollectionView reloadData];
    if (stickerList.stickers.count) {
        self.selectedStickerCollection = 0;
        NSIndexPath *selection = [NSIndexPath indexPathForItem:self.selectedStickerCollection
                                                     inSection:0];
        [self.categoryCollectionView selectItemAtIndexPath:selection
                                                  animated:NO
                                            scrollPosition:UICollectionViewScrollPositionCenteredHorizontally];
    }
}

- (IBAction)onCancel:(id)sender {
    [self animateHide];
}

-(void) animateOpen {
    
    self.backgroundView.alpha = 0.3f;
    self.backgroundView.frame = CGRectMake(-self.originalRect.size.width, self.originalRect.size.height + self.originalRect.origin.y, 0, 0);
    
    [UIView animateWithDuration:.3
                          delay:0.0
                        options:UIViewAnimationOptionCurveEaseOut
                     animations:^(){
                         self.backgroundView.frame = self.originalRect;
                         self.backgroundView.alpha = 1.0f;
                     }
                     completion:^(BOOL success) {
                         
                     }];
    
}

-(void) animateHide {
    
    [UIView animateWithDuration:.3
                          delay:0.0
                        options:UIViewAnimationOptionCurveEaseOut
                     animations:^(){
                         self.backgroundView.alpha = 0.3f;
                         self.backgroundView.frame = CGRectMake(-(self.backgroundView.frame.origin.x + self.backgroundView.frame.size.width), self.backgroundView.frame.origin.y + self.backgroundView.frame.size.height, 0, 0);
                     }
                     completion:^(BOOL success) {
                         self.dismiss();
                     }];
}


- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.stickerList.stickers.count;
}

- (NSInteger)numberOfSectionsInCollectionView: (UICollectionView *)collectionView {
    return 1;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    if (collectionView.tag == 0) {
        CSStickerPageCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"page" forIndexPath:indexPath];
        CSStickerPageModel *sticker = [self.stickerList.stickers objectAtIndex:indexPath.row];
        cell.delegate = self;
        cell.model = sticker;
        return cell;
    }
    else if (collectionView.tag == 1) {
        CSStickerCategoryCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"cell" forIndexPath:indexPath];
        CSStickerPageModel *sticker = [self.stickerList.stickers objectAtIndex:indexPath.row];
        [cell.imageView sd_setImageWithURL:[NSURL URLWithString:sticker.mainPic]];
        return cell;
    }
    return [collectionView dequeueReusableCellWithReuseIdentifier:@"cell" forIndexPath:indexPath];
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    if (collectionView.tag == 0) {
        return self.collectionView.frame.size;
    }
    else if (collectionView.tag == 1) {
        return CGSizeMake(50, 50);
    }
    return CGSizeMake(60, 60);
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    if (collectionView.tag == 1) {
        [self.collectionView scrollToItemAtIndexPath:[NSIndexPath indexPathForRow:indexPath.row inSection:0]
                                    atScrollPosition:UICollectionViewScrollPositionCenteredHorizontally
                                            animated:NO];
    }
}

-(void)scrollViewDidScroll:(UIScrollView *)scrollView {
    if (scrollView.tag == 0) {
        self.selectedStickerCollection = self.collectionView.contentOffset.x / self.collectionView.frame.size.width;
        NSIndexPath *selection = [NSIndexPath indexPathForItem:self.selectedStickerCollection
                                                     inSection:0];
        [self.categoryCollectionView selectItemAtIndexPath:selection
                                                  animated:NO
                                            scrollPosition:UICollectionViewScrollPositionCenteredHorizontally];
    }
}

-(void)onSticker:(NSString *)stickerUrl {
    if (self.delegate) {
        [self.delegate onSticker:stickerUrl];
    }
    [self animateHide];
}

@end
