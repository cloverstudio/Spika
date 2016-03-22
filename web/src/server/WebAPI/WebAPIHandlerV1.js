var express = require('express');
var router = express.Router();

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
        
        router.use("/user/login", require('./LoginHandler'));
        router.use("/temp", require('./TempHandler'));
        router.use("/message/list", require('./MessageListHandler'));
        router.use("/message/latest", require('./LatestMessageListHandler').router);
        router.use("/user/list", require('./UserListHandler'));
        router.use("/message/sendFile", require('./SendFileAsMessageHandler'));
        router.use("/file/upload", require('./FileUploadHandler'));
        router.use("/file/download", require('./FileDownloadHandler'));
        router.use("/test", require('./TestHandler'));
        router.use("/stickers", require('./StickerListHandler'));
        
        WebAPIHandler.router = router;
        app.use(Settings.options.urlPrefix + "/v1", router);
        
    }
}

module["exports"] = WebAPIHandler;