// Load modules
var socket = require('socket.io');
var express = require('express');
var http = require('http');
var init = require('./init.js');

// initialization
var app = express();
var server = http.createServer(app);
var port = init.port;
var io = socket.listen(server);

// Start Spika as stand alone server
var spika = require('./index.js');

var SpikaServer = new spika(app,io,init);
    
server.listen(init.port, function(){
    console.log('Server listening on port ' + init.port + '!');
});