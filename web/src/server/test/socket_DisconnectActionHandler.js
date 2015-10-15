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

    describe('disconnect', function () {
        
        it('Disconnect works', function (done) {
            
            var responseCount = 0;
            
        	var params1 = {
                name : "test",
                avatarURL : "test",
                roomID : "test",
                userID : "test"
        	};

        	var params2 = {
                name : "test",
                avatarURL : "test",
                roomID : "test",
                userID : "test2"
        	};
        	        	
            var client1 = io.connect(socketURL, connectOptions);
            var client2 = null;
            
            client1.on('connect', function(data){
            
                client1.emit('login',params1);
                
            });

            client1.on('newUser', function(param){ 
                
                if(client2 == null){
                    
                    client2 = io.connect(socketURL, connectOptions);

                    client2.on('connect', function(data){
                    
                        client2.emit('login',params2);   
                        
                    });
                    
                    client2.on('newUser', function(param){

                        client1.disconnect();
                        
                    });

                    client2.on('userLeft', function(param){ 

                        param.should.have.property('userID');
                        client2.disconnect();
                        
                        done();
                        
        
                    });

                }
                
            });

        });

        
    });

});