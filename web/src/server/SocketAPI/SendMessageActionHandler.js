var _ = require('lodash');

var UsersManager = require("../lib/UsersManager");
var DatabaseManager = require("../lib/DatabaseManager");
var Utils = require("../lib/Utils");
var Const = require("../const");
var SocketHandlerBase = require("./SocketHandlerBase");
var SocketAPIHandler = require('../SocketAPI/SocketAPIHandler');

var SendMessageActionHandler = function(){
    
}

var SendMessageLogic = require('../Logics/SendMessage');

var BridgeManager = require('../lib/BridgeManager');

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
            socket.emit('socketerror', {code:Const.resCodeSocketSendMessageNoRoomID}); 
            return;
        }


        if(Utils.isEmpty(param.userID)){
            socket.emit('socketerror', {code:Const.resCodeSocketSendMessageNoUserId});
            return;
        }

        if(Utils.isEmpty(param.type)){
            socket.emit('socketerror', {code:Const.resCodeSocketSendMessageNoType}); 
            return;
        }
                        
        if(param.type == Const.messageTypeText && Utils.isEmpty(param.message)){
            socket.emit('socketerror', {code:Const.resCodeSocketSendMessageNoMessage});               
            return;
        }

        if(param.type == Const.messageTypeLocation && (
                                        Utils.isEmpty(param.location) ||
                                        Utils.isEmpty(param.location.lat) ||
                                        Utils.isEmpty(param.location.lng))){
                                        
            socket.emit('socketerror', {code:Const.resCodeSocketSendMessageNoLocation});               
            
            return;
        
        }
        
        var userID = param.userID;

        BridgeManager.hook('sendMessage',param,function(result){
            
            if(result == null || result.canSend){
                
                var userID = param.userID;
            
                SendMessageLogic.execute(userID,param,function(result){
                    
                    
                    
                },function(err,code){
                    
                    if(err){
                        socket.emit('socketerror', {code:Const.resCodeSocketSendMessageFail}); 
                    }else{
                        socket.emit('socketerror', {code:code}); 
                    }
                    
                    
                });
            
            }
            
        });
        
    });


}


module["exports"] = new SendMessageActionHandler();