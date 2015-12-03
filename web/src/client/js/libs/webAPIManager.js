var CONST = require('../consts');
var _ = require('lodash');
var U = require('./utils.js');
var ErrorDialog = require('../Views/Modals/ErrorDialog/ErrorDialog');
var LoginUserManager = require('./loginUserManager.js');

(function(global) {
    "use strict;"

    var webAPIManager = {

        post : function(url,data,onSuccess,onError){
            
            var self = this;
            
            var header = {};
            
            if(!_.isNull(LoginUserManager.user)){
                var token = LoginUserManager.user.get('token');
                
                if(!_.isEmpty(token)){
                    header.token = token;
                }
            }
            
            $.ajax({
                type : "POST",
                url : url,
                data : JSON.stringify(data),
                dataType: 'json',
                contentType: "application/json; charset=UTF-8",
                headers: header,
                success: function (response) {  
                    
                    var errorCode = response.code;
                    
                    // server handled error
                    if(errorCode != 1){

                        var message = CONST.ERROR_CODES[errorCode];
                        
                        ErrorDialog.show('Error',message);
                        
                        if(!_.isUndefined(onError)){
                            onError();
                        }
                    }
                    
                    if(errorCode == 1){
                                                
                        if(!_.isUndefined(onSuccess))
                            onSuccess(response.data);
                        
                    }
                    
                    
                },
                statusCode: {
                    403: function() {
                    }
                },
                error: function (e) {
                        
                    ErrorDialog.show('Network Error','Critical Error',function(){
                            
                        ErrorDialog.hide(function(){
                            self.post(url,data,onSuccess,onError);
                        });
                        
                    });
                    
                    if(!_.isUndefined(onError)){
                        onError();
                    }

                } 
            });
                
        },
    
        fileUpload : function(url,file,onProgress,onSuccess,onError){
            
            var self = this;
            
            var header = {};
            
            if(!_.isNull(LoginUserManager.user)){
                var token = LoginUserManager.user.get('token');
                
                if(!_.isEmpty(token)){
                    header.token = token;
                    
                    
                    
                }
            }
            

            var data = new FormData();
            
            data.append('file', file);
                        
            $.ajax({
                type : "POST",
                url : url,
                data : data,
                dataType: 'json',
                contentType: false,
                processData: false,
                headers: header,
                xhr: function(){
                
                    var xhr = $.ajaxSettings.xhr() ;
                    
                    xhr.upload.addEventListener("progress", function(evt) {
                    
                        if(onProgress)
                            onProgress(evt.loaded/evt.total);
                            
                    }, false);

                    return xhr ;
                },
                success: function (response) {  
                                        
                    var errorCode = response.code;
                    
                    // server handled error
                    if(errorCode != 1){
                        
                        var message = CONST.ERROR_CODES[errorCode];
                        
                        ErrorDialog.show('Error',message);
                        
                        if(!_.isUndefined(onError)){
                            onError();
                        }
                        
                    }
                    
                    if(errorCode == 1){
                                       
                        if(!_.isUndefined(onSuccess))
                            onSuccess(response.data);
                        
                    }
                    
                    
                },
                statusCode: {
                    403: function() {
                    }
                },
                error: function (e) {
                    
                    ErrorDialog.show('Network Error','Critical Error',function(){
                            
                        ErrorDialog.hide(function(){
                            self.post(url,data,onSuccess,onError);
                        });
                        
                    });
                    
                    if(!_.isUndefined(onError)){
                        onError();
                    }

                } 
            });
                
        },
        get : function(url,onSuccess,onError){

            var header = {};
            
            if(!_.isNull(LoginUserManager.user)){
                var token = LoginUserManager.user.get('token');
                
                if(!_.isEmpty(token)){
                    header.token = token;
                    
                    
                        
                }
            }

            $.ajax({
                type : "GET",
                url : url,
                dataType: 'json',
                contentType: "application/json; charset=UTF-8",
                headers: header,
                success: function (response) {  
                    
                    var errorCode = response.code;
                    
                    // server handled error
                    if(errorCode != 1){
                        
                        var message = CONST.ERROR_CODES[errorCode];
                        
                        ErrorDialog.show('Error',message);

                        
                    }
                    
                    if(errorCode == 1){
                                                
                        if(!_.isUndefined(onSuccess))
                            onSuccess(response.data);
                        
                    }
                    
                    
                },
                statusCode: {
                    403: function() {
                    }
                },
                error: function (e) {
                        
                    ErrorDialog.show('Network Error','Critical Error',function(){
                            
                        ErrorDialog.hide(function(){
                            self.get(url,onSuccess,onError);
                        });
                        
                    });

                    if(!_.isUndefined(onError)){
                        onError();
                    }

                } 
            });

        }
            
    };
 
    // Exports ----------------------------------------------
    module["exports"] = webAPIManager;

})((this || 0).self || global);