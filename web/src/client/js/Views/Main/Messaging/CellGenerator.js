var _ = require('lodash');
var CONST = require('../../../consts');
var U = require('../../../libs/utils.js');
var LoginUserManager = require('../../../libs/loginUserManager.js');
var Message = require('../../../Models/message.js');
var WebAPIManager = require('../../../libs/webAPIManager');
var UrlGenerator = require('../../../libs/urlGenerator');
var socketIOManager = require('../../../libs/socketIOManager');
var Settings = require('../../../libs/Settings');

function CellGenerator(options){
    this.parentView = options.view;
    
    // loading templates
    this.messageTemplate = require('./MessageCells/Message.hbs');
    this.fileUploadingTemplate = require('./MessageCells/FileUploading.hbs');
    this.userStateChangeTemplate = require('./MessageCells/UserStateChange.hbs');
    this.fileTemplate = require('./MessageCells/File.hbs');
    this.thumbTemplate = require('./MessageCells/Thumbnail.hbs');
    this.typingTemplate = require('./MessageCells/Typing.hbs');
    this.deletedTemplate = require('./MessageCells/DeletedMessage.hbs');

};

CellGenerator.prototype.parentView = null;
CellGenerator.prototype.generate = function(messageModel){

    var flatData = {};

    _.forEach(messageModel.attributes, function(val, key) {
        flatData[key] = val;
    });

    _.forEach(messageModel.get('user').attributes, function(val, key) {
    
        if(key == 'created')
            return;
            
        if(key == 'id')
            return;
            
        flatData[key] = val;
    });
    
    flatData.message = U.escapeHtml(flatData.message);
    flatData.message = flatData.message.replace(new RegExp('  ','g'), '&nbsp;&nbsp;');
    flatData.message = flatData.message.replace(new RegExp('\t','g'), '&nbsp;&nbsp;&nbsp;&nbsp;');
    flatData.message = flatData.message.replace(new RegExp('\r?\n','g'), '<br/>');
    flatData.message = U.contentExtract(flatData.message);
        
    if(flatData.userID == LoginUserManager.user.get('id') && Settings.options.useBothSide){
        flatData.isMine = 'mine';
    }else{
        flatData.isMine = 'other';
    }
    
    var html = '';
        
    if(messageModel.get('deleted') != 0){

        html = this.deletedTemplate(flatData);
        
    } else {
    
        if(messageModel.get('type') == CONST.MESSAGE_TYPE_TEXT)
            html = this.messageTemplate(flatData);
        
        if(messageModel.get('type') == CONST.MESSAGE_TYPE_FILE){
                    
            if(!_.isUndefined(flatData.file.thumb)){
                
                // thumbnail exists
                //flatData.downloadURL = 'http://192.168.1.5:8080/img/ipad-art-wide-AVATAR2-420x0.jpg';
                flatData.downloadURL = UrlGenerator.downloadFile(flatData.file.file.id);
                flatData.thumbURL = UrlGenerator.downloadFile(flatData.file.thumb.id);
                html = this.thumbTemplate(flatData);
                                     
            }else{
    
                flatData.file.file.size = Math.floor(flatData.file.file.size / 1024 / 1024 * 100) / 100; // MB
                flatData.downloadURL = UrlGenerator.downloadFile(flatData.file.file.id);
                
                html = this.fileTemplate(flatData);
                
            }
            
        }
        
        if(messageModel.get('type') == CONST.MESSAGE_TYPE_FILE_UPLOADIND){
            
            if(messageModel.get('isUploading') == 1){
                html = this.fileUploadingTemplate(flatData);                
            }
            
        }
    
        if(messageModel.get('type') == CONST.MESSAGE_TYPE_TYPING){
            
            flatData.message = flatData.name + " is typing...";
            html = this.typingTemplate(flatData);
    
        }
            
        if(messageModel.get('type') == CONST.MESSAGE_TYPE_NEW_USER){
            
            flatData.message = flatData.name + " joined to conversation. ";
            html = this.userStateChangeTemplate(flatData);
    
        }
    
        if(messageModel.get('type') == CONST.MESSAGE_TYPE_USER_LEAVE){
            
            flatData.message = flatData.name + " left from conversation. ";
            html = this.userStateChangeTemplate(flatData);
    
        }
        

        
    }

    return html;

};


module.exports = CellGenerator;


    