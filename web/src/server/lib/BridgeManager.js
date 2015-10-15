var _ = require('lodash');
var Const = require("../const");
var Settings = require('./Settings');

var BridgeManager = {

    init:function(){
        
        var Observer = require("node-observer");

        Observer.subscribe(this, Const.notificationSendMessage, function(who, obj) {
            
            if(_.isEmpty(Settings.listeners))
                return;
                
            if(_.isFunction(Settings.listeners.onNewMessage))
                Settings.listeners.onNewMessage(obj);
            
        });
                
        Observer.subscribe(this, Const.notificationNewUser, function(who, obj) {
            
            
            if(_.isEmpty(Settings.listeners))
                return;
                
            if(_.isFunction(Settings.listeners.onNewUser))
                Settings.listeners.onNewUser(obj);
            
        });
        
        Observer.subscribe(this, Const.notificationUserTyping, function(who, obj) {
            
            
            if(_.isEmpty(Settings.listeners))
                return;
                
            if(_.isFunction(Settings.listeners.OnUserTyping))
                Settings.listeners.OnUserTyping(obj);
            
        });
                
        Observer.subscribe(this, Const.notificationMessageChanges, function(who, obj) {
            
            
            if(_.isEmpty(Settings.listeners))
                return;
                
            if(_.isFunction(Settings.listeners.OnMessageChanges))
                Settings.listeners.OnMessageChanges(obj);
            
        });

    }
    
}

module["exports"] = BridgeManager;