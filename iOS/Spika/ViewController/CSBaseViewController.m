//
//  BaseViewController.m
//  Prototype
//
//  Created by Ivo Peric on 27/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSBaseViewController.h"
#import "CSConfig.h"
#import "CSResponseModel.h"
#import "CSChatErrorCodes.h"

@interface CSBaseViewController ()

@property UIActivityIndicatorView *loadingIndicator;
@property UIView *indicatorBackground;
@property CGRect sizeOfView;

@end

@implementation CSBaseViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _manager = [AFHTTPRequestOperationManager manager];
    _manager.responseSerializer = [AFJSONResponseSerializer serializer];
    _manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [_manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [_manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    
    [self setSelfViewSizeFromViewController:[[UIScreen mainScreen] bounds]];
    
    // Do any additional setup after loading the view.
    
    self.edgesForExtendedLayout = UIRectEdgeNone;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void) apiGETCallWithURL: (NSString*) url completition: (apiCallFinish) finish{
    [self apiGETCallWithURL:url toShowIndicator:YES toHideIndicator:YES completition:finish];
}

-(void) apiGETCallWithURL: (NSString*) url toShowIndicator: (BOOL) toShow toHideIndicator: (BOOL) toHide completition: (apiCallFinish) finish {
    if(toShow){
        [self showIndicator];
    }
    [self.manager GET:url
           parameters:nil
              success:^(AFHTTPRequestOperation *operation, id responseObject) {
                  
                  if(toHide){
                      [self hideIndicator];
                  }
                  
                  CSResponseModel *responseModel = [[CSResponseModel alloc] initWithDictionary:responseObject error:nil];
                  
                  if (responseModel.code.intValue > 1) {
                      NSLog(@"fail client");
                      UIAlertView* alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                                      message:[CSChatErrorCodes errorForCode:responseModel.code]
                                                                     delegate:nil
                                                            cancelButtonTitle:@"OK"
                                                            otherButtonTitles:nil, nil];
                      [alert show];
                      return;
                  }
                  NSLog(@"success %@", url);
                  
                  finish(responseModel);
                  
              } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
                  [self hideIndicator];
                  
                  UIAlertView* alert = [[UIAlertView alloc] initWithTitle:@"Error" message:error.localizedDescription delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                  [alert show];
                  NSLog(@"fail %@", url);
              }];
    
}

-(void) apiPOSTCallWithUrl: (NSString*) url parameters: (NSDictionary*) params completition: (apiCallFinish) finish{
    [self apiPOSTCallWithUrl:url parameters:params toShowIndicator:YES toHideIndicator:YES completition:finish];
}

-(void) apiPOSTCallWithUrl: (NSString*) url parameters: (NSDictionary*) params toShowIndicator: (BOOL) toShow toHideIndicator: (BOOL) toHide completition:(apiCallFinish)finish {
    if(toShow){
        [self showIndicator];
    }
    [self.manager POST:url
            parameters:params
               success:^(AFHTTPRequestOperation *operation, id responseObject) {
                   
                   if(toHide){
                       [self hideIndicator];
                   }
                   
                   CSResponseModel *responseModel = [[CSResponseModel alloc] initWithDictionary:responseObject error:nil];
                   
                   if (responseModel.code.intValue > 1) {
                       NSLog(@"fail client");
                       UIAlertView* alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                                       message:[CSChatErrorCodes errorForCode:responseModel.code]
                                                                      delegate:nil
                                                             cancelButtonTitle:@"OK"
                                                             otherButtonTitles:nil, nil];
                       [alert show];
                       return;
                   }
                   NSLog(@"success %@", url);
                   
                   finish(responseModel);

                   
               } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
                   
                   [self hideIndicator];
                   
                   UIAlertView* alert = [[UIAlertView alloc] initWithTitle:@"Error" message:error.localizedDescription delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                   [alert show];
                   
                   NSLog(@"fail %@", url);
               }];

}

-(void) setSelfViewSizeFromViewController: (CGRect) frame{
    self.sizeOfView = frame;
}

-(void) showIndicator{

    if(!self.loadingIndicator ){
        
        self.indicatorBackground = [[UIView alloc] init];
        self.indicatorBackground.frame = self.sizeOfView;
        self.indicatorBackground.backgroundColor = kAppBlackColor(0.4);
        [self.view addSubview:self.indicatorBackground];
        [self.indicatorBackground bringSubviewToFront:self.view];
        
        self.loadingIndicator = [[UIActivityIndicatorView alloc]initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
        self.loadingIndicator.frame = CGRectMake(0.0, 0.0, 40.0, 40.0);
        self.loadingIndicator.center = self.indicatorBackground.center;
        [self.indicatorBackground addSubview:self.loadingIndicator];
        [self.loadingIndicator bringSubviewToFront:self.indicatorBackground];
        [UIApplication sharedApplication].networkActivityIndicatorVisible = TRUE;
    }
    
    self.loadingIndicator.hidden = NO;
    [self.loadingIndicator startAnimating];
    [self.loadingIndicator setTag:1];
    
}

-(void) hideIndicator{
    
    if(self.loadingIndicator){
    
        [self.loadingIndicator setTag:0];
        [self.loadingIndicator stopAnimating];
        [UIApplication sharedApplication].networkActivityIndicatorVisible = FALSE;
        
        self.indicatorBackground.hidden = YES;
        
    }
    
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
