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
var UserModel = require("../Models/UserModel");
var MessageModel = require("../Models/MessageModel");

var LatestMessageListHandler = function(){
    
}

_.extend(LatestMessageListHandler.prototype,RequestHandlerBase.prototype);

LatestMessageListHandler.prototype.attach = function(router){
        
    var self = this;
    
    /**
     * @api {get} /message/latest/:roomID/:lastMessageID Get all latest messages
     * @apiName Get all latest messages of the room
     * @apiGroup WebAPI
     * @apiDescription Get all latest message from the room

     * @apiParam {String} RoomID ID of room
     * @apiParam {String} lastMessageID MessageID of last message already shown. To get last 50 message put this param 0
     *
     * @apiSuccess {String} Token
     * @apiSuccess {String} User Model of loginned user
     *     
     * @apiSuccessExample Success-Response:
{
    "success": 1,
    "result": {
        "messages": [
            {
                "_id": "564d7c613e84a5407599ce8b",
                "user": {
                    "_id": "564b1f0c6d8463e192831fe4",
                    "userID": "5638c0a71b659fc060941d87",
                    "name": "KenYasue",
                    "avatarURL": "/spika/img/noavatar.png",
                    "token": "j4vSCqcIednY5y4g3wmMJuk6",
                    "created": 1447763724451,
                    "__v": 0
                },
                "userID": "5638c0a71b659fc060941d87",
                "roomID": "564d7c593e84a5407599ce80",
                "message": "10",
                "localID": "_NNq1fIRx938rNcISLd8MGYW063RcA94X",
                "type": 1,
                "created": 1447918689029,
                "__v": 0,
                "seenBy": []
            },
            {
                "_id": "564d7c5e3e84a5407599ce8a",
                "user": {
                    "_id": "564b1f0c6d8463e192831fe4",
                    "userID": "5638c0a71b659fc060941d87",
                    "name": "KenYasue",
                    "avatarURL": "/spika/img/noavatar.png",
                    "token": "j4vSCqcIednY5y4g3wmMJuk6",
                    "created": 1447763724451,
                    "__v": 0
                },
                "userID": "5638c0a71b659fc060941d87",
                "roomID": "564d7c593e84a5407599ce80",
                "message": "98",
                "localID": "_d9mEiYGCrGLRubjIfaHAAWtPUtO1eMBl",
                "type": 1,
                "created": 1447918686869,
                "__v": 0,
                "seenBy": []
            }
        ]
    }
}
    */
    
    router.get('/:roomID/:lastMessageID',function(request,response){
        
        self.logic(request,response,function(err,result){
            
            if(err){
                self.errorResponse(
                    response,
                    Const.httpCodeSucceed,
                    Const.responsecodeParamError,
                    err,
                    false
                );
            }else{
                self.successResponse(response,result);
            }
            
        });
        
    });
    

}

LatestMessageListHandler.prototype.logic = function(request,response,callBack){
    
    var self = this;
    
    
    var roomID = request.params.roomID;
    var lastMessageID = request.params.lastMessageID;
            
    if(Utils.isEmpty(lastMessageID)){
        
        callBack(Utils.localizeString("Please specify lastMessageID."),null);
    
        return;
        
    }

    if(Utils.isEmpty(roomID)){
        
        callBack(Utils.localizeString("Please specify roomID."),null);

        return;
        
    }
    
    async.waterfall([
    
        function (done) {

            MessageModel.findAllMessages(roomID,lastMessageID,function (err,data) {
                
                done(err,data);

            });
            
        },
        function (messages,done) {

            MessageModel.populateMessages(messages,function (err,data) {
                
                done(err,data);

            });
            
        }
    ],
        function (err, result) {
            
            if(err){

                
                callBack(err,null);
                
            }else{
                
                var responseJson = {
                    messages : result
                }
                
                callBack(null,responseJson);

            }
                 
        }
    );

}

var handler = new LatestMessageListHandler();
handler.attach(router);

module["exports"] = {
    handler: handler,
    router: router
};
