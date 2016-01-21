//
//  Utils.m
//  Prototype
//
//  Created by Ivo Peric on 27/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSUtils.h"
#import "CSSeenByModel.h"
#import "CSConfig.h"
#import "CSFileModel.h"
#import "CSCustomConfig.h"

@implementation CSUtils

+(CGFloat)getWidthForLabel:(UILabel*)label withFrameSizeWidth: (CGFloat) maxWidth
{
    
    CGSize maximumSize = CGSizeMake(maxWidth, FLT_MAX);
    UIFont *newFont = [UIFont fontWithName:label.font.fontName size:label.font.pointSize+2];
    NSDictionary *stringAttributes = [NSDictionary dictionaryWithObject:newFont forKey: NSFontAttributeName];
    
    CGSize textViewSize = [label.text boundingRectWithSize:maximumSize
                                                   options:NSStringDrawingTruncatesLastVisibleLine|NSStringDrawingUsesLineFragmentOrigin
                                                attributes:stringAttributes
                                                   context:nil].size;
    label.numberOfLines = textViewSize.height/label.font.lineHeight;
    
    return textViewSize.width;
}

+(CGFloat)getWidthOneLineForLabel:(UILabel*)label withFrameSizeWidth: (CGFloat) maxWidth
{
    
    CGSize maximumSize = CGSizeMake(maxWidth, FLT_MAX);
    UIFont *newFont = [UIFont fontWithName:label.font.fontName size:label.font.pointSize+2];
    NSDictionary *stringAttributes = [NSDictionary dictionaryWithObject:newFont forKey: NSFontAttributeName];
    
    CGSize textViewSize = [label.text boundingRectWithSize:maximumSize
                                                   options:NSStringDrawingTruncatesLastVisibleLine|NSStringDrawingUsesLineFragmentOrigin
                                                attributes:stringAttributes
                                                   context:nil].size;
    label.numberOfLines = 1;
    
    return textViewSize.width;
}

+(NSString *)generateDownloadURLFormFileId:(NSString *)fileId{

    return [NSString stringWithFormat:@"%@%@/%@", [CSCustomConfig sharedInstance].server_url, kAppDownloadSufix, fileId];

}

+(BOOL)isMessageAnImage:(CSMessageModel *)message{
    
    if([message.file.file.mimeType isEqualToString:kAppImagePNG]) return true;
    else if ([message.file.file.mimeType isEqualToString:kAppImageJPG]) return true;
    else if ([message.file.file.mimeType isEqualToString:kAppImageGIF]) return true;
    return false;
    
}

+(BOOL)isMessageAVideo:(CSMessageModel *)message{
    
    if([message.file.file.mimeType isEqualToString:kAppVideoMP4]) return true;
    return false;
    
}

+(BOOL)isMessageAAudio:(CSMessageModel *)message{
    
    if([message.file.file.mimeType isEqualToString:kAppAudioMP3]) return true;
    else if ([message.file.file.mimeType isEqualToString:kAppAudioWAV]) return true;
    return false;
    
}

+(NSArray *)generateUnSeenMessageIdsFrom:(NSArray *)messages andActiveUser:(CSUserModel *)user{

    NSMutableArray* unSeenIds = [NSMutableArray new];
    
    for(CSMessageModel* item in messages){
    
        BOOL seen = NO;
        
        if([user.userID isEqualToString:item.user.userID]){
            seen = true;
        }else{
            for(NSDictionary *dict in item.seenBy){
                NSError* error;
                CSSeenByModel* model = [[CSSeenByModel alloc] initWithDictionary:dict error:&error];
                if([model.user.userID isEqualToString:user.userID]){
                    seen = true;
                    continue;
                }
            }
        }
        
        if(!seen){
            [unSeenIds addObject:item._id];
        }
    
    }
    
    return unSeenIds;
    
}

+(NSString*) readableFileSize: (NSString *) size{
    
    double convertedValue = [size doubleValue];
    int multiplyFactor = 0;
    
    NSArray *tokens = @[@"B",@"KB",@"MB",@"GB",@"TB"];
    
    while (convertedValue > 1024) {
        convertedValue /= 1024;
        multiplyFactor++;
    }
    
    return [NSString stringWithFormat:@"%4.2f %@",convertedValue, tokens[multiplyFactor]];
}

+(NSString*) getFileFromFileModel: (CSFileModel*) file{
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    return [[paths objectAtIndex:0] stringByAppendingPathComponent:[NSString stringWithFormat:@"%@_%@", file.file.id, [file.file.name stringByReplacingOccurrencesOfString:@" " withString:@"_"]]];
    
}

+(BOOL) isFileExistsWithFileName: (NSString*) path{
    
    if (![[NSFileManager defaultManager] fileExistsAtPath:path]) {
        [[NSFileManager defaultManager] createFileAtPath:path
                                                contents:nil
                                              attributes:nil];
        
        return false;
        
    }

    NSError *erro;
    NSDictionary *fileAttributes = [[NSFileManager defaultManager] attributesOfItemAtPath:path error:&erro];
    NSNumber *fileSizeNumber = [fileAttributes objectForKey:NSFileSize];
    long long fileSize = [fileSizeNumber longLongValue];
    
    if(fileSize < 100) return false;
    
    return true;
    
}

+(UIImage*) resizeImage : (UIImage*) image toSize: (CGSize) newSize{
    UIGraphicsBeginImageContext( newSize );
    [image drawInRect:CGRectMake(0,0,newSize.width,newSize.height)];
    UIImage* newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return newImage;
}

@end
