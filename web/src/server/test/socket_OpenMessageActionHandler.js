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

    describe('openMessage', function () {
        
        it('Open message works.', function (done) {
            
            var counter = 0;
            
        	var loginParams = {
                name : "test",
                avatarURL : "test",
                roomID : "test",
                userID : "test",
        	};

        	var sendMessageParams = {
                roomID : "test",
                userID : "test",
                type : 1,
                message : "test"
        	};

        	var messageID = '';
        	
            var client1 = io.connect(socketURL, connectOptions);
            
            client1.on('connect', function(data){
  
                client1.emit('login',loginParams);
                
            });

            client1.on('newUser', function(param){ 
                
                client1.emit('sendMessage',sendMessageParams);

            });

            client1.on('newMessage', function(param){ 
                
                counter++;
                
                if(counter == 2){
                    
                    param.should.have.property('_id');
                    
                    var messageID = param['_id'];
                    
                	var openMessageParams = {
                        userID : "test",
                        messageIDs : [messageID]
                	};

                    client1.emit('openMessage',openMessageParams);
                    
                }
                
            });

            client1.on('messageUpdated', function(param){ 
                
                client1.disconnect();
                done();

            });
            
        });

        
    });

});