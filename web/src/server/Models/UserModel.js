var mongoose = require('mongoose');
var _ = require('lodash');
var Const = require('../const.js');
var async = require('async');
var Util = require('../lib/Utils');
var Settings = require("../lib/Settings");

var UserModel = function(){
    
};

UserModel.prototype.model = null;

UserModel.prototype.init = function(){

    // Defining a schema
    var userSchema = new mongoose.Schema({
        userID: { type: String, index: true },
        name: String,
        avatarURL: String,
        token: String,
        created: Number
    });
 
    this.model = mongoose.model(Settings.options.dbCollectionPrefix + "users", userSchema);
    return this.model;
		        
}

UserModel.prototype.findUserbyId = function(id,callBack){

    this.model.findOne({ userID: new RegExp("^" + id + "$","g") },function (err, user) {

        if (err) 
            console.error(err);
        
        if(callBack)
            callBack(err,user);
        
    });
            
}


UserModel.prototype.findUsersbyInternalId = function(aryId,callBack){
        
        var conditions = [];
        aryId.forEach(function(userId){
            
            conditions.push({
                _id : userId 
            });
            
        });
        
        var query = this.model.find({
            $or : conditions
        }).sort({'created': 1});        
        
        query.exec(function(err,data){
            
            if (err)
                console.error(err);
            
            if(callBack)
                callBack(err,data)
            
        });                
                
    },


module["exports"] = new UserModel();