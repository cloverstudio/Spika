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


var template = require('./Sidebar.hbs');

var SidebarView = Backbone.View.extend({

    el : null,
    usersCollection: null,
    userTemplate : null,
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

        this.userTemplate = require('./cellUser.hbs');
        
        this.refreshUsers();
        
        var self = this;

        // New user login
        Backbone.on(CONST.EVENT_ON_LOGIN_NOTIFY, function(obj){
            
            self.refreshUsers();
            
            /*
            var userData = {"avatarURL" : obj.avatar, "name" : obj.name};

            if(obj.room == LoginUserManager.roomID){
                $('#online').append(userTemplate(userData));
            }
            */

        });

        Backbone.on(CONST.EVENT_ON_LOGOUT_NOTIFY, function(obj){
            
            self.refreshUsers();
            
            /*
            var userData = {"avatarURL" : obj.avatar, "name" : obj.name};

            if(obj.room == LoginUserManager.roomID){
                $('#online').append(userTemplate(userData));
            }
            */

        });
        
        
        /*
        // Closing or refreshing window
        window.onbeforeunload = function() {

            socketIOManager.emit('logout', {
                id: LoginUserManager.user.get('id'),
                name: LoginUserManager.user.get('name'),
                room: LoginUserManager.roomID
            });

        };
        */

        _.debounce(function(){
            self.adjustSize();
        },100)();
                
        $( window ).resize(function() {
            
            self.adjustSize();
            
        });
        
    },

    adjustSize: function(){
        var userListAreaHeight = SS('#sidebar').height() - SS('#sidebar .col-header').height();
        SS('#online-users').height(userListAreaHeight);
    },
    
    refreshUsers:function(){
        
        var self = this;

        WebAPIManager.get(
            
            UrlGenerator.userList(LoginUserManager.roomID), 
                           
            // success
            function(data){
                
                self.usersCollection = User.collectionByResult(data);
                
                SS('#online-users').empty();
                                
                self.usersCollection.each(function(model,index){
                    //U.l(model.attributes);
                    SS('#online-users').append(self.userTemplate(model.attributes));
                });

                // hide processing which appears before login
                ProcessingDialog.hide();
                
            },
            
            //error
            function(error){
                
            }
            
        );
        
    }

});

module.exports = SidebarView;
