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

var FileDownloadHandler = function(){
    
}

_.extend(FileDownloadHandler.prototype,RequestHandlerBase.prototype);

FileDownloadHandler.prototype.attach = function(router){
        
    var self = this;

    /**
     * @api {get} /file/download/:fileID Download file by fileId
     * @apiName Download File
     * @apiGroup WebAPI
     *
     * @apiParam {fileID} fileID File ID
     *
     * @apiSuccess {Binary} ResponseBody  Entity of file
     */
    router.get('/:fileID', function (request, response) {

        var fileID = request.params.fileID;

        DatabaseManager.fileModel.findOne({ _id: fileID },function (err, file) {
            
            if (err) {
    
                self.errorResponse(
                    response,
                    Const.httpCodeSucceed,
                    Const.responsecodeParamError,
                    Utils.localizeString("Download Failed"),
                    false
                );
                                    
            } else {
                
                var filePath = Settings.options.uploadDir + fileID;
                var filename = file.name
                var mimetype = file.mimeType;
                
                fs.exists(filePath, function (exists) {
                    
                    if(!exists){
                        
                        self.errorResponse(
                            response,
                            Const.httpCodeFileNotFound,
                            0,
                            Utils.localizeString("Download Failed"),
                            false
                        );
                        
                    } else {
                        
                        response.setHeader('Content-disposition', 'filename=' + filename);
                        response.setHeader('Content-type', mimetype);
                        response.setHeader('Content-Length', file.size);

                        var filestream = fs.createReadStream(filePath);
                        filestream.pipe(response); 
                        
                    }
                    
                });
                    
            }
        
        });
                  

    });
    
}


new FileDownloadHandler().attach(router);
module["exports"] = router;
