var _ = require('lodash');
var async = require('async');

var Const = require("../const");
var Utils = require("./Utils");

var DatabaseManager = require('./DatabaseManager');

function checkToken(request, response, next) {
    
    var token = request.headers['access-token'];
    var userModel = DatabaseManager.userModel;

    if(_.isEmpty(token)){

        response.json({
            code : Const.resCodeTokenError
        });
        
        return;
    }

    userModel.findOne({token:token},function(err,findResult){

        if(_.isEmpty(findResult)){

            response.json({
                code : Const.resCodeTokenError
            });
        
            return;
            
        }
        
        var tokenGenerated = findResult.tokenGeneratedAt;
        
        var diff = Utils.now() - tokenGenerated;

        if(diff > Const.tokenValidInteval){

            response.json({
                code : Const.resCodeTokenError
            });
        
            return;
            
        }
        
        request.user = findResult;
        
        next();
        
    });
    
}

module.exports = checkToken;
