//
//  Config.h
//  Prototype
//
//  Created by Ivo Peric on 25/08/15.
//  Copyright (c) 2015 Clover Studio. All rights reserved.
//

#ifndef Spika_CSConfig_h
#define Spika_CSConfig_h

#define kAppWidth [UIScreen mainScreen].bounds.size.width
#define kAppHeight [UIScreen mainScreen].bounds.size.height

#define kAppFont @"HelveticaNeue"
#define kAppFontMessageSize 17

#define kAppMaxImageScaledSize 1024.0

#define kAppGalleryType 1

//colors
#define kAppDefaultColor(__ALPHA__) [UIColor colorWithRed:0/255. green:158/255. blue:159/255. alpha:__ALPHA__]
#define kAppGrayLight(__ALPHA__) [UIColor colorWithRed:218/255. green:218/255. blue:218/255. alpha:__ALPHA__]
#define kAppInfoUserMessageColor(__ALPHA__) [UIColor colorWithRed:102/255. green:102/255. blue:102/255. alpha:__ALPHA__]
#define kAppBlackColor(__ALPHA__) [UIColor colorWithRed:0/255. green:0/255. blue:0/255. alpha:__ALPHA__]

#define kAppMessageFontColor [UIColor colorWithRed:76/255. green:76/255. blue:76/255. alpha:255/255.]
#define kAppBubbleLeftColor [UIColor colorWithRed:235/255. green:235/255. blue:235/255. alpha:255/255.]
#define kAppBubbleRightColor [UIColor colorWithRed:0/255. green:158/255. blue:159/255. alpha:255/255.]

//API
#define kAppBaseUrl @"http://ossdemo.spika.chat/spika/v1/"
#define kAppDownloadSufix @"file/download"
#define kAppUploadApi @"file/upload"
#define kAppLogin @"user/login"
#define kAppGetMessages @"message/list"
#define kAppGetLatestMessages @"message/latest"
#define kAppGetUsersInRoom @"user/list"

//API parameters
#define paramUserID @"userID"
#define paramRoomID @"roomID"
#define paramName @"name"
#define paramId @"id"
#define paramAvatarURL @"avatarURL"
#define paramType @"type"
#define paramLocalID @"localID"
#define paramMessage @"message"
#define paramMessageID @"messageID"
#define paramMessageIDs @"messageIDs"
#define paramTOKEN @"TOKEN"
#define paramCreated @"created"
#define paramFile @"file"
#define paramThumb @"thumb"
#define paramSize @"size"
#define paramMimeType @"mimeType"
#define paramResponseObject @"responseObject"
#define paramLocation @"location"
#define paramLat @"lat"
#define paramLng @"lng"
#define paramAddress @"address"

//socket
#define kAppSocketURL @"http://ossdemo.spika.chat/spika"
#define kAppSocketLogin @"login"
#define kAppSocketNewMessage @"newMessage"
#define kAppSocketSendTyping @"sendTyping"
#define kAppSocketUserLeft @"userLeft"
#define kAppSocketMessageUpdated @"messageUpdated"
#define kAppSocketSendMessage @"sendMessage"
#define kAppSocketDeleteMessage @"deleteMessage"
#define kAppSocketOpenMessage @"openMessage"
#define kAppSocketError @"socketerror"

//notification name
#define kAppDeleteMessageNotification @"deleteMessageNotification"
#define kAppFileUploadedNotification @"fileUploadedNotification"
#define kAppLocationSelectedNotification @"locationSelectedNotification"

//mime Types
#define kAppImageJPG @"image/jpeg"
#define kAppImagePNG @"image/png"
#define kAppImageGIF @"image/gif"
#define kAppVideoMP4 @"video/mp4"
#define kAppAudioWAV @"audio/wav"
#define kAppAudioMP3 @"audio/mp3"

//table view cell indentifiers
#define kAppChatSettingsTable @"settings"
#define kAppSeenByTable @"seenBy"
#define kAppMyTextMessageCell @"myTextMessage"
#define kAppUserInfoMessageCell @"userInfo"
#define kAppYourTextMessageCell @"yourTextMessage"
#define kAppYourImageMessageCell @"yourImageMessage"
#define kAppMyImageMessageCell @"myImageMessage"
#define kAppYourMediaMessageCell @"yourMediaMessage"
#define kAppMyMediaMessageCell @"myMediaMessage"
#define kAppUsersTable @"users"

//message types
#define kAppTextMessageType 1
#define kAppFileMessageType 2
#define kAppLocationMessageType 3
#define kAppContactMessageType 4
#define kAppLeaveUserMessageType 1001
#define kAppNewUserMessageType 1000
#define kAppContactType 999

//message status
#define kAppMessageStatusSent 1
#define kAppMessageStatusDelivered 2
#define kAppMessageStatusRceived 0

//typing status
#define kAppTypingStatusON 1
#define kAppTypingStatusOFF 0

//arrays
#define kAppMenuSettingsArray @[@"Users"]

#endif
