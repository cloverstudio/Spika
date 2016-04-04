var Backbone = require('backbone');
var socket = require('socket.io-client');
var _ = require('lodash');
var CONST = require('../../../consts');
var template = require('./Messaging.hbs');
var U = require('../../../libs/utils.js');
var LoginUserManager = require('../../../libs/loginUserManager.js');
var socketIOManager = require('../../../libs/socketIOManager');
var WebAPIManager = require('../../../libs/webAPIManager');
var UrlGenerator = require('../../../libs/urlGenerator');
var FileUploader = require('./FileUploader');
var CellGenerator = require('./CellGenerator');
var Message = require('../../../Models/message.js');
var browser = require('bowser');
var Settings = require('../../../libs/Settings');
var stickerPanelView = require('./StickerPanel/StickerPanelView');

var MessagingView = Backbone.View.extend({

    el : null,
    initialTBHeight : 0,
    initialTBContainerHeight : 0,
    messages: null,
    isLoading: false,
    pagingReachesToLast: false,
    fileUplaoder: null,
    cellGenerator: null,
    lastTextLength: 0,
    initialize: function(options) {
        this.el = options.el;
        this.render();
    },

    render: function() {
        $(this.el).html(template());
        this.onLoad();
        return this;

    },
    onLoad: function(){
        
        // disable scroll in container
        $(Settings.options.defaultContainer).css('overflow-x','hidden');
        $(Settings.options.defaultContainer).css('overflow-y','hidden');
        
        var self = this;
        
        this.fileUplaoder = new FileUploader({
            view:this
        });
         
        this.cellGenerator = new CellGenerator({
            view:this
        });
         
        this.messages = Message.collectionByResult([]);
        
        this.initialTBHeight = SS( "#text-message-box" ).height();
        this.initialTBContainerHeight = SS( "#text-message-box-container" ).height();
        
        // Room name
        $("#room_name").html(LoginUserManager.roomID);
                
        Backbone.on(CONST.EVENT_ON_MESSAGE,function(obj){
            self.newMessage(obj);            
        });

        Backbone.on(CONST.EVENT_ON_MESSAGE_UPDATED,function(obj){

            _.forEach(obj,function(updatedMessage){
                
                var messageModel = Message.modelByResult(updatedMessage);
                messageModel.set('status',CONST.MEASSAGE_STATUS_SENT);
                
                self.updateMessage(messageModel);
                
            });
            
            if(obj.length > 0)
                self.afterRender();
                        
        });
        
        Backbone.on(CONST.EVENT_ON_TYPING,function(obj){
            
            if(obj.userID == LoginUserManager.user.get('id'))
                return;
                
            if(obj.type == CONST.TYPING_ON){
                
                self.addTyping(obj);
                
            } else {
                
                self.removeTyping(obj.userID);

            }
            
            self.afterRender();

        });


        SS( "#text-message-box" ).keypress(function(e) {
            
            var keycode = (e.keyCode ? e.keyCode : e.which);
            var shifted = e.shiftKey;
            if(keycode == 13){
                
                
                if(shifted){
                    
                    self.increaseTBHeight();
                    
                }else{

                    // send
                    e.preventDefault();
                    self.sendTextMessage(); 
                    self.resetTBHeight();
                }
                
            }
                        
        });
        
        SS( "#text-message-box" ).on('change keyup paste',function(){
            
            var length = $(this).val().length;
            
            if(self.lastTextLength == 0 && length > 0){
                socketIOManager.emit('sendTyping',{
                    roomID: LoginUserManager.roomID,
                    userID: LoginUserManager.user.get('id'),
                    type:CONST.TYPING_ON
                });
            }
            
            if(self.lastTextLength > 0 && length == 0){
                socketIOManager.emit('sendTyping',{
                    roomID: LoginUserManager.roomID,
                    userID: LoginUserManager.user.get('id'),
                    type:CONST.TYPING_OFF
                });
                
            }
            
            self.lastTextLength = length;
 
        });
        
        // handle paging
        SS( "#messages" ).scroll(function() {
            
            var position = $(this).scrollTop();
            
            if(position == 0){
                
                self.loadNextMessage();
                
            }
                        
        });
        
        SS('#btn-emoticons').on('click',function(){

            var view = new stickerPanelView({
                'el': "body"
            },function(selectedSticker){
	            
	            if(selectedSticker){
		            
	            }
	            
	            view = null;
	            
	            
            });

        });

        SS('#btn-fileupload').on('click',function(){
            
            self.fileUplaoder.handleClick();
            
        });
        
        SS('#file-input').on('change',function(event){
                        
            self.fileUplaoder.startUploadingFile(event);

        });
        
        _.debounce(function(){
            self.adjustSize();
        },100)();
                
        $( window ).resize(function() {
            
            self.adjustSize();
            
        });
        
        // loading past messages
        this.loadNextMessage();
        
    },
    
    loadNextMessage: function(){
        
        if(this.isLoading){
            U.l("stop its loading");
            return;
        }
        
        if(this.pagingReachesToLast){
            U.l("stop its reaches to the last");
            return;
        }
            
        // get last message
        var lastMessage = this.messages.at(0);

        var lastMessageId = '';
        
        if(_.isUndefined(lastMessage)){
            lastMessageId = 0;
        }else{
            lastMessageId = lastMessage.get('id');
        }
        
        var self = this;      

        this.isLoading = true;
                
        WebAPIManager.get(
            
            UrlGenerator.messageList(LoginUserManager.roomID,lastMessageId), 
            
            // success
            function(data){
                
                var loadedMessageModels = [];
                
                var html = '';
                
                data = data.messages;
                
                if(data.length < CONST.PAGING_ROW){
                    
                    self.pagingReachesToLast = true;
                    
                }
                                
                _.forEach(data,function(message,index){
                                        
                    var messageModel = Message.modelByResult(message);
                    messageModel.set('status',CONST.MEASSAGE_STATUS_SENT);

                    self.messages.add(messageModel);
                    
                    var newHtml = self.cellGenerator.generate(messageModel);
                                                      
                    // convert order here
                    html = newHtml + html;
                    
                    loadedMessageModels.push(messageModel);
                    
                });
                
                var currentHeight = SS('#messages')[0].scrollHeight;
                
                SS('#messages').prepend(html);
                
                _.debounce(function(){
                    var afterHeight = SS('#messages')[0].scrollHeight;
                    SS('#messages').scrollTop(afterHeight - currentHeight);
                },100)();
                
                self.afterRender();
                self.isLoading = false;
                
                self.sendOpenMessage(loadedMessageModels);

            },
            
            //error
            function(error){
                
            }
            
        );
        
    },
    
    adjustSize: function(){
        
        var statusBarHeight = SS('#additional-notification-container').height() + 10;
        
        if(browser.android){
            
            _.debounce(function(){
                
                // ToDo: find better way
                var messagingAreaHeight = $('body').height() - SS('#messaging .col-header').height();

                if(Settings.options.showTitlebar == false){
                    messagingAreaHeight = $('body').height();                               
                }        

                var textBoxHeight = SS('#text-message-box-container').height();
        
                SS('#additional-notification-container').css('bottom',textBoxHeight);
                SS('#messaging-content').height(messagingAreaHeight);
                SS('#messages').height(messagingAreaHeight - textBoxHeight - statusBarHeight);
                         
            },100)();
            
            return;
        }
        
        if(browser.ios){
            
            var messagingAreaHeight = $('body').height() - SS('#messaging .col-header').height();
            var textBoxHeight = SS('#text-message-box-container').height();

            if(Settings.options.showTitlebar == false){
                messagingAreaHeight = $('body').height();                               
            }     
                   
            SS('#additional-notification-container').css('bottom',textBoxHeight);
            SS('#messaging-content').height(messagingAreaHeight);
            SS('#messages').height(messagingAreaHeight - textBoxHeight - statusBarHeight);

        }
        
        
        var messagingAreaHeight = SS('#sidebar').height() - SS('#messaging .col-header').height();
        var textBoxHeight = SS('#text-message-box-container').height();

        if(Settings.options.showTitlebar == false){
            messagingAreaHeight = $('body').height();                               
        }     
                
        SS('#additional-notification-container').css('bottom',textBoxHeight);
        SS('#messaging-content').height(messagingAreaHeight);
        SS('#messages').height(messagingAreaHeight - textBoxHeight - statusBarHeight);
    },
    
    increaseTBHeight: function(){
        
        var currentHeight = SS( "#text-message-box-container" ).height();
        
        if(currentHeight < 100){
            SS( "#text-message-box-container" ).height(SS( "#text-message-box-container" ).height() + this.initialTBHeight);
            SS( "#text-message-box" ).height(SS( "#text-message-box" ).height() + this.initialTBHeight);
        }
        
    },

    resetTBHeight: function(){
        
        SS( "#text-message-box-container" ).height(this.initialTBContainerHeight);
        SS( "#text-message-box" ).height(this.initialTBHeight);
        
    },    
    
    sendTextMessage: function(){
        
        var message = SS( "#text-message-box" ).val();
        
        if(_.isEmpty(message))
            return;
            
        var tempID = '_' + U.getRandomString();
        
        // insert temp message
        

        var message = Message.modelByResult({
                    
            _id: tempID,
            localID: tempID,
            userID: LoginUserManager.user.get('id'),
            message: SS( "#text-message-box" ).val(),
            type: CONST.MESSAGE_TYPE_TEXT,
            created: U.now(),
            user: LoginUserManager.user.attributes
            
        })
        
        this.insertTempMessage(true,message);
        
        // Emit data to server
        socketIOManager.emit('sendMessage',{
            message: SS( "#text-message-box" ).val(),
            roomID: LoginUserManager.roomID,
            userID: LoginUserManager.user.get('id'),
            type:CONST.MESSAGE_TYPE_TEXT,
            localID: tempID,
            attributes: {
                client: "web"
            }
        });
        
        this.scrollToBottom();
        
        // Clear message_area
        $('#message_area').val('');
        
        SS( "#text-message-box" ).val('');
        
    },
    
    newMessage:function(obj){
                        
        var self = this;
        
        var newMessage = Message.modelByResult(obj);
        newMessage.set('status',CONST.MEASSAGE_STATUS_SENT);
                
        var tempMessageExists = false;
        
        if(newMessage.get('userID') == LoginUserManager.user.get('id')){
            
            // swap message
            this.messages.each(function(oldMessage,index){
                
                if(!_.isEmpty(oldMessage.get('localID')) && 
                    !_.isEmpty(newMessage.get('localID')) && 
                    oldMessage.get('localID') == newMessage.get('localID')){
                    
                    self.updateMessage(newMessage);
                    
                    tempMessageExists = true;
                    
                }

            });
            
        } else {
            
            self.sendOpenMessage([newMessage]);
                
        }

        
        if(!tempMessageExists){
        
            this.messages.add(newMessage);
            
            var newHtml = this.cellGenerator.generate(newMessage);
            
            SS('#messages').append(newHtml);
            
        }
        
        this.removeTyping(obj.userID);
        
        var isBottom = this.isScrollNearBottom();
        
        this.afterRender();

        this.scrollToBottom();
                      
                    
    },
    afterRender: function(){
        
        var self = this;

        // attach lightbox
        SS('.spika-thumb').colorbox({photo:true,fixed:true,width:'80%',height:'80%%¥',
            onOpen:function(evt){

                // call listener
                if(!_.isEmpty(window.parent.SpikaAdapter) &&
                
                    !_.isEmpty(window.parent.SpikaAdapter.listener)){
                    
                    var listener = window.parent.SpikaAdapter.listener;
                    
                    if(_.isFunction(listener.OnOpenFile)){
                        
                        var messageID = $(evt.el).attr('id');
                        var obj = self.messages.findMessageByID(messageID).attributes;
                        obj = _.clone(obj);
                        obj.user = obj.user.attributes;
                                            
                        if(!listener.OnOpenFile(obj)){
                            $.colorbox.close();
                        }
                    }
                }

            }
        });

        SS('.message-cell .message').css('cursor','pointer');
        SS('.message-cell .message').unbind().on('click',function(){

            // call listener
            if(!_.isEmpty(window.parent.SpikaAdapter) &&
            
                !_.isEmpty(window.parent.SpikaAdapter.listener)){
                
                var listener = window.parent.SpikaAdapter.listener;
                
                if(_.isFunction(listener.onOpenMessage)){
                    
                    var messageID = $(this).parent().attr('id');
                    var obj = self.messages.findMessageByID(messageID).attributes;
                    obj = _.clone(obj);
                    obj.user = obj.user.attributes;
                                        
                    if(listener.onOpenMessage(obj)){
                        self.openMessageInfoView(messageID);
                    }
                    
                    return;
                    
                }
            }

            self.openMessageInfoView($(this).parent().attr('id'));

        });

        SS('.message-cell .infoicon').css('cursor','pointer');
        SS('.message-cell .infoicon').unbind().on('click',function(){
            
            // call listener
            if(!_.isEmpty(window.parent.SpikaAdapter) &&
            
                !_.isEmpty(window.parent.SpikaAdapter.listener)){
                
                var listener = window.parent.SpikaAdapter.listener;
                
                if(_.isFunction(listener.onOpenMessage)){
                    
                    var messageID = $(this).parent().parent().attr('id');
                    
                    alert(messageID);
                    
                    var obj = self.messages.findMessageByID(messageID).attributes;
                    obj = _.clone(obj);
                    obj.user = obj.user.attributes;
                    
                    if(listener.onOpenMessage(obj)){
                        self.openMessageInfoView(messageID);
                    }  

                    return;
                }
            }
            
            self.openMessageInfoView($(this).parent().parent().attr('id'));
            
        });
                
        SS('.message-cell .file-container').css('cursor','pointer');
        SS('.message-cell .file-container').unbind().on('click',function(){
           
           self.openMessageInfoView($(this).parent().attr('id'));

        });

        // disable opening messsage detail view
        $(".message-cell .file-container a").click(function(e) { 
            e.stopPropagation(); 
            
            var link = $(this).attr('downloadlink');

            // call listener
            if(!_.isEmpty(window.parent.SpikaAdapter) &&
            
                !_.isEmpty(window.parent.SpikaAdapter.listener)){
                
                var listener = window.parent.SpikaAdapter.listener;
                
                if(_.isFunction(listener.onOpenMessage)){
                    
                    var messageID = $(this).attr('id');
                    var obj = self.messages.findMessageByID(messageID).attributes;
                    obj = _.clone(obj);
                    obj.user = obj.user.attributes;
                    
                    if(listener.OnOpenFile(obj)){
                        window.open(link);
                    }  

                    return;
                }
            }


            // open in new window
            window.open(link);
            
        });
                  
    },
    openMessageInfoView: function(messageID){
           
       // get message model by message id
       var message = this.messages.findMessageByID(messageID);
       
       Backbone.trigger(CONST.EVENT_MESSAGE_SELECTED,message);

    },
    resetMessages: function(){
        
        if(_.isNull(this.messages))
            return;
            
        var html = '';
        var self = this;
        
        this.messages.each(function(model,indes){
            
            var cell = self.cellGenerator.generate(model);
            
            html += cell;
            
        });
        
        // fix avatar positions
        SS('#messages').html(html);
        
        SS('.message-cell').each(function(){
           
           $(this).find('.avatar').css('line-height',($(this).find('.message').height() - 20) + "px"); 
           $(this).height($(this).find('.message').height()); 
           
        });
        
    },
    isScrollNearBottom: function(){
        
        var scrollPos =  SS('#messages').scrollTop();
        var scrollInnerHeight =  SS('#messages').innerHeight();
        var scrollHeight =  $('#messages')[0].scrollHeight;

        if(scrollPos + scrollInnerHeight >= scrollHeight - 50) {
            return true;
        }else
            return false;
        
    },
    scrollToBottom: function(){
        
        SS('#messages').scrollTop($('#messages')[0].scrollHeight);
        
    },
    insertTempMessage: function(isAppend,modelMessage){
        
        this.messages.add(modelMessage);

        var html = this.cellGenerator.generate(modelMessage);
        
        if(isAppend){
            SS('#messages').append(html);
        } else {
            SS('#messages').prepend(html);
        }
        
        this.afterRender();
        
    },
    removeTyping: function(userID){

        var userIDEscapted = encodeURIComponent(userID).replace("'","quote").replace("%","");
        var emlContainer = SS('#additional-notification-container');
        
        SS('#' + userIDEscapted + "-typing").remove();

       if(_.isEmpty(emlContainer.html())){
            emlContainer.height(0);
            emlContainer.fadeOut();
            this.adjustSize();
        }
                        
    },
    addTyping: function(obj){
        
        var emlContainer = SS('#additional-notification-container');
        var userIDEscapted = encodeURIComponent(obj.userID).replace("'","quote").replace("%","");


        var text = obj.user.name + " is typing...";
        var id = userIDEscapted + "-typing";
        
        console.log('addtyping',id);
        
        var html = '<span id="' + id + '">' + text + '</span>';
        
        if(_.isEmpty(emlContainer.html())){

             emlContainer.height(20);
             emlContainer.fadeIn();
             this.adjustSize();

            if(this.isScrollNearBottom())
                this.scrollToBottom();  

        }
        
        emlContainer.html( emlContainer.html() + html );

    },
    sendOpenMessage:function(messages){
        
        var IDs = [];
        
        _.forEach(messages,function(message){

            if(message.get('userID') != LoginUserManager.user.get('id')){
                
                IDs.push(message.get('id'));
                
            }
               
        });
        
        socketIOManager.emit('openMessage',{
            messageIDs: IDs,
            userID: LoginUserManager.user.get('id')
        });
            
    },
    updateMessage:function(newMessage){
       
        console.log(newMessage);
        
        if(_.isEmpty(newMessage))
            return;

        // get old message
        var oldMessage = this.messages.findMessageByID(newMessage.get('id'));
        
        if(_.isEmpty(oldMessage)){
            oldMessage = this.messages.findMessageByLocalID(newMessage.get('localID'));
        }

        if(_.isEmpty(oldMessage)){
            return;
        }

        this.messages.swap(oldMessage,newMessage)
        
        var newHtml = this.cellGenerator.generate(newMessage);
                                
        SS('#' + oldMessage.get('id')).replaceWith(newHtml);
        SS('#' + oldMessage.get('id')).attr('id',newMessage.get('id'));
        
    }
    
});

module.exports = MessagingView;
