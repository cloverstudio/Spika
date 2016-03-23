var Settings = require('./Settings');
var CONST = require('../consts');

(function(global) {
    "use strict;"

    var UrlGenerator = {
        
        userLogin: function(){

            return Settings.options.apiBaseUrl + CONST.URL_LOGIN;
            
        },
        userList: function(roomID){
            
            return Settings.options.apiBaseUrl + CONST.URL_API_USERS + '/' + roomID;
            
        },
        messageList: function(roomID,lastMessageId){
            
            return Settings.options.apiBaseUrl + CONST.URL_API_PAST_MESSAGE + '/' + roomID + '/' + lastMessageId;            
            
        },
        uploadFile: function(){
            
            return Settings.options.apiBaseUrl + CONST.URL_API_UPLOAD_FILE;            
            
        },
        downloadFile: function(fileID){
            
            return Settings.options.apiBaseUrl + CONST.URL_API_DOWNLOAD_FILE + '/' + fileID;            
            
        },
        sendFile: function(){
            
            return Settings.options.apiBaseUrl + CONST.URL_API_SEND_FILE;            
            
        },
        stickerList: function(){
            
            return Settings.options.apiBaseUrl + CONST.URL_API_STICKERLIST;            
            
        }
            
    };

    // Exports ----------------------------------------------
    module["exports"] = UrlGenerator;

})((this || 0).self || global);