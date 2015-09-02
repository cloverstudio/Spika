var _ = require('lodash');

var UsersManager = require("../lib/UsersManager");
var DatabaseManager = require("../lib/DatabaseManager");
var Utils = require("../lib/Utils");
var Const = require("../const");
var SocketHandlerBase = require("./SocketHandlerBase");
var SocketAPIHandler = require('../SocketAPI/SocketAPIHandler');

var SendMessageActionHandler = function(){
    
}

_.extend(SendMessageActionHandler.prototype,SocketHandlerBase.prototype);

SendMessageActionHandler.prototype.attach = function(io,socket){
        
    var self = this;

    /**
     * @api {socket} "sendMessage" Send New Message
     * @apiName Send Message
     * @apiGroup Socket 
     * @apiDescription Send new message by socket
     * @apiParam {string} roomID Room ID
     * @apiParam {string} userID User ID
     * @apiParam {string} type Message Type. 1:Text 2:File 3:Location
     * @apiParam {string} message Message if type == 1
     * @apiParam {string} fileID File ID if type == 2
     * @apiParam {object} location lat and lng if type == 3
     *
     */
     
    socket.on('sendMessage', function(param){
                        
        if(Utils.isEmpty(param.roomID)){
            console.log('param error roomID');
            return;
        }
            

        if(Utils.isEmpty(param.userID)){
            console.log('param error userID');
            return;
        }

        if(Utils.isEmpty(param.type)){
            console.log('param error type');
            return;
        }
                        
        if(param.type == Const.messageTypeText && Utils.isEmpty(param.message)){
            console.log('param error message');
            return;
        }

        if(param.type == Const.messageTypeLocation && (
                                        Utils.isEmpty(param.location) ||
                                        Utils.isEmpty(param.location.lat) ||
                                        Utils.isEmpty(param.location.lng))){
                                        
            console.log('param error message');
            
            return;
        
        }
        
        var userID = param.userID;
    
        SocketAPIHandler.sendNewMessage(userID,param);
        
    });


}


module["exports"] = new SendMessageActionHandler();