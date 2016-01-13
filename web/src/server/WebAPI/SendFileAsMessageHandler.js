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
var SocketAPIHandler = require('../SocketAPI/SocketAPIHandler');
var Settings = require("../lib/Settings");

var SendMessageLogic = require('../Logics/SendMessage');


var TempHandler = function(){
    
}

_.extend(TempHandler.prototype,RequestHandlerBase.prototype);

TempHandler.prototype.attach = function(router){
        
    var self = this;

    /**
     * @api {post} /message/sendFile Send File as Message
     * @apiName Send File as Message
     * @apiGroup WebAPI
     * @apiDescription This API is used only by old browsers like IE8

     * @apiParam {File} file urlencoded multy part field name
     * @apiParam {string} fileID File ID
     * @apiParam {string} userID File ID
     * @apiParam {string} type File ID
     *
     * @apiSuccess {String} Token
     * @apiSuccess {String} User Model of loginned user
     *     
     * @apiSuccessExample Success-Response:
        {
          "code": 1,
          "data": 'ok'
        }
    */
    router.post('',function(request,response){
        
        var form = new formidable.IncomingForm();
                            
        async.waterfall([

            function (done) {
                
                form.parse(request, function(err, fields, files) {
                    
                    // validation
                    if(_.isEmpty(files.file)){
                        
                        self.successResponse(response,Const.resCodeSendMessageNoFile);
                        return; 
                    };

                    if(_.isEmpty(fields.roomID)){
                        
                        self.successResponse(response,Const.resCodeSendMessageNoRoomID);
                        return; 
                    };
 
                    if(_.isEmpty(fields.userID)){
                        
                        self.successResponse(response,Const.resCodeSendMessageNoUserID);
                        return; 
                    };
 
  
                    if(_.isEmpty(fields.type)){
                        
                        self.successResponse(response,Const.resCodeSendMessageNoType);
                        return; 
                    };
                                                                   
                    
                    var tempPath = files.file.path;
                    var fileName = files.file.name;
            
                    var destPath = Settings.options.uploadDir;
                    
                    
                    // search user
                    done(err,{file:files.file,fields:fields});
                    
                });
                
            },
                        
            function (result,done){
                
                var tempPath = result.file.path;
                var fileName = result.file.name;
                
                // save to database
                var newFile = new DatabaseManager.fileModel({
                    name:fileName,
                    mimeType: result.file.type,
                    size: result.file.size,
                    created: Utils.now()                   
                });
                        
                newFile.save(function(err,fileModel){
                    
                    result.fileModel = fileModel;           
                    done(err,result);
                
                });
                
            },
            
            function (result,done){

                var tempPath = result.file.path;
                var fileName = result.file.name;
        
                var destPath = Settings.options.uploadDir;
        
                fs.copy(tempPath, destPath + result.fileModel._id, function(err) {
                
                    if (err){
                        
                        done(err,null);
                        
                    } else {
                             
                        done(err,result);
                        
                    }
                
                });
                
            },

            function (result,done){
                
                var file = result.file;
                             
                if(file.type.indexOf("jpeg") > -1 ||
                    file.type.indexOf("gif") > -1 ||
                    file.type.indexOf("png") > -1){
                    
                        var easyimg = require('easyimage');
                        var tempThumbFileName = result.fileModel.id + "_thumb";
                        var destPathTmp = Settings.options.uploadDir + tempThumbFileName;
                        
                        easyimg.thumbnail({
                                src: file.path, dst:destPathTmp,
                                width:256, height:256
                            }).then(
                            function(image) {
                                
                                // save to database
                                var thumbObj = new DatabaseManager.fileModel({
                                    name:"thumb_" + result.file.name,
                                    mimeType: result.file.type,
                                    size: image.size,
                                    created: Utils.now()                   
                                });
                                                             
                                thumbObj.save(function(err,thumbModel){

                                    var thumbFileName = thumbModel._id
                                    var destPath = Settings.options.uploadDir + thumbFileName;
        
                                    // rename
                                    fs.rename(destPathTmp, destPath, null);
                                    
                                    result.thumbModel = thumbModel;
                                                                            
                                    done(err,result);
                                
                                });

                            },
                            function (err) {
                                // ignore thubmnail error
                                console.log(err);
                                done(null,result);
                            }
                        );
                    
                } else {
                    
                    done(null,result);
                    
                }

                
            },

            function (result,done){

                var param = {
                    
                    roomID: result.fields.roomID,
                    message: '',
                    type: result.fields.type,
                    file: {
                        file : {
    		                id: result.fileModel.id,
        		            name: result.fileModel.name,
        		            size: result.fileModel.size,
        		            mimeType: result.fileModel.mimeType 
                        }
                    }
                };


                if(!_.isUndefined(result.thumbModel)){
                    param.file.thumb = {
		                id: result.thumbModel.id,
    		            name: result.thumbModel.name,
    		            size: result.thumbModel.size,
    		            mimeType: result.thumbModel.mimeType
                    };
                }
                

                SendMessageLogic.execute(result.fields.userID,param,function(){
                    done(null,result);
                });

            }
            
        ],
            function (err, result) {
                
                response.send('ok');
                             
            }
        );
        
    });


}


new TempHandler().attach(router);
module["exports"] = router;
