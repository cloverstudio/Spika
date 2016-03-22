var express = require('express');
var router = express.Router();
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
var tokenChecker = require('../lib/Auth');

var UserListHandler = function(){
    
}

_.extend(UserListHandler.prototype,RequestHandlerBase.prototype);

UserListHandler.prototype.attach = function(router){
        
    var self = this;

    /**
     * @api {get} /user/list/:roomID  Get List of Users in room
     * @apiName Get User List
     * @apiGroup WebAPI
     * @apiDescription Get list of users who are currently in the room

     * @apiParam {String} roomID ID of the room
     *
     *     
     * @apiSuccessExample Success-Response:
{
  "code": 1,
  "data": [
    {
      "userID": "test",
      "name": "test",
      "avatarURL": "http://localhost:8080/img/noavatar.png",
      "roomID": "test",
      "socketID": "Znw8kW-ulKXBMoVAAAAB"
    },
    {
      "userID": "test2",
      "name": "test2",
      "avatarURL": "http://localhost:8080/img/noavatar.png",
      "roomID": "test",
      "socketID": "xIBEwT0swJwjcI2hAAAC"
    }
  ]
}
    */
    router.get('/:roomID',tokenChecker,function(request,response){
      
        var roomID = request.params.roomID;
        var users = UsersManager.getUsers(roomID);
        
        if(_.isEmpty(roomID)){
          self.successResponse(response,Const.resCodeUserListNoRoomID);
          return;
        }
        
        self.successResponse(response,
          Const.responsecodeSucceed,
          Utils.stripPrivacyParamsFromArray(users));
        
    });


}

new UserListHandler().attach(router);
module["exports"] = router;
