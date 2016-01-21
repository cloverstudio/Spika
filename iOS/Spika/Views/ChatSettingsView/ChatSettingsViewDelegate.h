//
//  ChatSettingsViewDelegate.h
//  Prototype
//
//  Created by Ivo Peric on 27/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#ifndef Spika_CSChatSettingsViewDelegate_h
#define Spika_CSChatSettingsViewDelegate_h

@class CSChatSettingsView;
@protocol CSChatSettingsViewDelegate <NSObject>

@optional
-(void) onSettingsClickedPosition: (NSInteger) position;
-(void) onDismissClicked;
@end


#endif
