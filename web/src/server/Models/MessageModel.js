var mongoose = require('mongoose');
var _ = require('lodash');
var Const = require('../const.js');
var async = require('async');
var Util = require('../lib/Utils');
var UserModel = require('./UserModel');
var Settings = require("../lib/Settings");

var MessageModel = function(){
    
};

MessageModel.prototype.model = null;

MessageModel.prototype.init = function(){

    // Defining a schema
    var messageSchema = new mongoose.Schema({
        user: { type: mongoose.Schema.Types.ObjectId, index: true },
        userID: { type: String, index: true },
        roomID: { type: String, index: true },
        type: Number,
        message: String,
        image: String,
        file: {
            file: {
                id: mongoose.Schema.Types.ObjectId,
	            name: String,
	            size: Number,
	            mimeType: String
            },
            thumb: {
                id: mongoose.Schema.Types.ObjectId,
	            name: String,
	            size: Number,
	            mimeType: String
            }
        },
        seenBy:[],
        location: {
	            lat: Number,
	            lng: Number
        },
        deleted: Number,
        created: Number
    });

    // add instance methods
    messageSchema.methods.addSeenBy = function (user,callBack) {
        
        var seenBy = this.seenBy;
        var self = this;

        var listOfUsers = [];
        
        _.forEach(seenBy,function(seenObj){
               
            listOfUsers.push(seenObj.user);
            
        });
                    
        if(_.indexOf(listOfUsers,user._id) == -1){

            seenBy.push({user:user._id,at:Util.now()});
            
            this.update({
                seenBy: seenBy
            },{},function(err,userResult){
            
                if(callBack)
                    callBack(err,self);              
            
            });
    
            
        }

    }

    this.model = mongoose.model(Settings.options.dbCollectionPrefix + "messages", messageSchema);
    return this.model;
        
}

MessageModel.prototype.findMessagebyId = function(id,callBack){

    this.model.findOne({ _id: id },function (err, user) {

        if (err) 
            console.error(err);
        
        if(callBack)
            callBack(err,user)
        
        
    });
            
}


MessageModel.prototype.findMessages = function(roomID,lastMessageID,limit,callBack){
            
    if(lastMessageID != 0){
        
        var self = this;
        
        this.model.findOne({ _id: lastMessageID },function (err, message) {

            if (err) return console.error(err);
            
            var lastCreated = message.created;
            
            var query = self.model.find({
                roomID:roomID,
                created:{$lt:lastCreated}
            }).sort({'created': 'desc'}).limit(limit);        
            
            query.exec(function(err,data){
                
                if (err)
                    console.error(err);
                
                if(callBack)
                    callBack(err,data)
                
            });                
                
        
        });
        
    }else{
        
        var query = this.model.find({roomID:roomID}).sort({'created': 'desc'}).limit(limit);        
    
        query.exec(function(err,data){
            
            if (err) return console.error(err);
            
            if(callBack)
                callBack(err,data)
            
        });
    
    
    }

}

MessageModel.prototype.populateMessages = function(messages,callBack){
    
    if(!_.isArray(messages)){
        
        messages = [messages];
        
    }
    
    // collect ids
    var ids = [];
    
    messages.forEach(function(row){
        
        // get users for seeny too
        _.forEach(row.seenBy,function(row2){
            ids.push(row2.user); 
        });
        
        ids.push(row.user); 
        
    });
    
    if(ids.length > 0){
    
        UserModel.findUsersbyInternalId(ids,function(err,userResult){
            
            var resultAry = [];
            
            _.forEach(messages,function(messageElement,messageIndex,messagesEntity){
                
                var obj = messageElement.toObject();
                
                _.forEach(userResult,function(userElement,userIndex){
                    
                    // replace user to userObj
                    if(messageElement.user.toString() == userElement._id.toString()){
                        obj.user = Util.stripPrivacyParams(userElement.toObject());
                    }

                }); 
                
                var seenByAry = [];
                
                // replace seenby.user to userObj
                _.forEach(messageElement.seenBy,function(seenByRow){
                    
                    _.forEach(userResult,function(userElement,userIndex){
                        
                        // replace user to userObj
                        if(seenByRow.user.toString() == userElement._id.toString()){
                            
                            seenByAry.push({
                                user:Util.stripPrivacyParams(userElement.toObject()),
                                at:seenByRow.at 
                            });
                            
                        }

                    });
                                                    
                });
                
                obj.seenBy = seenByAry;
                    
                resultAry.push(obj);
                
            });
            
                              
            callBack(err,resultAry);
                                   
        });
        
    }else{
        callBack(null,messages);
    }
    
}

    
module["exports"] = new MessageModel();