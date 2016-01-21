//
//  ChatSettingsView.m
//  Prototype
//
//  Created by Ivo Peric on 27/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#import "CSChatSettingsView.h"
#import "CSConfig.h"

@interface CSChatSettingsView() <UITableViewDelegate, UITableViewDataSource>

@end

@implementation CSChatSettingsView
@synthesize delegate;

- (id)initWithCoder:(NSCoder *)aDecoder {
    
    if (self = [super initWithCoder:aDecoder]) {
        [self initializeView];
    }
    return self;
}

- (id)initWithFrame:(CGRect)frame {
    
    if (self = [super initWithFrame:frame]) {
        [self initializeView];
    }
    return self;
}

- (void)initializeView {
    
    NSString *className = NSStringFromClass([self class]);
    className = [className stringByReplacingOccurrencesOfString:[[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleExecutable"] withString:@""];
    className = [className stringByReplacingOccurrencesOfString:@"." withString:@""];
    
    NSString *path = [[NSBundle mainBundle] pathForResource:className ofType:@"nib"];
    if (![[NSFileManager defaultManager] fileExistsAtPath:path]) {
        return;
    }
    
    NSArray *array = [[NSBundle mainBundle] loadNibNamed:className
                                                   owner:self
                                                 options:nil];
    NSAssert(array.count == 1, @"Invalid number of nibs");
    [self addSubview:array[0]];
    
    _contentView.backgroundColor = self.backgroundColor;
    
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(imageViewTapped:)];
    [self.viewForDismiss addGestureRecognizer:tap];
    
}

- (void)imageViewTapped:(UITapGestureRecognizer *)gr {
    if ([(NSObject *)delegate respondsToSelector:@selector(onDismissClicked)]) {
        [self.delegate onDismissClicked];
    }
}

-(void)viewDidLayoutSubviews{
    
    [self.tableView setSeparatorInset:UIEdgeInsetsZero];
    if ([self.tableView respondsToSelector:@selector(setLayoutMargins:)]) {
        [self.tableView setLayoutMargins:UIEdgeInsetsZero];
    }
    
}

- (void)setFrame:(CGRect)frame {
    
    [super setFrame:frame];
    
    _contentView.frame = self.bounds;
}

- (void)layoutSubviews {
    
    [super layoutSubviews];
    
    _contentView.frame = self.bounds;
}

#pragma mark - Setters

- (void)setBackgroundColor:(UIColor *)backgroundColor {
    
    [super setBackgroundColor:backgroundColor];
    
    _contentView.backgroundColor = backgroundColor;
}

- (void)setItems:(NSArray *)items {
    
    _items = [NSArray arrayWithArray:items];
    self.heightOfTable.constant = [self menuHeight];
    [_tableView reloadData];
}

- (CGFloat)menuHeight {
    
    CGFloat height = 0.0f;
    UITableView *tableView = _tableView;
    NSUInteger count = [self tableView:tableView numberOfRowsInSection:0];
    for (int i=0; i<count; i++) {
        height += [self tableView:tableView heightForRowAtIndexPath:[NSIndexPath indexPathForRow:i inSection:0]];
    }

    return height;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return _items.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    return 50;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:kAppChatSettingsTable];
    
    if (cell == nil) {
        
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault
                                      reuseIdentifier:kAppChatSettingsTable];
        cell.textLabel.text = [_items objectAtIndex:indexPath.row];
        cell.textLabel.textAlignment = NSTextAlignmentCenter;
    }
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{

    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    if ([(NSObject *)delegate respondsToSelector:@selector(onSettingsClickedPosition:)]) {
        [self.delegate onSettingsClickedPosition:indexPath.row];
    }

}

-(void) animateTableViewToOpen:(BOOL) toOpen withMenu:(CSChatSettingsView*) menu{
    
    if(toOpen){
        
        if(CGRectIsEmpty(self.originalRect)){
        
            self.originalRect = self.tableView.frame;
        
        }
        
        menu.hidden = NO;
        self.tableView.frame = CGRectMake(self.originalRect.origin.x, self.originalRect.origin.y , self.originalRect.size.width, 0);
        [UIView animateWithDuration:0.3 delay:0.0 options:UIViewAnimationOptionCurveEaseOut
                         animations:^(){
                             self.tableView.frame = CGRectMake(self.tableView.frame.origin.x, self.tableView.frame.origin.y , self.tableView.frame.size.width, self.originalRect.size.height);
                             
                         }
                         completion:nil];
    }else{
        
        [UIView animateWithDuration:0.3 delay:0.0 options:UIViewAnimationOptionCurveEaseOut
                         animations:^(){
                             self.tableView.frame = CGRectMake(self.tableView.frame.origin.x, self.tableView.frame.origin.y , self.tableView.frame.size.width, 0);
                             
                         }
                         completion:^(BOOL b){
                            menu.hidden = YES;
                         }];

    }

}


@end
