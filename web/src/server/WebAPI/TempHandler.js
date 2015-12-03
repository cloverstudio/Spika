var express = require('express');
var router = express.Router();
var bodyParser = require("body-parser");
var path = require('path');
var _ = require('lodash');

var RequestHandlerBase = require("./RequestHandlerBase");
var UsersManager = require("../lib/UsersManager");
var DatabaseManager = require("../lib/DatabaseManager");
var Utils = require("../lib/Utils");
var Const = require("../const");
var async = require('async');
var formidable = require('formidable');
var fs = require('fs-extra');
var path = require('path');
var mime = require('mime');
var SocketAPIHandler = require('../SocketAPI/SocketAPIHandler');

var TempHandler = function(){
    
}

_.extend(TempHandler.prototype,RequestHandlerBase.prototype);

TempHandler.prototype.attach = function(router){
        
    var self = this;

    //Login data (requires body-parser)
    router.get('',function(request,response){
        
        self.successResponse(response,Const.responsecodeSucceed,{
            param1:1,
            param2:2
        });
        
    });

}

new TempHandler().attach(router);
module["exports"] = router;
