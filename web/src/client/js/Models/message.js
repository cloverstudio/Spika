var Backbone = require('backbone');
var _ = require('lodash');
var User = require('./user');

(function(global) {
    "use strict;"

    // Class ------------------------------------------------
    var MessageModel = Backbone.Model.extend({
        defaults: {
            id: "",
            localID: "",
            userID: "",
            message: "",
            type: 1,
            created: 0,
            deleted: 0,
            user: null,
            file:{
                file:null,
                thubm:null,
            },
            seenBy:[],
            status: 0
        },
        initialize: function(){
    
        },
        toObject:function(){
            
            var obj = $.extend(true, {}, this.attributes);
            obj.user = obj.user.attributes;
            
            var seebByObjAry = [];
            
            _.forEach(obj.seenBy,function(row){
                
                seebByObjAry.push({
                    user: row.user.attributes,
                    at : row.at
                });
                
            });
            
            obj.seenBy = seebByObjAry;
            
            return obj;
            
        }
    });

    var MessageCollection = Backbone.Collection.extend({
        model: MessageModel,
        comparator : function(model) {
            return model.get('created');
        },
        findMessageByID : function(messageID){
            
            return this.findWhere({ "id": messageID });
            
        },
        findMessageByLocalID : function(localID){
            
            return this.findWhere({ "localID": localID });
            
        },
        swap: function(messageOld,messageNew){
            
            if(messageOld.get('created') == messageNew.get('created') ||
                messageOld.get('localID') == messageNew.get('localID')){
                this.remove(messageOld);
                this.add(messageNew);
                console.log('swapped');
            }
            
        }
    });
    
    var message = {
        Model:MessageModel,
        Collection:MessageCollection,
    }
    
    message.modelByResult = function(obj){
        
        var model = new MessageModel({
            id: obj._id,
            userID: obj.userID,
            localID: obj.localID,
            message: obj.message,
            type: obj.type,
            file: obj.file,
            created: obj.created,
            deleted: obj.deleted
        });
        
        if(!_.isUndefined(obj.seenBy)){
            
            var seenByArray = [];
            
            _.forEach(obj.seenBy,function(seenByRow){
                
                var userModel = User.modelByResult(seenByRow.user);
                
                seenByArray.push({
                    user: userModel,
                    at: seenByRow.at  
                });
                
            });
            
            model.set('seenBy',seenByArray);
            
        }
        
        if(!_.isNull(obj.user) && !_.isUndefined(obj.user)){
            
            var userModel = User.modelByResult(obj.user);
            
        }
        
        model.set('user',userModel);
                
        return model;
                
    }


    message.collectionByResult = function(obj){
        
        if(!_.isArray(obj))
            return null;
        
        var aryForCollection = [];
        
        _.each(obj,function(row){

            aryForCollection.push(message.modelByResult(row));
             
        });
        
        return new MessageCollection(aryForCollection);
                
    }
    
    module["exports"] = message;

})((this || 0).self || global);
