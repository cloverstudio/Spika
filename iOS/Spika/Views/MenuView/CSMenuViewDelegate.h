//
//  MenuViewDelegate.h
//  Prototype
//
//  Created by Ivo Peric on 02/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#ifndef Spika_CSMenuViewDelegate_h
#define Spika_CSMenuViewDelegate_h

@class CSMenuView;
@protocol CSMenuViewDelegate <NSObject>

@required
-(void) onCamera;
-(void) onGallery;
-(void) onLocation;
-(void) onFile;
-(void) onVideo;
-(void) onContact;
-(void) onAudio;
@end

#endif
