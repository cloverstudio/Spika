//
//  FileModel.h
//  Prototype
//
//  Created by mislav on 14/07/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSModel.h"
#import "CSFileDetailsModel.h"

@interface CSFileModel : CSModel

@property (nonatomic, strong) CSFileDetailsModel *file;
@property (nonatomic, strong) CSFileDetailsModel *thumb;

@end
