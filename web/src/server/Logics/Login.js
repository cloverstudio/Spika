var express = require('express');
var router = express.Router();
var async = require('async');
var formidable = require('formidable');
var fs = require('fs-extra');
var path = require('path');
var mime = require('mime');
var bodyParser = require("body-parser");
var path = require('path');
var _ = require('lodash');

var UsersManager = require("../lib/UsersManager");
var DatabaseManager = require("../lib/DatabaseManager");
var Utils = require("../lib/Utils");
var SocketAPIHandler = require('../SocketAPI/SocketAPIHandler');
var UserModel = require("../Models/UserModel");
var Settings = require("../lib/Settings");
var Const = require("../const");

var LoginLogic = {
    execute : function(param,callBack){
        
        var name = param.name;
        var avatarURL = param.avatarURL;
        var roomID = param.roomID;
        var userID = param.userID;
                   
        if(Utils.isEmpty(name)){
            
            if(callBack)
                callBack(Utils.localizeString("Please specify name."),null);
                
            return;
            
        }
        
        if(Utils.isEmpty(avatarURL)){
            avatarURL = Settings.options.noavatarImg;
        }
        
        if(Utils.isEmpty(roomID)){

            if(callBack)
                callBack(Utils.localizeString("Please specify room id."),null);

            return;
            
        }
        
        if(Utils.isEmpty(userID)){

            if(callBack)
                callBack(Utils.localizeString("Please specify user id."),null);
            
            return;
            
        }
        
        // create token
        var token = Utils.randomString(24);
        
        // check existance
                    
        UserModel.findUserbyId(userID,function (err,user) {
                      
            if(user == null){
            
                // save to database
                var newUser = new DatabaseManager.userModel({
                    userID: userID,
                    name: name,
                    avatarURL: avatarURL,
                    token: token,
                    created: Utils.now()
                });

                newUser.save(function(err,user){
                
                    if(err) throw err;
            
                    if(callBack)
                        callBack(null,{
                            token: token,
                            user: user
                        });
            
                });

            } else {
                
            
                user.update({
                    name: name,
                    avatarURL: avatarURL,
                    token: token
                },{},function(err,userResult){
                
                    if(err){

                        if(callBack)
                            callBack(Utils.localizeString(err),null);
                        
                    }else{

                        if(callBack)
                            callBack(null,{
                                token: token,
                                user: user
                            });
                            
                    }                

            
                });
                                
            }
              
        });
        
    }
}

module["exports"] = LoginLogic;