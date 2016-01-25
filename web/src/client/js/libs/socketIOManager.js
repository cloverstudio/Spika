var socket = require('socket.io-client');
var Backbone = require('backbone');
var _ = require('lodash');
var CONST = require('../consts');
var Config = require('../init');
var ErrorDialog = require('../Views/Modals/ErrorDialog/ErrorDialog');

(function(global) {
    "use strict;"

    var socketIOManager = {
        
        io : null,
        
        init:function(){
            
            this.io = socket.connect(Config.socketUrl);
            

            this.io.on('socketerror', function(param){
                
                if(param.code){
                    
                    ErrorDialog.show('Error',CONST.ERROR_CODES[param.code]);
                    
                }else{
                    ErrorDialog.show('Error','Unknown Error');
                }
                
                
            });
            
            this.io.on('newUser', function(param){
                Backbone.trigger(CONST.EVENT_ON_LOGIN_NOTIFY, param);
                                
                // call listener
                if(!_.isEmpty(window.parent.SpikaAdapter) &&
                    !_.isEmpty(window.parent.SpikaAdapter.listener)){
                    
                    var listener = window.parent.SpikaAdapter.listener;
                    
                    if(_.isFunction(listener.onNewUser))
                        listener.onNewMessage(param);
                    
                }
                
            });

            this.io.on('userLeft', function(param){
            
                Backbone.trigger(CONST.EVENT_ON_LOGOUT_NOTIFY, param);

                // call listener
                if(!_.isEmpty(window.parent.SpikaAdapter) &&
                    !_.isEmpty(window.parent.SpikaAdapter.listener)){
                    
                    var listener = window.parent.SpikaAdapter.listener;
                    
                    if(_.isFunction(listener.onUserLeft))
                        listener.onUserLeft(param);
                    
                }

            });

            this.io.on('newMessage', function(param){ 
                
                console.log('newMessage',param);
                
                Backbone.trigger(CONST.EVENT_ON_MESSAGE,param);	
                    
                // call listener
                if(!_.isEmpty(window.parent.SpikaAdapter) &&
                    !_.isEmpty(window.parent.SpikaAdapter.listener)){
                    
                    var listener = window.parent.SpikaAdapter.listener;
                    
                    if(_.isFunction(listener.onNewMessage))
                        listener.onNewMessage(param);
                    
                }

            }); 

            this.io.on('sendTyping', function(param){ 
                
                
                Backbone.trigger(CONST.EVENT_ON_TYPING,param);	    
 
                 // call listener
                if(!_.isEmpty(window.parent.SpikaAdapter) &&
                    !_.isEmpty(window.parent.SpikaAdapter.listener)){
                    
                    var listener = window.parent.SpikaAdapter.listener;
                    
                    if(_.isFunction(listener.OnUserTyping))
                        listener.OnUserTyping(param);
                    
                }

           }); 
            
            this.io.on('login', function(param){
                Backbone.trigger(CONST.EVENT_ON_LOGIN, param);
            });

            this.io.on('logout', function(param){
                Backbone.trigger(CONST.EVENT_ON_LOGOUT, param);
            });

            this.io.on('messageUpdated', function(param){
                Backbone.trigger(CONST.EVENT_ON_MESSAGE_UPDATED, param);
 
                 // call listener
                if(!_.isEmpty(window.parent.SpikaAdapter) &&
                    !_.isEmpty(window.parent.SpikaAdapter.listener)){
                    
                    var listener = window.parent.SpikaAdapter.listener;
                    
                    if(_.isFunction(listener.OnMessageChanges))
                        listener.OnMessageChanges(param);
                    
                }

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