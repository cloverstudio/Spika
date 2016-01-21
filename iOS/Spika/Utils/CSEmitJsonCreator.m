//
//  EmitJsonCreator.m
//  Prototype
//
//  Created by Ivo Peric on 07/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSEmitJsonCreator.h"

@implementation CSEmitJsonCreator

+(NSDictionary*) createEmitSendMessage:(CSMessageModel*) message andUser: (CSUserModel*) user andMessage:(NSString*) messageTxt andFile:(CSFileModel *)file andLocation:(CSLocationModel*) locationModel{
    
    NSMutableDictionary *params = [NSMutableDictionary new];
    
    [params setValue:user.roomID forKey:paramRoomID];
    [params setValue:user.userID forKey:paramUserID];
    [params setValue:message.type forKey:paramType];
    [params setValue:message.localID forKey:paramLocalID];
    [params setValue:messageTxt forKey:paramMessage];
    
    if(file){
        
        NSMutableDictionary *fileDict = [NSMutableDictionary new];
        
        NSDictionary *fileInside = @{
                                     paramId : file.file.id,
                                     paramName : file.file.name,
                                     paramSize : file.file.size,
                                     paramMimeType : file.file.mimeType
                                     };
        
        [fileDict setValue:fileInside forKey:paramFile];
        
        if(file.thumb){
            NSDictionary *thumbInside = @{
                                          paramId : file.thumb.id,
                                          paramName : file.thumb.name,
                                          paramSize : file.thumb.size,
                                          paramMimeType : file.thumb.mimeType
                                          };
            
            [fileDict setValue:thumbInside forKey:paramThumb];
        }
        
        [params setValue:fileDict forKey:paramFile];
        
    }
    
    if(locationModel) {
        NSDictionary* locationDict =@{
                                      paramLat : [[NSNumber alloc] initWithDouble:locationModel.lat],
                                      paramLng : [[NSNumber alloc] initWithDouble:locationModel.lng]
                                      };
        
        [params setValue:locationDict forKey:paramLocation];
    }
    
    return params;
}

@end
