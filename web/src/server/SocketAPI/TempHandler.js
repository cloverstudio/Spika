var _ = require('lodash');

var UsersManager = require("../lib/UsersManager");
var DatabaseManager = require("../lib/DatabaseManager");
var Utils = require("../lib/Utils");
var Const = require("../const");
var SocketHandlerBase = require("./SocketHandlerBase");

var TempHandler = function(){
    
}

_.extend(TempHandler.prototype,SocketHandlerBase.prototype);

TempHandler.prototype.attach = function(io,socket){
        
    var self = this;
    
    socket.on('temp', function () {
    
        
    });

}


module["exports"] = new TempHandler();