//
//  FileDetailsModel.h
//  Prototype
//
//  Created by Ivo Peric on 28/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSModel.h"

@interface CSFileDetailsModel : CSModel

@property (nonatomic, strong) NSString* id;
@property (nonatomic, strong) NSString* name;
@property (nonatomic, strong) NSString* size;
@property (nonatomic, strong) NSString* mimeType;

@end
