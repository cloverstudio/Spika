var init = require('./init.js');
var express = require('express');
var _ = require('lodash');

var Settings = require('./lib/Settings');

var spika = function(app,io,options){

    Settings.options = _.merge(init,options.config);
    Settings.listeners = options.listeners;
        
    var WebAPIHandlerV1 = require('./WebAPI/WebAPIHandlerV1');
    WebAPIHandlerV1.init(app,express);
    
    var SocketAPIHandler = require('./SocketAPI/SocketAPIHandler');
    SocketAPIHandler.init(io);
    
    var DatabaseManager = require('./lib/DatabaseManager');
    DatabaseManager.init(Settings.options);
    
    var BridgeManager = require('./lib/BridgeManager');
    BridgeManager.init();

    // define custome funcitons
    this.getOnlineUsersByRoomId = function(roomId){
        var UsersManager = require('./lib/UsersManager');
        return UsersManager.getUsers(roomId);    
    };

    this.loginToSpika = function(user,roomId,callBack){
        
        var LoginLogic = require("./Logics/Login");

        LoginLogic.execute(user,function(err,loginResult){
            
            if(err){
                if(callBack) callBack(false);
            }else{
                                       
                if(callBack)
                    callBack({
                        ok: true,
                        message : "test"
                    });
                   
            }
            
        });        
        
    };


    this.sendMessage = function(userID,param,callBack){
                
        var SendMessageLogic = require("./Logics/SendMessage");

        SendMessageLogic.execute(userID,param,function(sendMessageResult){
            
            if(callBack) callBack(sendMessageResult);
            
        });        
        
    };


}

spika.prototype.options = {};

module.exports = spika;