var request = require('supertest');
var app = require('../mainTest');

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
    
    var token1 = "";
        	
before(function(done){
    
    // need to wait a bit to everyting be ready
    setTimeout(function(){

        request(app)
            .post('/spika/v1/user/login')
            .send(params1)
    		.expect('Content-Type', /json/)
    		.expect(200) 
            .end(function (err, res) {
    
    		if (err) {
    			throw err;
    		}
            
    
            request(app)
                .post('/spika/v1/user/login')
                .send(params2)
                .expect('Content-Type', /json/)
                .expect(200) 
                .end(function (err, res) {
                
                if (err) {
                	throw err;
                }
                
                
                done();
            
            });
        
        });   
    
        
    }, 100);

});