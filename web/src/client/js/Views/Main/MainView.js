var Backbone = require('backbone');
var _ = require('lodash');

var LoginUserManager = require('../../libs/loginUserManager.js');
var ErrorDialog = require('../Modals/ErrorDialog/ErrorDialog');
var U = require('../../libs/utils.js');
var template = require('./Main.hbs');
var CONST = require('../../consts');
var Settings = require('../../libs/Settings');

/**
 * Main View
 * 
 * @class
 */
var MainView = Backbone.View.extend({
     
    el : null,
    
    /**
     * Initialize MainView
     * 
     * @method
     * @name MainView.initialize
     * @param options
    */
    initialize: function(options) {
        this.el = options.el;
        this.render();
    },

    /**
     * Render MainView
     * 
     * @method
     * @name MainView.render
    */
    render: function() {
                
        $(this.el).html(template());
        this.onLoad();
        return this;

    },
    
    onLoad: function(){

        var self = this;
        
        var SidebarView = require('./Sidebar/SidebarView.js'); 
        var view = new SidebarView({
            'el': "#sidebar-content"
        });

        var MessagingView = require('./Messaging/MessagingView.js'); 
        var view = new MessagingView({
            'el': "#messaging-content"
        });

        var MessageDetailView = require('./MessageDetail/MessageDetailView.js'); 
        var view = new MessageDetailView({
            'el': "#message-info"
        });

 
        if(Settings.options.showSidebar == false){
            
           SS('#sidebar').css('display','none'); 
           SS('#messaging').attr('class','col-md-12 col-sm-12 col-xs-12'); 
            
        }        
        
        if(Settings.options.showTitlebar == false){
            
           SS('#titlebar').css('display','none'); 
                       
        }        

        this.adjustSize();
        
        $( window ).resize(function() {
            
            self.adjustSize();
            
        });
        
    },
    
    adjustSize: function(){
        
        SS('#message-info').height($(window).height() - SS('#message-info').position().top - SS('#text-message-box-container').height());
        SS('#sidebar').height($(window).height());
        SS('#messaging').height($(window).height());
        
    }

});

module.exports = MainView;
