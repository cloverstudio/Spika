//
//  JSONValueTransformer+CustomTransformer.m
//  Prototype
//
//  Created by mislav on 15/07/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "JSONValueTransformer+CustomTransformer.h"

@implementation JSONValueTransformer (CustomTransformer)


//* Array Strong Typing

- (NSArray *)NSArrayFromNSString:(NSString*)string {
    return nil;
}

- (NSArray *)NSArrayFromNSNumber:(NSNumber*)number {
    return nil;
}

- (NSArray *)NSArrayFromNSDictionary:(NSDictionary*)dictionary {
    return dictionary.allValues;
}


//* MutableArray Strong Typing

- (NSMutableArray *)NSMutableArrayFromNSString:(NSString*)string {
    return nil;
}

- (NSMutableArray *)NSMutableArrayFromNSNumber:(NSNumber*)number {
    return nil;
}

- (NSMutableArray *)NSMutableArrayFromNSDictionary:(NSDictionary*)dictionary {
    return [NSMutableArray arrayWithArray:dictionary.allValues];
}

//* NSString Strong Typing

- (NSString *)NSStringFromNSArray:(NSArray*)array {
    return nil;
}

- (NSString *)NSStringFromNSDictionary:(NSDictionary*)dictionary {
    return nil;
}

//* NSNumber Strong Typing

- (NSNumber *)NSNumberFromNSArray:(NSArray*)array {
    return nil;
}

- (NSNumber *)NSNumberFromNSDictionary:(NSDictionary*)dictionary {
    return nil;
}

@end
