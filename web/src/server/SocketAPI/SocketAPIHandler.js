var _ = require('lodash');

var UsersManager = require("../lib/UsersManager");
var DatabaseManager = require("../lib/DatabaseManager");
var Utils = require("../lib/Utils");
var Const = require("../const");
var UserModel = require("../Models/UserModel");
var MessageModel = require("../Models/MessageModel");
var Settings = require("../lib/Settings");


var SocketAPIHandler = {
    
    io:null,
    nsp : null,
    init: function(io){
        
        var self = this;
        this.io = io;
        this.nsp = io.of(Settings.options.socketNameSpace);
        
        this.nsp.on('connection', function(socket) {
                        
            require('./DisconnectActionHandler').attach(io,socket);
            require('./LoginActionHandler').attach(io,socket);
            require('./SendMessageActionHandler').attach(io,socket);
            require('./SendTypingActionHandler').attach(io,socket);
            require('./OpenMessageActionHandler').attach(io,socket);
            require('./DeleteMessageActionHandler').attach(io,socket);

  
        });

    },
    
    sendNewMessage: function(userID,param,callBack){
        
        var self = this;
        
        //save to DB
        UserModel.findUserbyId(userID,function (err,user) {

            var objMessage = {
                user:user._id,
                userID: userID,
                roomID: param.roomID,
                message: param.message,
                type: param.type,
                file: null,
                created: Utils.now()                   
            };
            
            if(!Utils.isEmpty(param.file)){
                
                objMessage.file = {
                    file : {
		                id: param.file.file.id,
    		            name: param.file.file.name,
    		            size: param.file.file.size,
    		            mimeType: param.file.file.mimeType
                    }
                };
                
                if(!Utils.isEmpty(param.file.thumb)){
                 
                    objMessage.file.thumb = {
		                id: param.file.thumb.id,
    		            name: param.file.thumb.name,
    		            size: param.file.thumb.size,
    		            mimeType: param.file.thumb.mimeType
                    };
                
                }
                
            }

            if(!Utils.isEmpty(param.location)){
                
                objMessage.location = param.location;

            }

            
            // save to database
            var newMessage = new DatabaseManager.messageModel(objMessage);

            newMessage.save(function(err,message){

                if(err) throw err;

                MessageModel.populateMessages(message,function (err,data) {
                                        
                    var messageObj = data[0];
                    messageObj.localID = '';
                    messageObj.deleted = 0;
                    
                    if(!Utils.isEmpty(param.localID))
                        messageObj.localID = param.localID;
                                        
                    self.io.of(Settings.options.socketNameSpace).in(param.roomID).emit('newMessage', data[0]);
                                    
                    if(callBack)
                        callBack();

                });

            });
            
        });

 
    }
    
};

module["exports"] = SocketAPIHandler;