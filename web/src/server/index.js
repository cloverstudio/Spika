var init = require('./init.js');
var express = require('express');
var _ = require('lodash');

var Settings = require('./lib/Settings');

var spika = function(app,io,options){

    Settings.options = _.merge(init,options.config);
    Settings.listeners = options.listeners;
        
    var WebAPIHandlerV1 = require('./WebAPI/WebAPIHandlerV1');
    WebAPIHandlerV1.init(app,express);
    
    var SocketAPIHandler = require('./SocketAPI/SocketAPIHandler');
    SocketAPIHandler.init(io);
    
    var DatabaseManager = require('./lib/DatabaseManager');
    DatabaseManager.init(Settings.options);
    
    var BridgeManager = require('./lib/BridgeManager');
    BridgeManager.init();
    
}

spika.prototype.options = {};

module.exports = spika;