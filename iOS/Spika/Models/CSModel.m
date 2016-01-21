//
//  Model.m
//  Prototype
//
//  Created by mislav on 10/07/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSModel.h"

@implementation CSModel

//+(JSONKeyMapper*)keyMapper
//{
//    return [JSONKeyMapper mapperFromUnderscoreCaseToCamelCase];
//}

+(BOOL)propertyIsOptional:(NSString*)propertyName
{
    return YES;
}

@end
