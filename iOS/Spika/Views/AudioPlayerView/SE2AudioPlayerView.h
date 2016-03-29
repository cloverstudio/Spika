//
//  SE2AudioPlayerView.h
//  ios-v2-spika-enterprise
//
//  Created by CloverField on 10/03/16.
//  Copyright © 2016 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
@class SE2MessageModel;

@protocol SE2AudioPlayerViewDelegate <NSObject>

-(void)viewInChatSelected;

@end

@interface SE2AudioPlayerView : UIView

@property (weak, nonatomic) IBOutlet UIButton *playButton;
@property (weak, nonatomic) IBOutlet UIView *durationHolderView;
@property (weak, nonatomic) IBOutlet UILabel *durationLabel;
@property (weak, nonatomic) IBOutlet UISlider *durationSlider;
@property (weak, nonatomic) IBOutlet UIView *separator;

- (IBAction)onPlay:(id)sender;
- (IBAction)onSlider:(id)sender;

-(id)initWithFrame:(CGRect)frame url:(NSString*)url;

@property (nonatomic, weak) id<SE2AudioPlayerViewDelegate> delegate;

@end
