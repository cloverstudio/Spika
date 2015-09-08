(function(global) {
    "use strict;"

    // Class ------------------------------------------------
    var Const = {};

    Const.responsecodeSucceed = 1;
    Const.responsecodeError = 0;
    
    Const.responsecodeParamError = 2001;
    Const.responsecodeTokenError = 2100;

    Const.httpCodeSucceed = 200;
    Const.httpCodeFileNotFound = 404;
    
    Const.messageTypeText = 1;
    Const.messageTypeFile = 2;
    Const.messageTypeLocation = 3;
    Const.messageNewUser = 1000;
    Const.messageUserLeave = 1001;

    Const.typingOn = 1;
    Const.typingOff = 0;
    
    Const.pagingLimit = 50;

    Const.notificationSendMessage = "SendMessage";
    Const.notificationNewUser = "NewUser";
    Const.notificationUserTyping = "UserTyping";
    Const.notificationMessageChanges = "MessageChanges";


    // Exports ----------------------------------------------
    module["exports"] = Const;

})((this || 0).self || global);
