var should = require('should'); 
var assert = require('assert'); 
var mongoose = require('mongoose');
var app = require('../mainTest');
var io = require('socket.io-client');

describe('SOCKET', function () {

    var socketURL = "http://localhost:8181/spika";
    var connectOptions ={
        transports: ['websocket'],
        'force new connection': true
    };

    describe('sendMessage', function () {
        
        it('Send message works.', function (done) {
            
            var counter = 0;
            
        	var loginParams = {
                name : "test",
                avatarURL : "test",
                roomID : "test",
                userID : "test",
        	};

        	var sendTypingParams = {
                roomID : "test",
                userID : "test",
                type : 1
        	};

        	var messageID = '';
        	
            var client1 = io.connect(socketURL, connectOptions);
            
            client1.on('connect', function(data){
  
                client1.emit('login',loginParams);
                
            });

            client1.on('newUser', function(param){ 
                
                client1.emit('sendTyping',sendTypingParams);

            });

            client1.on('sendTyping', function(param){ 
                
                client1.disconnect();
                done();
                
            });
            
            
        });

        
    });

});