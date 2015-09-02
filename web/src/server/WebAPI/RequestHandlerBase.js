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
        httpCode,
        errorCode,
        reason,
        retry){

    response.status(httpCode);
    response.json({
        success : Const.responsecodeError,
        error : {
            code : errorCode,
            message : reason,
            retry : retry
        }
    });
    
}

RequestHandlerBase.prototype.successResponse = function(response,data){

    response.status(Const.httpCodeSucceed);
    response.json({
        success : Const.responsecodeSucceed,
        result : data
    });
    
}

module["exports"] = RequestHandlerBase;