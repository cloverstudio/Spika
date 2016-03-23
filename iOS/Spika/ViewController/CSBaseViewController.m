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
    
    [self setSelfViewSizeFromViewController:[[UIScreen mainScreen] bounds]];
    
    // Do any additional setup after loading the view.
    
    self.edgesForExtendedLayout = UIRectEdgeNone;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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

@end
