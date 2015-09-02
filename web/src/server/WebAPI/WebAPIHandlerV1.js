var bodyParser = require("body-parser");
var path = require('path');
var _ = require('lodash');

var UsersManager = require("../lib/UsersManager");
var DatabaseManager = require("../lib/DatabaseManager");
var Utils = require("../lib/Utils");
var Const = require("../const");
var Settings = require("../lib/Settings");
var async = require('async');
var formidable = require('formidable');
var fs = require('fs-extra');
var path = require('path');
var mime = require('mime');
var SocketAPIHandler = require('../SocketAPI/SocketAPIHandler');
var Settings = require("../lib/Settings");

var WebAPIHandler ={
    
    init: function(app,express){
                
        var self = this;
        
        app.use(Settings.options.urlPrefix,express.static(__dirname + '/../../../public'));
        app.use(bodyParser.json());
        
        /*
        app.use(function(err, req, res, next) {
          console.error(err.stack);
          res.status(500).send('Something broke!');
        });
        */
        
        // HTTP Routes
        require('./LoginHandler').attach(app);
        require('./TempHandler').attach(app);
        require('./MessageListHandler').attach(app);
        require('./UserListHandler').attach(app);
        require('./SendFileAsMessageHandler').attach(app);
        require('./FileUploadHandler').attach(app);
        require('./FileDownloadHandler').attach(app);

        app.get('/fail/sync', function(req, res) {
           throw new Error('whoops');
        });
        app.get('/fail/async', function(req, res) {
           process.nextTick(function() {
              throw new Error('whoops');
           });
        });
        
    }
}

module["exports"] = WebAPIHandler;