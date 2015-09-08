var _ = require('lodash');
var Observer = require("node-observer");

var UsersManager = require("../lib/UsersManager");
var DatabaseManager = require("../lib/DatabaseManager");
var Utils = require("../lib/Utils");
var Const = require("../const");
var SocketHandlerBase = require("./SocketHandlerBase");
var UserModel = require("../Models/UserModel");
var Settings = require("../lib/Settings");

var SendTypingActionHandler = function(){
    
}

_.extend(SendTypingActionHandler.prototype,SocketHandlerBase.prototype);

SendTypingActionHandler.prototype.attach = function(io,socket){
        
    var self = this;

    /**
     * @api {socket} "sendTyping" Send typing notification
     * @apiName Typing Notification
     * @apiGroup Socket 
     * @apiDescription Send typing notification

     * @apiParam {string} userID User ID
     * @apiParam {string} roomID Room ID
     * @apiParam {string} type 0: Remove typing notificaiton 1: Show typing notification
     *
     */
     
    socket.on('sendTyping', function(param){

        if(Utils.isEmpty(param.userID)){
            console.log('param error userID');
            return;
        }
        
        if(Utils.isEmpty(param.roomID)){
            console.log('param error roomID');
            return;
        }
        
        if(Utils.isEmpty(param.type)){
            console.log('param error type');
            return;
        }
        
        UserModel.findUserbyId(param.userID,function (err,user) {
            
            param.user = user;
            io.of(Settings.options.socketNameSpace).in(param.roomID).emit('sendTyping', param);
            Observer.send(this, Const.notificationUserTyping, param);

            
        });
        
        
        
    });




}


module["exports"] = new SendTypingActionHandler();