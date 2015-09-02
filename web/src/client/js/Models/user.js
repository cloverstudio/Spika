var Backbone = require('backbone');
var _ = require('lodash');

(function(global) {
    "use strict;"

    // Class ------------------------------------------------
    var UserModel = Backbone.Model.extend({
        defaults: {
            id: "",
            name: "Not specified",
            avatarURL: "Not specified",
            token: ""
        },
        initialize: function(){
    
        }
    });

    var UserCollection = Backbone.Collection.extend({
        model: UserModel
    });
    
    var user = {
        Model:UserModel,
        Collection:UserCollection,
    }
    
    user.modelByResult = function(obj){
        
        var model = new UserModel({
            id: obj.userID,
            name: obj.name,
            avatarURL: obj.avatarURL,
            token: obj.token
        });

        return model;
                
    }
    
    user.collectionByResult = function(obj){
        
        if(!_.isArray(obj))
            return null;
        
        var aryForCollection = [];
        var collection =  new UserCollection(null);
        
        _.each(obj,function(row){
            
            var modelUser = new UserModel({
                id: row.userID,
                name: row.name,
                avatarURL: row.avatarURL,
                token: row.token
            });
            
            collection.add(modelUser);
                          
        });
        
        
        
        
        return collection;
                
    }
    
    module["exports"] = user;

})((this || 0).self || global);
