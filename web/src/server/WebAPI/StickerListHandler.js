var express = require('express');
var router = express.Router();
var bodyParser = require("body-parser");
var path = require('path');
var _ = require('lodash');
var Request = require('request');

var RequestHandlerBase = require("./RequestHandlerBase");
var UsersManager = require("../lib/UsersManager");
var DatabaseManager = require("../lib/DatabaseManager");
var Utils = require("../lib/Utils");
var Const = require("../const");
var Config = require("../init");
var async = require('async');
var formidable = require('formidable');
var fs = require('fs-extra');
var path = require('path');
var mime = require('mime');
var SocketAPIHandler = require('../SocketAPI/SocketAPIHandler');

var StickerListHandler = function(){
    
}

_.extend(StickerListHandler.prototype,RequestHandlerBase.prototype);

StickerListHandler.prototype.attach = function(router){
        
    var self = this;

    /**
     * @api {get} /stickers Sticker List
     * @apiName Sticker List
     * @apiGroup WebAPI
     * @apiDescription Return list of stickers

     * @apiSuccessExample Success-Response:

{
	"code": 1,
	"data": {
		"stickers": [{
			"mainPic": "http://spika.chat/api/v2/sticker/b9jrzYulNunQrfFIgy9Xiv2w3SVB1Zpy",
			"list": [{
				"fullPic": "http://spika.chat/api/v2/sticker/8FzKKTcwl8YMJWIizBluXnlGBO3Zwpj2",
				"smallPic": "http://spika.chat/api/v2/sticker/b9jrzYulNunQrfFIgy9Xiv2w3SVB1Zpy"
			}, {
				"fullPic": "http://spika.chat/api/v2/sticker/19hxo8g5KhFgQLrzbziWSmmoCV7aCykg",
				"smallPic": "http://spika.chat/api/v2/sticker/3aqSVUCxP5fvI2PraJ9DoumHYu9oSKGg"
			}, {
				"fullPic": "http://spika.chat/api/v2/sticker/QjT3MDOiecxMFTqCHGH9Q4WIrmmiaoqP",
				"smallPic": "http://spika.chat/api/v2/sticker/QoZ20LPd7JRtPSsEfhCx86I3lqs0aOWw"
			}, {
				"fullPic": "http://spika.chat/api/v2/sticker/Qof2yrFvW5BstguLA7zQOG2VV5XC07TZ",
				"smallPic": "http://spika.chat/api/v2/sticker/c48qmefZEzjxGK8Bv3YaKOzKqMxPHU6y"
			}, {
				"fullPic": "http://spika.chat/api/v2/sticker/Oax2l7xCGeZ0SjR4WZuFsslnN4375Mlj",
				"smallPic": "http://spika.chat/api/v2/sticker/rUDFHI3BJDJOCFmnKhm90xzCPXWX4M2i"
			}, {

				"fullPic": "http://spika.chat/api/v2/sticker/vv8tszaB5qZ9uHuWeX2lZBjuiku5c8n9",
				"smallPic": "http://spika.chat/api/v2/sticker/m5UpjDqxDrcn43dBfrEWfDAbTAkLO24e"
			}, {
				"fullPic": "http://spika.chat/api/v2/sticker/kPoQiYSOaFIzg47zPBiNQoZaukXZYB0o",
				"smallPic": "http://spika.chat/api/v2/sticker/kYJDCQqyThkwmf6zuagkN0k6bkwzwPyu"
			}]
		}, {
			"mainPic": "http://spika.chat/api/v2/sticker/pQMwOMxxkdECAlfqJbOvOoITiUaL6ZJT",
			"list": [{
				"fullPic": "http://spika.chat/api/v2/sticker/T4gIS2iC6gbOv75889oE31GbnCEJ4OgR",
				"smallPic": "http://spika.chat/api/v2/sticker/pQMwOMxxkdECAlfqJbOvOoITiUaL6ZJT"
			}, {
				"fullPic": "http://spika.chat/api/v2/sticker/7eZzmei9Pwut6nsosFYmgJ05N7olP9Mo",
				"smallPic": "http://spika.chat/api/v2/sticker/aAckl7IhnJ28U8U7bnDSALGl7EvhGgND"
			}, {
				"fullPic": "http://spika.chat/api/v2/sticker/gS52BAxfOJZAP7CN9HLyFzXcjhF6Vgxa",
				"smallPic": "http://spika.chat/api/v2/sticker/tc4OG0zCt8pTxlhmJkxUNvET8mmLEjlx"
			}, {

				"fullPic": "http://spika.chat/api/v2/sticker/oSp6ltfWHkC5Kb7SjgIVSuUR72W1DdRo",
				"smallPic": "http://spika.chat/api/v2/sticker/vPYb8BuoDjjDkgfFIeNNDiNfH5Sd5PtB"
			}, {
				"fullPic": "http://spika.chat/api/v2/sticker/9SOVxUFQOTZvSy2rw1xA51c5Z2aAZRr9",
				"smallPic": "http://spika.chat/api/v2/sticker/N9yYXMJF7E5y8PSyiVQCYXmUSDuQfCZg"
			}, {
				"fullPic": "http://spika.chat/api/v2/sticker/Kga2eWk4xJv1lmC1YnnKGhoJM3e9hjhI",
				"smallPic": "http://spika.chat/api/v2/sticker/tnbe3Tksib3invpGFno9wVslSUnOVZml"
			}]
		}]
	}
}
    */
    
    router.get('',function(request,response){

        Request(Config.stickerAPI, function (error, responseAPI, body) {
            
            if (!error && responseAPI.statusCode == 200) {
                
                var obj = JSON.parse(body);
                
                if(obj && obj.data && obj.data.stickers){
                    
                    var list = obj.data.stickers;
                    
                    var mappedList = _.map(list,function(group){
                        
                        var stickers = {
                            
                            mainPic : Config.stickerBaseURL + group.mainTitlePic
                            
                              
                        };
                        
                        var list = _.map(group.list,function(row){
                            
                            return {
                                fullPic:Config.stickerBaseURL + row.fullPic,
                                smallPic:Config.stickerBaseURL + row.smallPic
                            }
                             
                        });
                        
                        stickers.list = list;
                        
                        return stickers;
                            
                    });
                    
                    self.successResponse(response,Const.responsecodeSucceed,{
                        stickers:mappedList
                    });
                    
                }else{
                    
                    self.successResponse(response,Const.resCodeStickerListFailed);
                    
                }

            }else{
                self.successResponse(response,Const.resCodeStickerListFailed);
            }
            
        });

    });

}

new StickerListHandler().attach(router);
module["exports"] = router;
