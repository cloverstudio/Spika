var _ = require('lodash');

var UsersManager = require("../lib/UsersManager");
var DatabaseManager = require("../lib/DatabaseManager");
var Utils = require("../lib/Utils");
var Const = require("../const");
var UserModel = require("../Models/UserModel");
var MessageModel = require("../Models/MessageModel");
var Settings = require("../lib/Settings");
var Observer = require("node-observer");


var SocketAPIHandler = {
    
    io:null,
    nsp : null,
    init: function(io){
        
        var self = this;
        this.io = io;
        this.nsp = io.of(Settings.options.socketNameSpace);
        
        this.nsp.on('connection', function(socket) {
                        
            require('./DisconnectActionHandler').attach(io,socket);
            require('./LoginActionHandler').attach(io,socket);
            require('./SendMessageActionHandler').attach(io,socket);
            require('./SendTypingActionHandler').attach(io,socket);
            require('./OpenMessageActionHandler').attach(io,socket);
            require('./DeleteMessageActionHandler').attach(io,socket);
            
        });

    }
    
};

module["exports"] = SocketAPIHandler;