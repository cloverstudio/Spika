//
//  MenuView.h
//  Prototype
//
//  Created by Ivo Peric on 02/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CSMenuViewDelegate.h"

typedef void(^dismissMenu)(void);

@interface CSMenuView : UIView

@property (weak, nonatomic) IBOutlet UIButton *cameraButton;
@property (weak, nonatomic) IBOutlet UIButton *galleryButton;
@property (weak, nonatomic) IBOutlet UIButton *locationButton;
@property (weak, nonatomic) IBOutlet UIButton *fileButton;
@property (weak, nonatomic) IBOutlet UIButton *videoButton;
@property (weak, nonatomic) IBOutlet UIButton *contactButton;
@property (weak, nonatomic) IBOutlet UIView *backgroundView;

- (IBAction)onCamera:(id)sender;
- (IBAction)onGallery:(id)sender;
- (IBAction)onLocation:(id)sender;
- (IBAction)onFile:(id)sender;
- (IBAction)onVideo:(id)sender;
- (IBAction)onContact:(id)sender;
- (IBAction)onAudio:(id)sender;
- (IBAction)onCancel:(id)sender;

@property (nonatomic, strong) dismissMenu dismiss;
@property (nonatomic, weak) id <CSMenuViewDelegate> delegate;

-(void) initializeInView:(UIView *)parentView dismiss:(dismissMenu)dismiss;
-(void) animateOpen;
-(void) animateHide;

@end
