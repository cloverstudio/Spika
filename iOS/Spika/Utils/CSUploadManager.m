//
//  UploadManager.m
//  Prototype
//
//  Created by Ivo Peric on 02/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSUploadManager.h"
#import "CSConfig.h"
#import "CSProgressUploadingView.h"
#import "CSCustomConfig.h"
#import "UIAlertView+Blocks.h"

@interface CSUploadManager()

@property CSProgressUploadingView* progressView;

@end

@implementation CSUploadManager

-(void) uploadImage: (UIImage*) imageToPost fileName:(NSString*) fileName mimeType:(NSString*) mimeType viewForLoading: (UIView*) parentView completition:(fileUploadFinished) finished{
    
    if(parentView){
        _progressView = [[CSProgressUploadingView alloc] init];
        [parentView addSubview:_progressView];
    }
    
    // 1. Create `AFHTTPRequestSerializer` which will create your request.
    AFHTTPRequestSerializer *serializer = [AFHTTPRequestSerializer serializer];

    NSError * error;
    
    NSData *imageData = UIImageJPEGRepresentation(imageToPost, 1.0);
    
    NSString* url = [NSString stringWithFormat:@"%@%@", [CSCustomConfig sharedInstance].server_url, kAppUploadApi];
    
    // 2. Create an `NSMutableURLRequest`.
    NSMutableURLRequest *request =
    [serializer multipartFormRequestWithMethod:@"POST" URLString:url
                                    parameters:nil
                     constructingBodyWithBlock:^(id<AFMultipartFormData> formData) {
                         [formData appendPartWithFileData:imageData
                                                     name:paramFile
                                                 fileName:fileName
                                                 mimeType:mimeType];
                     } error:&error];
    
    // 3. Create and use `AFHTTPRequestOperationManager` to create an `AFHTTPRequestOperation` from the `NSMutableURLRequest` that we just created.
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    AFHTTPRequestOperation *operation =
    [manager HTTPRequestOperationWithRequest:request
                                     success:^(AFHTTPRequestOperation *operation, id responseObject) {
                                         if(parentView){
                                             [_progressView removeFromSuperview];
                                         }
                                         finished(responseObject);
                                     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
                                         NSLog(@"Failure %@", error.description);
                                         if(parentView){
                                             [_progressView removeFromSuperview];
                                         }
                                         [UIAlertView showWithTitle:NSLocalizedStringFromTable(@"Server Error", @"CSChatLocalization", nil)
                                                            message:error.localizedDescription
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil
                                                           tapBlock:nil
                                          ];
                                     }];
    
    // 4. Set the progress block of the operation.
    [operation setUploadProgressBlock:^(NSUInteger __unused bytesWritten,
                                        long long totalBytesWritten,
                                        long long totalBytesExpectedToWrite) {
        if(parentView){
            [_progressView changeProgressLabel:[NSString stringWithFormat:@"%lld", totalBytesWritten] max:[NSString stringWithFormat:@"%lld", totalBytesExpectedToWrite]];
            [_progressView changeProgressView:[NSString stringWithFormat:@"%f", (float)((float)totalBytesWritten / (float)totalBytesExpectedToWrite)]];
            
            if(totalBytesWritten >= totalBytesExpectedToWrite){
                [_progressView chageLabelToWaiting];
            }
        }
        
    }];
    
    // 5. Begin!
    [operation start];
}

-(void) uploadFile: (NSData*) data fileName:(NSString*) fileName mimeType:(NSString*) mimeType viewForLoading: (UIView*) parentView completition:(fileUploadFinished) finished{
    
    if(parentView){
        _progressView = [[CSProgressUploadingView alloc] init];
        [parentView addSubview:_progressView];
    }
    
    // 1. Create `AFHTTPRequestSerializer` which will create your request.
    AFHTTPRequestSerializer *serializer = [AFHTTPRequestSerializer serializer];
    
    NSError * error;
    NSString* url = [NSString stringWithFormat:@"%@%@", [CSCustomConfig sharedInstance].server_url, kAppUploadApi];
    
    // 2. Create an `NSMutableURLRequest`.
    NSMutableURLRequest *request =
    [serializer multipartFormRequestWithMethod:@"POST" URLString:url
                                    parameters:nil
                     constructingBodyWithBlock:^(id<AFMultipartFormData> formData) {
                         [formData appendPartWithFileData:data
                                                     name:paramFile
                                                 fileName:fileName
                                                 mimeType:mimeType];
                     } error:&error];
    
    // 3. Create and use `AFHTTPRequestOperationManager` to create an `AFHTTPRequestOperation` from the `NSMutableURLRequest` that we just created.
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    AFHTTPRequestOperation *operation =
    [manager HTTPRequestOperationWithRequest:request
                                     success:^(AFHTTPRequestOperation *operation, id responseObject) {
                                         if(parentView){
                                             [_progressView removeFromSuperview];
                                         }
                                         finished(responseObject);
                                     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
                                         NSLog(@"Failure %@", error.description);
                                         if(parentView){
                                             [_progressView removeFromSuperview];
                                         }
                                         [UIAlertView showWithTitle:NSLocalizedStringFromTable(@"Server Error", @"CSChatLocalization", nil)
                                                            message:error.localizedDescription
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil
                                                           tapBlock:nil
                                          ];
                                     }];
    
    // 4. Set the progress block of the operation.
    [operation setUploadProgressBlock:^(NSUInteger __unused bytesWritten,
                                        long long totalBytesWritten,
                                        long long totalBytesExpectedToWrite) {
        if(parentView){
            [_progressView changeProgressLabel:[NSString stringWithFormat:@"%lld", totalBytesWritten] max:[NSString stringWithFormat:@"%lld", totalBytesExpectedToWrite]];
            [_progressView changeProgressView:[NSString stringWithFormat:@"%f", (float)((float)totalBytesWritten / (float)totalBytesExpectedToWrite)]];
            
            if(totalBytesWritten >= totalBytesExpectedToWrite){
                [_progressView chageLabelToWaiting];
            }
        }
        
    }];
    
    // 5. Begin!
    [operation start];
}

@end
