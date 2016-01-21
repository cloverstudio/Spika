//
//  CellClickedDelegate.h
//  Prototype
//
//  Created by Ivo Peric on 28/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSMessageModel.h"

#ifndef Spika_CSCellClickedDelegate_h
#define Spika_CSCellClickedDelegate_h

@protocol CSCellClickedDelegate <NSObject>

@optional
-(void) onInfoClicked: (CSMessageModel*) message;
@end

#endif
