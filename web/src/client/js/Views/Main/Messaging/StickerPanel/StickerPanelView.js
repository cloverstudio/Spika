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

var EmoticonPanelView = Backbone.View.extend({

    el : null,
    
    currentMessage:null,
    
    initialize: function(options) {
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
                
                console.log(data);

            },
            
            //error
            function(error){
                
            }
            
        );
        
        
    }
    
});

module.exports = EmoticonPanelView;
