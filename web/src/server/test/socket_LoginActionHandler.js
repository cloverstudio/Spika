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

    describe('login', function () {
        
        it('Login passes with all parameters provided.', function (done) {
            
            var responseCount = 0;
            
        	var params = {
                name : "test",
                avatarURL : "test",
                roomID : "test",
                userID : "test",
        	};
        	
            var client1 = io.connect(socketURL, connectOptions);
            
            client1.on('connect', function(data){
  
                client1.emit('login',params);
                
            });

            client1.on('newUser', function(param){ 
            
                responseCount++;
                
                if(responseCount == 2)
                    done();

            });

            client1.on('newMessage', function(param){ 
            
                responseCount++;
                
                if(responseCount == 2){
                    done();
                    client1.disconnect();
                }

            });
                        

        });

        it('Login failed if userID is not provided', function (done) {
            
            var responseCount = 0;
            
        	var params = {
                name : "test",
                avatarURL : "test",
                roomID : "test",
                userID : "",
        	};
        	
            var client1 = io.connect(socketURL, connectOptions);
            
            client1.on('connect', function(data){
                
                client1.emit('login',params);
                
            });

            client1.on('newUser', function(param){ 
                
                throw (new Error("failed"));
                
            });

            client1.on('newMessage', function(param){ 
            
                throw (new Error("failed"));

            });
            
            client1.on('socketerror', function(data){
                
                data.code.should.equal(1000025);
                done();
                client1.disconnect();
                
            });

        });

        it('Login failed if roomID is not provided', function (done) {
            
            var responseCount = 0;
            
        	var params = {
                name : "test",
                avatarURL : "test",
                roomID : "",
                userID : "test",
        	};
        	
            var client1 = io.connect(socketURL, connectOptions);
            
            client1.on('connect', function(data){
                
                client1.emit('login',params);
                
            });

            client1.on('newUser', function(param){ 
                
                throw (new Error("failed"));
                
            });

            client1.on('newMessage', function(param){ 
            
                throw (new Error("failed"));

            });
            
            client1.on('socketerror', function(data){
                
                data.code.should.equal(1000026);
                done();
                client1.disconnect();
                
            });

        });

        
    });

});