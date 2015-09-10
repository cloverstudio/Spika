// import libraries
window.$ = window.jQuery = require('jquery');
var Backbone = require('backbone');
Backbone.$ = $;
var Cookies = require('js-cookie');
require('jquery-colorbox');

var _ = require('lodash');
var bootstrap = require('bootstrap-sass');

require('./libs/global.js');
var U = require('./libs/utils.js');
var JSON = require('JSON2');
var LoginUserManager = require('./libs/loginUserManager.js');
var socketIOManager = require('./libs/socketIOManager');
var Config = require('./init.js');
var Const = require('./consts.js');
var UrlGenerator = require('./libs/urlGenerator');
var LocalizationManager = require('./libs/localizationManager.js');
var WebAPIManager = require('./libs/webAPIManager');
var ErrorDialog = require('./Views/Modals/ErrorDialog/ErrorDialog');
var ProcessingDialog = require('./Views/Modals/ProcessingDialog/ProcessingDialog');
var ViewHelpers = require('./libs/viewHelpers.js');
var Settings = require('./libs/Settings');

ViewHelpers.attach();

// app instance (global)
window.app = {
    
    login:function(userId,name,avatarURL,roomID,callBack){
        
        var self = this;
        
        socketIOManager.init();
        LocalizationManager.init(Settings.options.lang);
        
        ProcessingDialog.show();
                
        WebAPIManager.post(
        
            UrlGenerator.userLogin(), 
            
            {userID: userId, name: name, avatarURL: avatarURL, roomID: roomID},
            
            // success
            function(data){
                
                socketIOManager.emit('login',{
                    name : name,
                    avatar : avatarURL,
                    roomID : roomID,
                    userID: userId
                });
    
                LoginUserManager.setLoginUser(name, avatarURL, roomID, userId, data.token);
    
                var loginInfo = {
                    id:userId,
                    name:name,
                    avatarURL:avatarURL,
                    roomID:roomID
                }
                
                Cookies.set(Const.COOKIE_KEY_LOGININFO, loginInfo);
                                            
                if(!_.isUndefined(callBack)){
                    callBack();
                }
              
            },
            
            //error
            function(error){
                ProcessingDialog.hide();
            }
            
        );

    }
    
}

// add some dummy functions to pass IE8
U.ie8Fix();

// disable ajax cache
$.ajaxSetup({
    cache: false
});

// load default language 
LocalizationManager.init(Config.lang);


// setting up router
var AppRouter = Backbone.Router.extend({
    routes: {
        "login": "loginRoute",
        "colors": "colorsRoute",
        "main": "mainRoute",
        "*actions": "defaultRoute"
    }
});

// Initiate the router
var app_router = new AppRouter;

app_router.on('route:defaultRoute', function(actions) {

    var queryInfo = U.getURLQuery();

    Settings.options = Config;
        
    if(!_.isEmpty(queryInfo.params)){
        
        var bootOptions = JSON.parse(queryInfo.params);
        var user = bootOptions.user;
        
        Settings.options = _.merge(Config,bootOptions.config);
             
        if(!_.isEmpty(user) &&
            !_.isEmpty(user.id) &&
            !_.isEmpty(user.name) &&
            !_.isEmpty(user.roomID)){
            
            app.login(
            
                user.id,
                user.name,
                user.avatarURL,
                user.roomID,
                
                function(){
                                                                      
                    var MainView = require('./Views/Main/MainView.js');
                                        
                    var view = new MainView({
                        'el': Config.defaultContainer
                    });
        
                }
            );
                    
        }

            
    } else {
        U.goPage('login');    
    }

});

app_router.on('route:loginRoute', function(actions) {
    
    if(_.isEmpty(Settings.options))
        Settings.options = Config;

    var LoginView = require('./Views/Login/LoginView.js');
        
    var view = new LoginView({
        'el': Config.defaultContainer
    });
    
});

app_router.on('route:colorsRoute', function(actions) {
        
    var ColorsView = require('./Views/Colors/ColorsView.js');
        
    var view = new ColorsView({
        'el': Config.defaultContainer
    });
    
});

app_router.on('route:mainRoute', function(actions) {

    if(_.isEmpty(Settings.options))
        Settings.options = Config;

    if(_.isNull(LoginUserManager.user)){
        
        var loginInfo = Cookies.getJSON(Const.COOKIE_KEY_LOGININFO);
        
        if(_.isUndefined(loginInfo))
            U.goPage('login');
        
        else {
            
            app.login(
                loginInfo.id,
                loginInfo.name,
                loginInfo.avatarURL,
                loginInfo.roomID,
                function(){
                                        
                    var MainView = require('./Views/Main/MainView.js');
                                        
                    var view = new MainView({
                        'el': Config.defaultContainer
                    });
        
                }
            );
            
        }
        
    } else {
        
        var MainView = require('./Views/Main/MainView.js');
                
        var view = new MainView({
            'el': Config.defaultContainer
        });
        
    }

    
});

$(function() {
    
    // Start Backbone history a necessary step for bookmarkable URL's
    Backbone.history.start();

});



