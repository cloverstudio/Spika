var bodyParser = require("body-parser");
var path = require('path');
var _ = require('lodash');

var Const = require("../const");
var Settings = require("../lib/Settings");

var RequestHandlerBase = function(){
    
}

RequestHandlerBase.prototype.version = 'v1';

RequestHandlerBase.prototype.path = function(path){
    
    return Settings.options.urlPrefix + "/" + this.version + path;
    
}

RequestHandlerBase.prototype.errorResponse = function(
        response,
        httpCode){

    response.status(httpCode);
    response.send("");
    
}

RequestHandlerBase.prototype.successResponse = function(response,code,data){
    
    response.status(Const.httpCodeSucceed);
    
    if(code != Const.responsecodeSucceed){
        
        response.json({
            code : code
        });
        
    } else {

        response.json({
            code : Const.responsecodeSucceed,
            data : data
        });
    
    }

    
}

module["exports"] = RequestHandlerBase;