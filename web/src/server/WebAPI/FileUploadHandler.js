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

var FileUploadHandler = function(){
    
}

_.extend(FileUploadHandler.prototype,RequestHandlerBase.prototype);

FileUploadHandler.prototype.attach = function(router){
        
    var self = this;
    
    /**
     * @api {post} /file/upload  Upload File 
     * @apiName Upload File
     * @apiGroup WebAPI
     * @apiDescription Upload file and get file id by response

     * @apiParam {File} file urlencoded multy part field name
     *
     * @apiSuccess {String} Token
     * @apiSuccess {String} User Model of loginned user
     *     
     * @apiSuccessExample Success-Response:
        {
            "code": 1,
            "data": {
                "file": {
                    "id": "55cdeba8a2d0956d24b421df",
                    "name": "Procijena.xlsx",
                    "size": 493966,
                    "mimeType": "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                }
            }
        }
    */
    router.post('',function(request,response){
        
        var form = new formidable.IncomingForm();
        
        async.waterfall([
            
            function (done) {
                
                fs.exists(Settings.options.uploadDir, function (exists) {
                    
                    if(exists){
                        
                        /* works only node > 0.12
                        fs.access(Config.uploadDir, fs.R_OK | fs.W_OK, function(err) {
                            
                            if(err){

                                console.log('Please check permission of upload dir');
                                done("Upload dir doesnt has permission",{});
                                
                            }else {

                                done(null,{});
                                                                
                            }
                        }); 
                        */
                        
                        done(null,{});
                        
                    } else {

                        console.log('Please check path of upload dir');                        
                        done("Upload dir doesnt exist",{});
                        
                    }
                    
                });        
                        
            },
            function (result,done) {
                
                form.parse(request, function(err, fields, files) {
            
                    if(!files.file){
                        
                        self.successResponse(response,Const.resCodeFileUploadNoFile);
                        return; 
                        
                    }else{
                    
                        var tempPath = files.file.path;
                        var fileName = files.file.name;
                
                        var destPath = Settings.options.uploadDir;
                        
                        done(err,files.file);

                    }
                    
                });
                
            },
            
            function (file,done){
                                    
                var tempPath = file.path;
                var fileName = file.name;
                
                // save to database
                var newFile = new DatabaseManager.fileModel({
                    name:fileName,
                    mimeType: file.type,
                    size: file.size,
                    created: Utils.now()                   
                });
                                             
                newFile.save(function(err,fileModel){
                                            
                    done(err,{
                        file:file,
                        fileModel:fileModel
                    });
                
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
                        var tempThumbFileName = result.fileModel.id + "_thumb.jpg"; // force to be jpg
                        var destPathTmp = Settings.options.uploadDir + tempThumbFileName;
                        
                        easyimg.thumbnail({
                                src: file.path, dst:destPathTmp,
                                width:256, height:256
                            }).then(
                            function(image) {
                                
                                // save to database
                                var thumbObj = new DatabaseManager.fileModel({
                                    name:"thumb_" + result.file.name,
                                    mimeType: "image/jpeg",
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
               
            
        ],
            function (err, result) {
                
                if(err){

                    self.errorResponse(
                        response,
                        Const.httpCodeSeverError
                    );
                
                }else{
                                            
                    var responseJson = {
                        file:{
    		                id: result.fileModel.id,
        		            name: result.file.name,
        		            size: result.file.size,
        		            mimeType: result.file.type
                        }
                    };
                    
                    if(!_.isUndefined(result.thumbModel)){
                        responseJson.thumb = {
    		                id: result.thumbModel.id,
        		            name: result.thumbModel.name,
        		            size: result.thumbModel.size,
        		            mimeType: result.thumbModel.mimeType
                        };
                    }
                    
                    self.successResponse(response,Const.responsecodeSucceed,
                        responseJson);
                }
                     
            }
        );
        
    });

}
new FileUploadHandler().attach(router);
module["exports"] = router;
