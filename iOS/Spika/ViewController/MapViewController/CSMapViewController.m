//
//  MapViewController.m
//  Prototype
//
//  Created by Ivo Peric on 07/09/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSMapViewController.h"
#import "CSConfig.h"

@interface CSMapViewController () <CLLocationManagerDelegate>

@end

@implementation CSMapViewController

-(id) initWithMessage: (CSMessageModel*) message{
    if (self = [super init]) {
        _message = message;
        _heightForButtonsLayout = 0;
    }
    return self;
}

-(id) initToPostLocation{
    if (self = [super init]) {
        _heightForButtonsLayout = 50;
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];

    [_mkMap setMapType:MKMapTypeStandard];
    [_mkMap setZoomEnabled:YES];
    [_mkMap setScrollEnabled:YES];
    
    if(_message){
        [self addLocationFromMessage];
    }else{
        [self setMyLocation];
    }
    
    [_mkMap setDelegate:self];
    
    self.buttonsLayoutHeight.constant = _heightForButtonsLayout;
    
    self.edgesForExtendedLayout = UIRectEdgeNone;
}

-(void)viewWillAppear:(BOOL)animated{
    self.title = @"Location";
}

-(void)dealloc{
    _mkMap.delegate = nil;
    
    [_locationManager stopUpdatingLocation];
    _locationManager.delegate = nil;
}

-(void) addLocationFromMessage{
    MKCoordinateRegion region = { {0.0, 0.0}, {0.0,0.0}};
    region.center.latitude = _message.location.lat;
    region.center.longitude = _message.location.lng;
    region.span.latitudeDelta = 0.01f;
    region.span.longitudeDelta = 0.01f;
    
    [self showPinToLocation:region title:_message.message];
}

-(void) showPinToLocation: (MKCoordinateRegion) region title: (NSString*) title{
    [_mkMap setRegion:region animated:YES];
    
    _annotation = [[CSDisplayMap alloc] init];
    _annotation.title=title;
    _annotation.coordinate = region.center;
    [_mkMap addAnnotation:_annotation];
}

-(void) setMyLocation{
    
    if(nil == _locationManager){
        _locationManager = [[CLLocationManager alloc] init];
    }
    _locationManager.delegate = self;
    _locationManager.desiredAccuracy = kCLLocationAccuracyKilometer;
    _locationManager.distanceFilter = 500;
    
    [_locationManager startUpdatingLocation];
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0){
        [self.locationManager requestWhenInUseAuthorization];
    }
    
    UILongPressGestureRecognizer *gesture = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(onUserTouchMap:)];
    gesture.minimumPressDuration = 0.1f;
    [_mkMap addGestureRecognizer:gesture];
}

- (void)onUserTouchMap:(UIGestureRecognizer *)gestureRecognizer{
    
    if (gestureRecognizer.state != UIGestureRecognizerStateBegan) {
        return;
    }
    
    CGPoint touchPoint = [gestureRecognizer locationInView:_mkMap];
    CLLocationCoordinate2D touchMapCoordinate = [_mkMap convertPoint:touchPoint toCoordinateFromView:_mkMap];
    
    [_mkMap removeAnnotation:_annotation];
    [_annotation setCoordinate:touchMapCoordinate];
    [_mkMap addAnnotation:_annotation];
    
    [_mkMap selectAnnotation:_annotation animated:YES];
    
    CLLocation *oldLocation = [[CLLocation alloc] initWithCoordinate:_myLocation.coordinate altitude:_myLocation.altitude horizontalAccuracy:_myLocation.horizontalAccuracy verticalAccuracy:_myLocation.verticalAccuracy timestamp:_myLocation.timestamp];
    
    _myLocation = [[CLLocation alloc] initWithCoordinate:touchMapCoordinate altitude:oldLocation.altitude horizontalAccuracy:oldLocation.horizontalAccuracy verticalAccuracy:oldLocation.verticalAccuracy timestamp:oldLocation.timestamp];

}

-(void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations{
    
    if(_myLocation){
        return;
    }
    
    _myLocation = [locations lastObject];
    
    MKCoordinateRegion region;
    region.center = _myLocation.coordinate;
    region.span = MKCoordinateSpanMake(0.1f, 0.1f);
    
    region = [self.mkMap regionThatFits:region];
    [self.mkMap setRegion:region animated:YES];
    
    [self showPinToLocation:region title:@"Location"];
    
}

-(void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error{
    NSLog(@"ERROR");
}

-(MKAnnotationView *)mapView:(MKMapView *)mV viewForAnnotation:
(id <MKAnnotation>)annotation {
    MKPinAnnotationView *pinView = nil;
    if(annotation != _mkMap.userLocation)
    {
        static NSString *defaultPinID = @"com.invasivecode.pin";
        pinView = (MKPinAnnotationView *)[_mkMap dequeueReusableAnnotationViewWithIdentifier:defaultPinID];
        if ( pinView == nil ){
            pinView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:defaultPinID];
        }
        
        pinView.pinColor = MKPinAnnotationColorPurple;
        pinView.canShowCallout = YES;
        pinView.animatesDrop = YES;
    }
    else {
        [_mkMap.userLocation setTitle:_message.message];
    }
    return pinView;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)onOkClicked:(id)sender {
    CSLocationModel *location = [CSLocationModel new];
    location.lat = _myLocation.coordinate.latitude;
    location.lng = _myLocation.coordinate.longitude;
    
    CLGeocoder *geoCoder = [CLGeocoder new];
    
    [geoCoder reverseGeocodeLocation:_myLocation completionHandler:^(NSArray *placemarks, NSError *error) {
        if([placemarks count] > 0){
            CLPlacemark *place = [placemarks lastObject];
            
            NSArray *lines = place.addressDictionary[ @"FormattedAddressLines"];
            NSString *addressString = [lines componentsJoinedByString:@", "];
            
            NSDictionary *dict = @{
                                   paramLocation : location,
                                   paramAddress : addressString
                                   };

            [[NSNotificationCenter defaultCenter] postNotificationName:kAppLocationSelectedNotification
                                                                object:nil
                                                              userInfo:dict];
            
            [self.navigationController popViewControllerAnimated:YES];

        }
    }];
    
}

- (IBAction)onCancelClicked:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}
@end
