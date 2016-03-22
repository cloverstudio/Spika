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
     * @api {post} /stickers Sticker List
     * @apiName Sticker List
     * @apiGroup WebAPI
     * @apiDescription Return list of stickers

     * @apiSuccessExample Success-Response:

{
	"code": 1,
	"data": {
		"stickers": [{
			"mainPic": "http://spika.chat/api/v2/sticker/b9jrzYulNunQrfFIgy9Xiv2w3SVB1Zpy",
			"list": ["http://spika.chat/api/v2/sticker/b9jrzYulNunQrfFIgy9Xiv2w3SVB1Zpy", "http://spika.chat/api/v2/sticker/3aqSVUCxP5fvI2PraJ9DoumHYu9oSKGg", "http://spika.chat/api/v2/sticker/QoZ20LPd7JRtPSsEfhCx86I3lqs0aOWw", "http://spika.chat/api/v2/sticker/c48qmefZEzjxGK8Bv3YaKOzKqMxPHU6y", "http://spika.chat/api/v2/sticker/rUDFHI3BJDJOCFmnKhm90xzCPXWX4M2i", "http://spika.chat/api/v2/sticker/hVGn5nyGZlbqppawUciZ4kWj0eZOZnrK", "http://spika.chat/api/v2/sticker/BwFwTPLLCSvzZShBQQJ450ls1OSIvPEM", "http://spika.chat/api/v2/sticker/TdyqkgPIQzAgRtVOw2OOjwjDvPSYKTlP", "http://spika.chat/api/v2/sticker/BiLZ64jXkAI6ALYe5lQD4lse486b219m", "http://spika.chat/api/v2/sticker/IrDil2WYU3m5m1RYl9XfqB8c9mHFX8dM", "http://spika.chat/api/v2/sticker/3broN4vD7YJP5pdIjYUBhHvIDOFctBfx", "http://spika.chat/api/v2/sticker/7bFhjXlex2NVtK3PHgLQJ0LElNxFzddv", "http://spika.chat/api/v2/sticker/kqyaVPJmcV6ebIYjRFJB6X1DTqcpWTgH", "http://spika.chat/api/v2/sticker/eptGRbAR9KP5ozEhn7L1QR631DGHGOMu", "http://spika.chat/api/v2/sticker/klBwbghPluy36Aq4H6IhaEEzrTiN8pFP", "http://spika.chat/api/v2/sticker/DkJX9uFhIH5x2CHl6CTHBenABVAvOMkB", "http://spika.chat/api/v2/sticker/1ANNMBxOsgy9QdjDgvu2q5vannDsdMqh", "http://spika.chat/api/v2/sticker/lZe8A5s5Dpa0KYqnpyWx5MlGGfrkCP9I", "http://spika.chat/api/v2/sticker/XuqQw8BsPcWhKBYmmQSHhaxduBfvB7ao", "http://spika.chat/api/v2/sticker/uWwEcVMjEgp31lWKHue5elGM4ZKJq4CM", "http://spika.chat/api/v2/sticker/Xv7wxvnbld9Qpu62sA9J2rRg6zuhOydL", "http://spika.chat/api/v2/sticker/JA7k7ACEhtbdjvkhjSOYZuCSCVta4YVQ", "http://spika.chat/api/v2/sticker/m5UpjDqxDrcn43dBfrEWfDAbTAkLO24e", "http://spika.chat/api/v2/sticker/kYJDCQqyThkwmf6zuagkN0k6bkwzwPyu"]
		}, {
			"mainPic": "http://spika.chat/api/v2/sticker/pQMwOMxxkdECAlfqJbOvOoITiUaL6ZJT",
			"list": ["http://spika.chat/api/v2/sticker/pQMwOMxxkdECAlfqJbOvOoITiUaL6ZJT", "http://spika.chat/api/v2/sticker/aAckl7IhnJ28U8U7bnDSALGl7EvhGgND", "http://spika.chat/api/v2/sticker/tc4OG0zCt8pTxlhmJkxUNvET8mmLEjlx", "http://spika.chat/api/v2/sticker/86tp8CCo1mb57TxQJTksRBv5IpYtpVYC", "http://spika.chat/api/v2/sticker/Im6Ms2s188hnDRQPzpP8IrnAGN3EWKjS", "http://spika.chat/api/v2/sticker/NDHK2ez8r8mZV4OMTqbz6oKyOlfGjaA1", "http://spika.chat/api/v2/sticker/8hRKl2TQERV5pScEB71vbZfEFH1oBgWD", "http://spika.chat/api/v2/sticker/Q6MgqAfBtW98QDzA1o1JIITkfI61g2LJ", "http://spika.chat/api/v2/sticker/hRwhEMmS2lCUWfbXGcrnIg6r2UigsiG4", "http://spika.chat/api/v2/sticker/ccxgpuyoo4h6BIOExulbGbvJrwoBaf2W", "http://spika.chat/api/v2/sticker/Pea4OYqN1z37WWpZDhJadEnCw8RpiG6a", "http://spika.chat/api/v2/sticker/6Zvf8kzjsjZjwm42Pk8BSh6DHa7RDIOo", "http://spika.chat/api/v2/sticker/kISbMv27ODZOsVp8keGsfbxxWdnsMAVe", "http://spika.chat/api/v2/sticker/JUsE3OrWnxsfIw8X4HDSaCymafDgSR3b", "http://spika.chat/api/v2/sticker/3gemWLEN4Zmy6abUpuBI6goKGHCVJtJO", "http://spika.chat/api/v2/sticker/wt7xL9WvKOaJWe9qsjlG6Ca2JtDsnAUx", "http://spika.chat/api/v2/sticker/luRBFSvsB4SpWbdXcs7r48fwaqfLr0wV", "http://spika.chat/api/v2/sticker/IN0494KgdlfMzUOnlmr2nXek25IWoekS", "http://spika.chat/api/v2/sticker/1VJj3MjQtNC6rdxSRnr2rNGynv6YkBt1", "http://spika.chat/api/v2/sticker/USg3tjECZUZvUNijvCltQ2KE5UuY0hER", "http://spika.chat/api/v2/sticker/Ypv3KD4N1yuNvPKr1oZSO4wqME0HAqxt", "http://spika.chat/api/v2/sticker/WskavLBKWDt4DsmBDbUzR7tLVXrMPucC", "http://spika.chat/api/v2/sticker/YE7Web70RCxTlTekDFtj1KyCezJdDeKD", "http://spika.chat/api/v2/sticker/469L3wbYcGSrQds6SDQncuLBWxSSG8IG", "http://spika.chat/api/v2/sticker/Ft3ym30ywBXS8lTrdB2RyDWzcjJjEdMH", "http://spika.chat/api/v2/sticker/XmvFB08dJPlazKOlby3YbbdHz7ZqQKHu", "http://spika.chat/api/v2/sticker/DgldNrgFXL6Qq3i3RYKKBxTBAElOfLOE", "http://spika.chat/api/v2/sticker/ieaAVLnUfMK4wd4adN6mKk6L8vy5u0U0", "http://spika.chat/api/v2/sticker/LHvV9CoIu6VK5Ed4y7jTjiok1QejIVP8", "http://spika.chat/api/v2/sticker/ZTKqUNWkpPGg4vRTV1joy3wSjjXDKagL", "http://spika.chat/api/v2/sticker/2O7WljYcjyH4bYAtEO10qfbzIz1yAYQN", "http://spika.chat/api/v2/sticker/UhJg1xBLrk1FrCaCllQ3nkljfmH0IuyK", "http://spika.chat/api/v2/sticker/zjmYyHeSfWip9kHb2M8LUDjp4Fb2p1HH", "http://spika.chat/api/v2/sticker/FYtFvM89Frrd6zvseK52ByuaVjFl3izy", "http://spika.chat/api/v2/sticker/OaHNzTmPVQJUqjx8E0sRTTE5A29JpfqZ", "http://spika.chat/api/v2/sticker/reFmvMzRJqcMpPuqit0qYtShtFta5Lix", "http://spika.chat/api/v2/sticker/znQYBySgxn30BFkLVMow3Xjge1NB4aen", "http://spika.chat/api/v2/sticker/q3dcqfkUUl72r2ap43ETjrwasqDar0A1", "http://spika.chat/api/v2/sticker/aYT2Z7oBj00wx15UKOvnMqCNmrKZoxyD", "http://spika.chat/api/v2/sticker/wuti3xYXnrsQMg6pjghqUGecQ1DNErGS", "http://spika.chat/api/v2/sticker/H9797Ks6X6drR6qoEzoBggg2pjyjkpsT", "http://spika.chat/api/v2/sticker/3UxSzRImlGwOxFvSJIki7pZVTTombvbO", "http://spika.chat/api/v2/sticker/OTDMSAFvw2Hykh9TO1U0csAT9JxR5EVH", "http://spika.chat/api/v2/sticker/AXQJ6jeIqeXGLodqBPXkdN1ym8im0Hxn", "http://spika.chat/api/v2/sticker/j2UaA8xeEAcIKd9nbrxbIC992xprkzVE", "http://spika.chat/api/v2/sticker/m0PjeXx0S1q5zzBA0lWj1aoYETMdCv7d", "http://spika.chat/api/v2/sticker/NAu94cL1et5RCJBwiNYyV1TQiLQw7DMK", "http://spika.chat/api/v2/sticker/vPYb8BuoDjjDkgfFIeNNDiNfH5Sd5PtB", "http://spika.chat/api/v2/sticker/N9yYXMJF7E5y8PSyiVQCYXmUSDuQfCZg", "http://spika.chat/api/v2/sticker/tnbe3Tksib3invpGFno9wVslSUnOVZml"]
		}, {
			"mainPic": "http://spika.chat/api/v2/sticker/tkPnWFQ9S1AwFc8zOVgUhrOSMBvKtUHH",
			"list": ["http://spika.chat/api/v2/sticker/tkPnWFQ9S1AwFc8zOVgUhrOSMBvKtUHH", "http://spika.chat/api/v2/sticker/AljlTnVfFiLgowcSJ5CxSy17P0l2AcTu", "http://spika.chat/api/v2/sticker/ZU7ZJZB7ZIVSFMrVSGHC26ehSpmoIfwu", "http://spika.chat/api/v2/sticker/Nqd25t3FwEARg1GhYZwi5dRWthtiCu5T", "http://spika.chat/api/v2/sticker/0A81HjrLjjmNUDYilf3AS5gOtbJf9uCZ", "http://spika.chat/api/v2/sticker/kTXJgNek96Bph1Jh5Vwj7pnPLdEl2EoV", "http://spika.chat/api/v2/sticker/dgAPiamwkCFMQfcV8VlC6ULJeOeESMjp", "http://spika.chat/api/v2/sticker/HcrISW4vbFJc5ljsdlQRBN7JEGeKIIiV", "http://spika.chat/api/v2/sticker/AcSvseKiih8in4l5C9Dds1DJYePQU5Mr", "http://spika.chat/api/v2/sticker/zKkIgueo6JnHQhd8ce6tdtMX0pqzwMvp", "http://spika.chat/api/v2/sticker/q8owxaPO0swYOhKIgIJZMzEehY55wpIQ", "http://spika.chat/api/v2/sticker/nSCbpo762FnFdRh3IPySFjOjhc6GXcKD", "http://spika.chat/api/v2/sticker/vI4GaTL3q49Vy3PJgplOjwmjZw8bzfuI", "http://spika.chat/api/v2/sticker/kze3d8nDksouopqvKAewlvOSq2x99xWR", "http://spika.chat/api/v2/sticker/6W5cyTAvJNixxD27TlJi2a05emqj8tAD", "http://spika.chat/api/v2/sticker/p4sC8mPTFqBhOOiMcK4C9odhkF6YPSL4", "http://spika.chat/api/v2/sticker/ADnnsxqXeqtBwDftbJKE26blg7Emib2m", "http://spika.chat/api/v2/sticker/JC145PKUk3XWzlpvsu7UZqa0zudiBM1H", "http://spika.chat/api/v2/sticker/nXdStkdGIMB65f8IOWqDJsrnLENx9dMX", "http://spika.chat/api/v2/sticker/B35RjhhHjsIkQfCdjANauhVRZsU734lP", "http://spika.chat/api/v2/sticker/4CmiJphgMuMFVDfFPuDU6LKQp5eg2QFN", "http://spika.chat/api/v2/sticker/4gyFDJ5La24cAfcvJs2oFPPNtV1QsDxQ", "http://spika.chat/api/v2/sticker/jx7KX7C7qyqNgAR2DkvnfIQTr99Q7WGW", "http://spika.chat/api/v2/sticker/LdEphx92kqI10ErOrnJqMWSlMn7i9k5K", "http://spika.chat/api/v2/sticker/l2yvAScl2VTAGEqACFywlH3nGNGUAacn"]
		}, {
			"mainPic": "http://spika.chat/api/v2/sticker/So4r38nT7AZdIlcWp6fRnU6QQ1bbnf2R",
			"list": ["http://spika.chat/api/v2/sticker/So4r38nT7AZdIlcWp6fRnU6QQ1bbnf2R", "http://spika.chat/api/v2/sticker/jiz2LSH8D67Vr7UVf7fJTQs7XE3N2I7F", "http://spika.chat/api/v2/sticker/7S68zmBsen4Dy2e3tboAmybAmkdCWSpu", "http://spika.chat/api/v2/sticker/ih19PzXyJ57MPn0NhzHJndj60GEwedCN", "http://spika.chat/api/v2/sticker/Uo3SUobWOVGdFnpC1AQSKJRID1JpvWEE", "http://spika.chat/api/v2/sticker/sWd1LG5xrV4LM5gUBMnk9NZDeLT43ADf", "http://spika.chat/api/v2/sticker/WsklSmhqKou03AJnszZg9GQkaWJYvuT4", "http://spika.chat/api/v2/sticker/hCpt3UQnDzRqMjBUzaKM1H1fQM9CzSpU", "http://spika.chat/api/v2/sticker/FDm60Rcbxx1Nvu91PfusQirM6CuSuXom", "http://spika.chat/api/v2/sticker/GseEPyOCniSzYPyMhpXhC1MBk7hKZ8BG", "http://spika.chat/api/v2/sticker/KUzNHsYORJY49N78Mc8JMvrEWId0NxY5", "http://spika.chat/api/v2/sticker/feqol90XEstvbYztYrmtMpi7dHNarRQi", "http://spika.chat/api/v2/sticker/XUGteHrStHprGEMJd4Iu8bOaqv5UwAH3", "http://spika.chat/api/v2/sticker/C1ud39kPuuEnkHarioKss5HtRphrmS6Z", "http://spika.chat/api/v2/sticker/OInx4lno9I5bzfwjKyLrJKWWBWBizysF", "http://spika.chat/api/v2/sticker/Oe8CydNyzKMiMzb0pgHUfEONctrF4umz", "http://spika.chat/api/v2/sticker/EtToaoxghmjwbcU6P5t2uQ6omIO0GVHo", "http://spika.chat/api/v2/sticker/Xa3WB6lMrGTKO9ymS4VspUM4knTctho6", "http://spika.chat/api/v2/sticker/3t3xRvSGZcInsZQbxM5Tk5GdrxJw6vZl", "http://spika.chat/api/v2/sticker/PZyAxLVJU0QtBYM9dg4TXx0xZSXWYj2o"]
		}, {
			"mainPic": "http://spika.chat/api/v2/sticker/qj1tzgGew00VRpjrBfQKZlFJe45K3DG8",
			"list": ["http://spika.chat/api/v2/sticker/qj1tzgGew00VRpjrBfQKZlFJe45K3DG8", "http://spika.chat/api/v2/sticker/rNo7TjnYMBwUzNoK8yQqbQOZay8qvHF8", "http://spika.chat/api/v2/sticker/7uGBmF68vk2kfWBnIWPltNIADscRGFXR", "http://spika.chat/api/v2/sticker/jMIZO6n8alYgxhQqjXiekMBM7rj03wKZ", "http://spika.chat/api/v2/sticker/55TITAMRHrj1qUFxjG2kVjpmW4goIOWg", "http://spika.chat/api/v2/sticker/T27aHrj6J2cLcfnpUnG1r1sFBceYP1mZ", "http://spika.chat/api/v2/sticker/yiDcVZrfH6mUVVlfCL2nJPkAoUheh1LF", "http://spika.chat/api/v2/sticker/DTk0ycQ5EKcIQ83qStvv4JDH93QKWLXh", "http://spika.chat/api/v2/sticker/IlMK9YNUylIM0D7VCJ6U5OAU7YAyK2BN", "http://spika.chat/api/v2/sticker/FJEAUec4P2ErioLeGMiSoFdo9msEj4Cq", "http://spika.chat/api/v2/sticker/DP7AC3wlYikW4mf6BaYzcbnj73lAmEyJ", "http://spika.chat/api/v2/sticker/HMFZGvwTifXnbwAMzZr6OTLsIturtizb", "http://spika.chat/api/v2/sticker/NKhPyEtCippxEErcThCAq0dXs5mgLabx", "http://spika.chat/api/v2/sticker/yoU47dbtbvXnFN9aGJp4mhTUBq3Wfsa3", "http://spika.chat/api/v2/sticker/6nZan6L63VPj3rqlo3TOHanX0uTSgkXE", "http://spika.chat/api/v2/sticker/NjgrJjHNky1KIkoqFr3UtAsJHWoG8E7l", "http://spika.chat/api/v2/sticker/x98g96TtCVJ6tCb8VOr7ovGWEQV4cez6", "http://spika.chat/api/v2/sticker/Mx36Avf5eZkmoIyBqv8l1ujjGk3kH5mB", "http://spika.chat/api/v2/sticker/GHiKRk19OWi2SX5MSpGsXFu6Dwj2YJZm", "http://spika.chat/api/v2/sticker/LSXWdrgiOPbikxEf2yQHYPZNajO7hzSH", "http://spika.chat/api/v2/sticker/mAbd7Fy3srCpnMSw4snkmwHyHDz0EbHF", "http://spika.chat/api/v2/sticker/jbWEGEwJhf7mTVKWBOt0L8GYPKTqS6SV", "http://spika.chat/api/v2/sticker/ewDSmN7EzkfpENnK530hVetFEAGMTcCC", "http://spika.chat/api/v2/sticker/5lSUTrxEDb49WOpWGUWAqeoUuMxNSxg4", "http://spika.chat/api/v2/sticker/yPdjGOCx6GbhrXZj7nx1HUVxO1nfM6j4", "http://spika.chat/api/v2/sticker/sexefZh40iTCKj9KBTMSNrTprpD06FpW", "http://spika.chat/api/v2/sticker/kAOSx8TOKL8g9WwQcnrGTbV8rU699FYV", "http://spika.chat/api/v2/sticker/Om1q7ASQJX4RAjK5jFyXOPeLlViY3xMT"]
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
                            
                            return Config.stickerBaseURL + row.smallPic
                             
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
