var Backbone = require('backbone');
var _ = require('lodash');
var socket = require('socket.io-client');

var U = require('../../../../libs/utils.js');
var LoginUserManager = require('../../../../libs/loginUserManager.js');
var socketIOManager = require('../../../../libs/socketIOManager');
var UrlGenerator = require('../../../../libs/urlGenerator');
var WebAPIManager = require('../../../../libs/webAPIManager');
var CONST = require('../../../../consts');
var ProcessingDialog = require('../../../Modals/ProcessingDialog/ProcessingDialog');
var User = require('../../../../Models/user.js');
var LocalizationManager = require('../../../../libs/localizationManager');

var template = require('./StickerPanel.hbs');
var templateContents = require('./StickerContent.hbs');

var EmoticonPanelView = Backbone.View.extend({

    el : null,
    currentMessage:null,
    dataList : [],
    callBack : null,
    ignoreClose : false,
    initialize: function(options,callBack) {
    
    	if($("#sticker-panel")[0])
    		return;
    		
		this.callBack = callBack;
        this.el = options.el;
        this.render();
    }, 

    render: function() {
        
        $(this.el).append(template());
        
        this.onLoad();
        
        return this;
    },

    onLoad: function(){
        
        var self = this;

        
        WebAPIManager.get(
            
            UrlGenerator.stickerList(), 
            
            // success
            function(data){
                
                self.dataList = data.stickers;

		        Backbone.on(CONST.EVENT_ON_GLOBAL_CLICK,function(){
		            
                    if(self.ignoreClose){
                        self.ignoreClose = false;
                        return;
                    }
                        
                        
		            self.hide();
		                 
		        });
		        
		        console.log("data.stickers",data.stickers);
		        
		        $('#sticker-panel').html(templateContents({
		        	list:data.stickers
		        }));
        
				self.afterRender();
            },
            
            //error
            function(error){
                
                console.log(error);
            }
            
        );
        
        
    },
    hide:function(){

	    $("#sticker-panel").remove();
	    Backbone.off(CONST.EVENT_ON_GLOBAL_CLICK);
	    
	    if(this.callBack)
	    	this.callBack(null);
            
    },
    
    afterRender: function(){
	    
        var self = this;
        
	    var tabWidth = $('#sticker-tabs li.tabs').outerWidth();
	    var totalWidth = tabWidth * this.dataList.length;
	    
	    $('#sticker-tabs').width(totalWidth);
	    $('#sticker-picture-container ul').css('display','none');
	    $('#sticker-picture-container #pictures-0').css('display','block');
	    $('#sticker-tabs #tab-0').addClass('selected');
        
        $('#sticker-tabs .tabs').on('click',function(){
            
            self.ignoreClose = true;
            var listNumber = $(this).attr('key');
            
            $('#sticker-tabs .tabs').removeClass('selected');
            $(this).addClass('selected');
            
            $('#sticker-picture-container ul').css('display','none');
            $('#sticker-picture-container #pictures-' + listNumber).css('display','block');
            
        });

        $('#sticker-tabs-prev').on('click',function(){
            
            self.ignoreClose = true;
            var scroll = $('#sticker-tabs-parent').scrollLeft();
            var width = $('#sticker-tabs-parent').outerWidth();
            
            $('#sticker-tabs-parent').scrollLeft(scroll - width);
            
            
        });
 
        $('#sticker-tabs-next').on('click',function(){
            
            self.ignoreClose = true;
            
            var scroll = $('#sticker-tabs-parent').scrollLeft();
            var width = $('#sticker-tabs-parent').outerWidth();
            
            $('#sticker-tabs-parent').scrollLeft(scroll + width);
            
        });
        
        $('#sticker-picture-container .picture').on('click',function(){
            
            self.ignoreClose = true;
            self.sendMessage($(this).attr('url'));
            
        });
        
        
    },
    
    sendMessage: function(url){
        
        this.hide();
        
        socketIOManager.emit('sendMessage',{
            message: url,
            roomID: LoginUserManager.roomID,
            userID: LoginUserManager.user.get('id'),
            type:CONST.MESSAGE_TYPE_STICKER,
            attributes: {
                client: "web"
            }
        });
            
    }
    
});

module.exports = EmoticonPanelView;
