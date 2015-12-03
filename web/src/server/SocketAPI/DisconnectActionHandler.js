var _ = require('lodash');

var UsersManager = require("../lib/UsersManager");
var DatabaseManager = require("../lib/DatabaseManager");
var Utils = require("../lib/Utils");
var Const = require("../const");
var SocketHandlerBase = require("./SocketHandlerBase");
var UserModel = require("../Models/UserModel");
var Settings = require("../lib/Settings");

var DisconnectActionHandler = function(){
    
}

_.extend(DisconnectActionHandler.prototype,SocketHandlerBase.prototype);

DisconnectActionHandler.prototype.attach = function(io,socket){
        
    var self = this;
    
    socket.on('disconnect', function () {
        
        var roomID = UsersManager.getRoomBySocketID(socket.id);
        var user = UsersManager.getUserBySocketID(socket.id);
                        
        if(!_.isNull(user)){
        
            UsersManager.removeUser(roomID,user.userID);
            socket.leave(roomID);
            
            io.of(Settings.options.socketNameSpace).in(roomID).emit('userLeft', user);
            
            if(Settings.options.sendAttendanceMessage){

                //save as message
                UserModel.findUserbyId(user.userID,function (err,user) {
                    
                    // save to database
                    var newMessage = new DatabaseManager.messageModel({
                        user:user._id,
                        userID: user.userID,
                        roomID: roomID,
                        message: '',
                        type: Const.messageUserLeave,
                        created: Utils.now()                   
                    });
                                
                    newMessage.save(function(err,message){
                    
                        if(err) throw err;
                
                        var messageObj = message.toObject();
                        messageObj.user = user.toObject();
                        
                        io.of(Settings.options.socketNameSpace).in(roomID).emit('newMessage', messageObj);
                        
                    });
                    
                });
            
            }
            
        } else {
            
        }
        
    });

}


module["exports"] = new DisconnectActionHandler();