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

var TestHandler = function(){
    
}

_.extend(TestHandler.prototype,RequestHandlerBase.prototype);

TestHandler.prototype.attach = function(route){
        
    var self = this;

    route.get('/',function(request,response){
        
        response.send('Hello');
        
    });

}

new TestHandler().attach(router);
module["exports"] = router;
