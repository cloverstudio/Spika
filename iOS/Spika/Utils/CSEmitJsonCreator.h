//
//  EmitJsonCreator.h
//  Prototype
//
//  Created by Ivo Peric on 07/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CSMessageModel.h"
#import "CSConfig.h"
#import "CSUserModel.h"
#import "CSFileModel.h"
#import "CSLocationModel.h"

@interface CSEmitJsonCreator : NSObject

+(NSDictionary*) createEmitSendMessage:(CSMessageModel*) message andUser: (CSUserModel*) user andMessage:(NSString*) messageTxt andFile:(CSFileModel *)file andLocation:(CSLocationModel*) locationModel;

@end
