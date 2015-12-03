var _ = require('lodash');
var Observer = require("node-observer");

var UsersManager = require("../lib/UsersManager");
var DatabaseManager = require("../lib/DatabaseManager");
var Utils = require("../lib/Utils");
var Const = require("../const");
var SocketHandlerBase = require("./SocketHandlerBase");
var SocketAPIHandler = require('../SocketAPI/SocketAPIHandler');
var MessageModel = require("../Models/MessageModel");
var Settings = require("../lib/Settings");

var DeleteMessageActionHandler = function(){
    
}

_.extend(DeleteMessageActionHandler.prototype,SocketHandlerBase.prototype);

DeleteMessageActionHandler.prototype.attach = function(io,socket){
        
    var self = this;

    /**
     * @api {socket} "deleteMessage" Delete Message
     * @apiName Delete Message
     * @apiGroup Socket 
     * @apiDescription Delete Message
     * @apiParam {string} userID User ID
     * @apiParam {string} messageID Message ID
     *
     */
     
    socket.on('deleteMessage', function(param){
        
        if(Utils.isEmpty(param.userID)){
            socket.emit('socketerror', {code:Const.resCodeSocketDeleteMessageNoUserID});               
            return;
        }

        if(Utils.isEmpty(param.messageID)){
            socket.emit('socketerror', {code:Const.resCodeSocketDeleteMessageNoMessageID});               
            return;
        }
        
        MessageModel.findMessagebyId(param.messageID,function(err,message){
            
            if(err) {
                socket.emit('socketerror', {code:Const.resCodeSocketUnknownError});               
                return;
            }
            
            message.update({
                message: '',
                file: null,
                location:null,
                deleted: Utils.now()
            },{},function(err,userResult){

                MessageModel.populateMessages(message,function (err,messages) {

                    if(err) {
                        socket.emit('socketerror', {code:Const.resCodeSocketUnknownError});               
                        return;
                    }
                    
                    if(messages.length > 0){
                                                
                        var obj = messages[0];
                        obj.deleted = Utils.now();
                        obj.message = '';
                        obj.file = null;
                        obj.location = null;

                        // send updated messages
                        io.of(Settings.options.socketNameSpace).in(messages[0].roomID).emit('messageUpdated', [obj]);
                        Observer.send(this, Const.notificationMessageChanges, [obj]);

                        
                    }

                });

            });
            
        });
        
    });


}


module["exports"] = new DeleteMessageActionHandler();