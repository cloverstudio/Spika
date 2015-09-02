var socket = require('socket.io-client');
var Backbone = require('backbone');
var _ = require('lodash');
var CONST = require('../consts');
var Config = require('../init');

(function(global) {
    "use strict;"

    var socketIOManager = {
        
        io : null,
        
        init:function(){
            
            this.io = socket.connect(Config.socketUrl);
            

            this.io.on('newUser', function(param){
                Backbone.trigger(CONST.EVENT_ON_LOGIN_NOTIFY, param);
            });

            this.io.on('userLeft', function(param){
                Backbone.trigger(CONST.EVENT_ON_LOGOUT_NOTIFY, param);
            });

            this.io.on('newMessage', function(param){ 
                Backbone.trigger(CONST.EVENT_ON_MESSAGE,param);	    
            }); 

            this.io.on('sendTyping', function(param){ 
                Backbone.trigger(CONST.EVENT_ON_TYPING,param);	    
            }); 
            
            this.io.on('login', function(param){
                Backbone.trigger(CONST.EVENT_ON_LOGIN, param);
            });

            this.io.on('logout', function(param){
                Backbone.trigger(CONST.EVENT_ON_LOGOUT, param);
            });

            this.io.on('messageUpdated', function(param){
                Backbone.trigger(CONST.EVENT_ON_MESSAGE_UPDATED, param);
            });

            
        },
        
        emit:function(command,params){
            
            var command = arguments[0];
            this.io.emit(command, params);
            
        }
            
    };
 
    // Exports ----------------------------------------------
    module["exports"] = socketIOManager;

})((this || 0).self || global);