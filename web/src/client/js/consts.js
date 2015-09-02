var Const = {
        
    EVENT_ON_MESSAGE: 'event_start_message',
    EVENT_ON_IMAGE: 'event_start_image',
    EVENT_ON_LOGIN: 'event_login',
    EVENT_ON_LOGOUT: 'event_logout',
    EVENT_ON_LOGOUT_NOTIFY: 'event_logout_notify',
    EVENT_ON_LOGIN_NOTIFY: 'event_login_notify',
    EVENT_ON_TYPING: 'event_typing',
    EVENT_ON_MESSAGE_UPDATED: 'event_message_updated',
    EVENT_MESSAGE_SELECTED: 'event_message_selected',
    
    URL_LOGIN: "/user/login",
    URL_API_USERS: "/user/list",
    URL_API_PAST_MESSAGE: "/message/list",
    URL_API_UPLOAD_FILE: "/file/upload",
    URL_API_DOWNLOAD_FILE: "/file/download",
    URL_API_SEND_FILE: "/message/sendFile",
    
    COOKIE_KEY_LOGININFO: "cookie_logininfo",
    
    MESSAGE_TYPE_TEXT : 1,
    MESSAGE_TYPE_FILE : 2,
    MESSAGE_TYPE_NEW_USER : 1000,
    MESSAGE_TYPE_USER_LEAVE : 1001,
    MESSAGE_TYPE_FILE_UPLOADIND : 10000,
    MESSAGE_TYPE_TYPING : 10001,
    
    MEASSAGE_STATUS_SENDING: 0,
    MEASSAGE_STATUS_SENT: 1,
    
    TYPING_OFF: 0,
    TYPING_ON:1,
    
    PAGING_ROW: 50

    
};

module.exports = Const;
