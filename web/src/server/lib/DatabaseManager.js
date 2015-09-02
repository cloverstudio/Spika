var mongoose = require('mongoose');
var _ = require('lodash');
var Const = require('../const.js');

var DatabaseManager = {
    
    messageModel:null,
    userModel:null,
    fileModel:null,
    
    init: function(options){
		
		var self = this;
		
        // Connection to our chat database
        console.log("Connecting mongoDB " + options.chatDatabaseUrl);
        
        try{
	        
	        mongoose.connect(options.chatDatabaseUrl, function(err){

	          if (err) {
		          
	            console.log("Failed to connect MongoDB!");
	            console.error(err);
	            
	          } else {
		        
		        // Defining a schema

		        

                self.messageModel = require('../Models/MessageModel').init();
		        self.userModel = require('../Models/UserModel').init();
		        self.fileModel = require('../Models/FileModel').init();
				
		
	          }
	        });
	
        } catch(ex){
	        
	        console.log("Failed to connect MongoDB!");

	        throw ex;
	        
        }

    }
    
}

module["exports"] = DatabaseManager;