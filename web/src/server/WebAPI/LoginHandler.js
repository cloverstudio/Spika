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

var RequestHandlerBase = require("./RequestHandlerBase");
var UsersManager = require("../lib/UsersManager");
var DatabaseManager = require("../lib/DatabaseManager");
var Utils = require("../lib/Utils");
var SocketAPIHandler = require('../SocketAPI/SocketAPIHandler');
var UserModel = require("../Models/UserModel");
var Settings = require("../lib/Settings");
var Const = require("../const");

var LoginLogic = require("../Logics/Login");

var LoginHandler = function(){
    
}

_.extend(LoginHandler.prototype,RequestHandlerBase.prototype);

LoginHandler.prototype.attach = function(router){
        
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
    router.post('/',function(request,response){
                
        LoginLogic.execute(request.body,function(err,result){
            
            if(err){
                self.errorResponse(
                    response,
                    Const.httpCodeSucceed,
                    Const.responsecodeParamError,
                    err,
                    false
                );
            }else{
                
                self.successResponse(response,{
                    token: result.token,
                    user: result.user
                });
                
            }
            
        });
        
    });

}

new LoginHandler().attach(router);
module["exports"] = router;
