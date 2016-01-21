//
//  MenuView.m
//  Prototype
//
//  Created by Ivo Peric on 02/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSMenuView.h"
#import "CSConfig.h"

@interface CSMenuView () <UIGestureRecognizerDelegate>

@property (nonatomic) CGRect originalRect;

@end

@implementation CSMenuView
@synthesize delegate;

-(void) initializeInView:(UIView *)parentView dismiss:(dismissMenu)dismiss {
    
    self.dismiss = dismiss;

    self.frame = CGRectMake(0, 0, parentView.frame.size.width, parentView.frame.size.height);
    UIView *view = [[[NSBundle mainBundle] loadNibNamed:NSStringFromClass([self class])
                                                  owner:self
                                                options:nil] objectAtIndex:0];
    view.frame = self.bounds;
    [self addSubview:view];
        
    self.backgroundView.layer.cornerRadius = 10;
    self.backgroundView.layer.masksToBounds = YES;
    
    self.cameraButton.hidden = YES;
    self.galleryButton.hidden = YES;
    self.locationButton.hidden = YES;
    self.fileButton.hidden = YES;
    self.videoButton.hidden = YES;
    self.contactButton.hidden = YES;
        
    UITapGestureRecognizer *singleFingerTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleSingleTap:)];
    [self addGestureRecognizer:singleFingerTap];
    
    UITapGestureRecognizer *singleFingerTap2 = [[UITapGestureRecognizer alloc] initWithTarget:nil action:nil];
    [self.backgroundView addGestureRecognizer:singleFingerTap2];
    
    self.originalRect = self.backgroundView.frame;
    
    [parentView addSubview:self];
    [self animateOpen];
}

-(void) animateOpen{

    self.backgroundView.alpha = 0.3f;
    self.backgroundView.frame = CGRectMake((self.originalRect.size.width + 8), self.originalRect.size.height + self.originalRect.origin.y, 0, 0);
    
    [UIView animateWithDuration:.3 delay:0.0 options:UIViewAnimationOptionCurveEaseOut
                     animations:^(){
                         self.backgroundView.frame = self.originalRect;
                         self.backgroundView.alpha = 1.0f;
                     }
                     completion:^(BOOL success){
                         [self animateButtons];
                     }];

}

-(void) animateHide{
    
    [self animateHideButtons];
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [UIView animateWithDuration:.3 delay:0.0 options:UIViewAnimationOptionCurveEaseOut
                         animations:^(){
                             self.backgroundView.alpha = 0.3f;
                             self.backgroundView.frame = CGRectMake(self.backgroundView.frame.origin.x + self.backgroundView.frame.size.width, self.backgroundView.frame.origin.y + self.backgroundView.frame.size.height, 0, 0);
                         }
                         completion:^(BOOL success){
                             [self hideButtons];
                             self.dismiss();
                         }];
    });
    
}

-(void) animateButtons{

    [self singleButtonAnimation:self.cameraButton withDelay:.0];
    [self singleButtonAnimation:self.galleryButton withDelay:.05];
    [self singleButtonAnimation:self.locationButton withDelay:.1];
    [self singleButtonAnimation:self.fileButton withDelay:.15];
    [self singleButtonAnimation:self.videoButton withDelay:.2];
    [self singleButtonAnimation:self.contactButton withDelay:.25];
    
}

-(void) animateHideButtons{
    
    [self singleButtonHideAnimation:self.cameraButton withDelay:.25];
    [self singleButtonHideAnimation:self.galleryButton withDelay:.2];
    [self singleButtonHideAnimation:self.locationButton withDelay:.15];
    [self singleButtonHideAnimation:self.fileButton withDelay:.1];
    [self singleButtonHideAnimation:self.videoButton withDelay:.05];
    [self singleButtonHideAnimation:self.contactButton withDelay:.0];
    
}

-(void) hideButtons{

    self.cameraButton.hidden = YES;
    self.galleryButton.hidden = YES;
    self.locationButton.hidden = YES;
    self.fileButton.hidden = YES;
    self.videoButton.hidden = YES;
    self.contactButton.hidden = YES;
    
}

-(void) singleButtonAnimation:(UIButton*) button withDelay:(float) delay{

    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(delay * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        button.hidden = NO;
    });

    button.transform = CGAffineTransformMakeScale(0.1, 0.1);
    
    button.alpha = 0.3f;
    
    [UIView animateWithDuration:.3 delay:delay options:UIViewAnimationOptionCurveEaseOut
                     animations:^(){
                         button.alpha = 0.8f;
                         button.transform = CGAffineTransformMakeScale(1.1, 1.1);
                     }
                     completion:^(BOOL success){
                         [UIView animateWithDuration:.1 delay:0.0 options:UIViewAnimationOptionCurveEaseOut
                                          animations:^(){
                                              button.alpha = 1.0f;
                                              button.transform = CGAffineTransformMakeScale(1.0, 1.0);
                                          }
                                          completion:nil];
                     }];

}

-(void) singleButtonHideAnimation:(UIButton*) button withDelay:(float) delay{
    
    button.transform = CGAffineTransformMakeScale(1.0, 1.0);
    
    [UIView animateWithDuration:.1 delay:delay options:UIViewAnimationOptionCurveEaseOut
                     animations:^(){
                         button.alpha = 0.8f;
                         button.transform = CGAffineTransformMakeScale(1.1, 1.1);
                     }
                     completion:^(BOOL success){
                         [UIView animateWithDuration:.3 delay:0.0 options:UIViewAnimationOptionCurveEaseOut
                                          animations:^(){
                                              button.alpha = .0f;
                                              button.transform = CGAffineTransformMakeScale(0.1, 0.1);
                                          }
                                          completion:^(BOOL success){
                                              button.hidden = YES;
                                          }];
                     }];
    
}

- (void)handleSingleTap:(UITapGestureRecognizer *)recognizer {
    [self animateHide];
}

- (IBAction)onCamera:(id)sender {
    [self.delegate onCamera];
}

- (IBAction)onGallery:(id)sender {
    [self.delegate onGallery];
}

- (IBAction)onLocation:(id)sender {
    [self.delegate onLocation];
}

- (IBAction)onFile:(id)sender {
    [self.delegate onFile];
}

- (IBAction)onVideo:(id)sender {
    [self.delegate onVideo];
}

- (IBAction)onContact:(id)sender {
    [self.delegate onContact];
}

- (IBAction)onAudio:(id)sender {
    [self.delegate onAudio];
}

- (IBAction)onCancel:(id)sender {
    [self animateHide];
}

@end
