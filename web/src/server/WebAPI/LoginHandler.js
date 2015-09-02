var bodyParser = require("body-parser");
var path = require('path');
var _ = require('lodash');

var RequestHandlerBase = require("./RequestHandlerBase");
var UsersManager = require("../lib/UsersManager");
var DatabaseManager = require("../lib/DatabaseManager");
var Utils = require("../lib/Utils");
var Const = require("../const");
var async = require('async');
var formidable = require('formidable');
var fs = require('fs-extra');
var path = require('path');
var mime = require('mime');
var SocketAPIHandler = require('../SocketAPI/SocketAPIHandler');
var UserModel = require("../Models/UserModel");
var Settings = require("../lib/Settings");

var LoginHandler = function(){
    
}

_.extend(LoginHandler.prototype,RequestHandlerBase.prototype);

LoginHandler.prototype.attach = function(app){
        
    var self = this;

    /**
     * @api {post} /user/login Get api token
     * @apiName Login
     * @apiGroup WebAPI
     * @apiDescription Login to the room specified in request, and get token for the room.

     * @apiParam {name} Users Name
     * @apiParam {avatarURL} URL of avatar image
     * @apiParam {roomID} Room Name to login
     * @apiParam {userID} User's Unique ID
     *
     * @apiSuccess {String} Token
     * @apiSuccess {String} User Model of loginned user
     *     
     * @apiSuccessExample Success-Response:
            {
              "success": 1,
              "result": {
                "token": "v3S8mMex95HFJmWm5io5RhgW",
                "user": {
                  "_id": "55cde8271d8ccb18230fe20f",
                  "userID": "testuser",
                  "name": "testuser",
                  "avatarURL": "http://localhost:8080/img/noavatar.png",
                  "token": "5pMroCXBQzpxxUElGusLOzXp",
                  "created": 1439557671981,
                  "__v": 0
                }
              }
            }
    */
     app.post(this.path('/user/login'),function(request,response){
        
        var name = request.body.name;
        var avatarURL = request.body.avatarURL;
        var roomID = request.body.roomID;
        var userID = request.body.userID;
                   
        if(Utils.isEmpty(name)){
            
            self.errorResponse(
                response,
                Const.httpCodeSucceed,
                Const.responsecodeParamError,
                Utils.localizeString("Please specify name."),
                false
            );
        
            return;
            
        }
        
        if(Utils.isEmpty(avatarURL)){
            avatarURL = Settings.options.noavatarImg;
        }
        
        if(Utils.isEmpty(roomID)){
        
            self.errorResponse(
                response,
                Const.httpCodeSucceed,
                Const.responsecodeParamError,
                Utils.localizeString("Please specify room id."),
                false
            );
            
            return;
            
        }
        
        if(Utils.isEmpty(userID)){
        
            self.errorResponse(
                response,
                Const.httpCodeSucceed,
                Const.responsecodeParamError,
                Utils.localizeString("Please specify user id."),
                false
            );
            
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
            
                    self.successResponse(response,{
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
                    
                        self.errorResponse(
                            response,
                            Const.httpCodeSucceed,
                            Const.responsecodeParamError,
                            Utils.localizeString(err),
                            true
                        );
                        
                    }else{
                    
                        self.successResponse(response,{
                            token: token,
                            user: user
                        });
                        
                    }                

            
                });
                                
            }
              
        });
        
    });

}


module["exports"] = new LoginHandler();