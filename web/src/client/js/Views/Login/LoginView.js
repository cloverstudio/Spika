var Backbone = require('backbone');
var template = require('./Login.hbs');
var _ = require('lodash');
var socket = require('socket.io-client');
var Cookies = require('js-cookie');

var U = require('../../libs/utils.js');
var Const = require('../../consts.js');

var LoginView = Backbone.View.extend({

    el : null,

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

        var self = this;
        var loginInfo = Cookies.getJSON(Const.COOKIE_KEY_LOGININFO);

        var defaultValues = {
            name:'',
            avatarURL:'',
            roomID:'',
            id:''
        }

        if(!_.isUndefined(loginInfo)){
            defaultValues = loginInfo;
        }

        SS('#input-name').val(defaultValues.name);
        SS('#input-avatarurl').val(defaultValues.avatarURL);
        SS('#input-room').val(defaultValues.roomID);
        SS('#input-id').val(defaultValues.id);
        
        SS('#btn-enter').on('click',function(){
                             
            if(self.validate()){

                var name = SS('#input-name').val();
                var avatar = SS('#input-avatarurl').val();
                var room = SS('#input-room').val();
                var id = SS('#input-id').val();

                app.login(id,name,avatar,room,function(){
                    
                    U.goPage('main');
                                        
                });

            }
            
        });
        
    },

    validate: function(){

        var name = SS('#input-name').val();
        var avatar = SS('#input-avatarurl').val();
        var room = SS('#input-room').val();
        var id = SS('#input-id').val();

        var result = true;

        SS('.form-group').removeClass('has-error');
        SS('.label-error').text("");

        if(_.isEmpty(name)){

            result = false;

            SS('#input-name').parent().find('.label-error').text("Please input name");
            SS('#input-name').parent().addClass('has-error');
            
        } else {
            SS('#input-name').parent().addClass('has-success');
        }

        if(_.isEmpty(room)){

            result = false;

            SS('#input-room').parent().find('.label-error').text("Please room name");
            SS('#input-room').parent().addClass('has-error');
            
        } else {
            $('#input-room').parent().addClass('has-success');
        }

        if(_.isEmpty(id)){

            result = false;

            SS('#input-id').parent().find('.label-error').text("Please input user id");
            SS('#input-id').parent().addClass('has-error');
            
        } else {
            SS('#input-id').parent().addClass('has-success');
        }

        return result;

    }

});

module.exports = LoginView;
