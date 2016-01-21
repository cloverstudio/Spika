//
//  MapViewController.h
//  Prototype
//
//  Created by Ivo Peric on 07/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "CSMessageModel.h"
#import "CSDisplayMap.h"

@interface CSMapViewController : UIViewController <MKMapViewDelegate>

-(id) initWithMessage: (CSMessageModel*) message;
-(id) initToPostLocation;

@property (nonatomic, strong) CSMessageModel* message;
@property (nonatomic) int heightForButtonsLayout;
@property (nonatomic, strong) CLLocationManager *locationManager;
@property (nonatomic, strong) CLLocation *myLocation;
@property (nonatomic, strong) CSDisplayMap *annotation;

@property (weak, nonatomic) IBOutlet MKMapView *mkMap;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *buttonsLayoutHeight;
- (IBAction)onOkClicked:(id)sender;
- (IBAction)onCancelClicked:(id)sender;

@end
