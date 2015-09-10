var Backbone = require('backbone');
var _ = require('lodash');
var socket = require('socket.io-client');

var U = require('../../../libs/utils.js');
var LoginUserManager = require('../../../libs/loginUserManager.js');
var socketIOManager = require('../../../libs/socketIOManager');
var UrlGenerator = require('../../../libs/urlGenerator');
var WebAPIManager = require('../../../libs/webAPIManager');
var CONST = require('../../../consts');
var ProcessingDialog = require('../../Modals/ProcessingDialog/ProcessingDialog');
var User = require('../../../Models/user.js');
var LocalizationManager = require('../../../libs/localizationManager');

var template = require('./MessageDetail.hbs');

var MessageDetailView = Backbone.View.extend({

    el : null,
    currentMessage:null,
    initialize: function(options) {
        this.el = options.el;
        this.render();
    }, 

    render: function() {
        this.onLoad();
        return this;
    },

    onLoad: function(){
        
        var self = this;
        
        Backbone.on(CONST.EVENT_MESSAGE_SELECTED,function(obj){
            
            self.currentMessage = obj;
            
            $(self.el).html(template(obj.toObject()));
            SS(self.el).addClass('on');
            
            if(obj.get('userID') == LoginUserManager.user.get('id')){
                SS('#btn-deletemessage').show();
            }else{
                SS('#btn-deletemessage').hide();
            }
            
            if(obj.get('deleted') == 0){
                SS('.deltedalert').hide();
            }else{
                SS('.deltedalert').show();
                SS('#btn-deletemessage').hide();        
            }

            SS('#btn-closeinfoview').unbind().on('click',function(){
                
                SS(self.el).removeClass('on');
                
            });
            
            SS('#btn-deletemessage').unbind().on('click',function(){
                
                if(confirm(LocalizationManager.localize('Are you sure to delete this message ?'))){
                    
                    socketIOManager.emit('deleteMessage',{
                        messageID: self.currentMessage.get('id'),
                        userID: LoginUserManager.user.get('id')
                    });
                    
                    SS(self.el).removeClass('on');
                                    
                }
                
            });
            
                        
        });

        _.debounce(function(){
        
            self.adjustSize();
            
        },100)();
                
        $( window ).resize(function() {
            
            self.adjustSize();
            
        });
        
    },

    adjustSize: function(){
        
        
        
    }
    
});

module.exports = MessageDetailView;
