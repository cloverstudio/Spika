var should = require('should');
var request = require('supertest');
var app = require('../mainTest');

describe('WEB', function () {
  
    describe('/user/login POST', function () {

        it('Login passes when all parameters is given.', function (done) {
                   
        	var body = {
                name : "test",
                avatarURL : "test",
                roomID : "test",
                userID : "test",
        	};
    	
            request(app)
                .post('/spika/v1/user/login')
                .send(body)
        		.expect('Content-Type', /json/)
        		.expect(200) 
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}
                
                
                res.body.should.have.property('code');
                res.body.code.should.equal(1);
                res.body.should.have.property('data');
                res.body.data.should.have.property('user');
                res.body.data.user.should.have.property('token');
                
                token1 = res.body.data.token;
                
                done();
            
            });   
            
        });

        it('Login passes when avatarURL is not given.', function (done) {
                   
        	var body = {
                name : "test",
                avatarURL : "",
                roomID : "test",
                userID : "test",
        	};
    	
            request(app)
                .post('/spika/v1/user/login')
                .send(body)
        		.expect('Content-Type', /json/)
        		.expect(200) 
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}
                
                res.body.should.have.property('code');
                res.body.code.should.equal(1);
                res.body.should.have.property('data');
                res.body.data.should.have.property('user');
                res.body.data.user.should.have.property('token');
                
                token1 = res.body.data.token;
                
                done();
            
            });   
            
        });

        it('Login failes when userID is not given.', function (done) {

        	var body = {
                name : "test",
                avatarURL : "test",
                roomID : "test",
                userID : "",
        	};
    	
            request(app)
                .post('/spika/v1/user/login')
                .send(body)
        		.expect('Content-Type', /json/)
        		.expect(200) 
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}
                
                res.body.should.have.property('code');
                res.body.code.should.equal(1000003);
                
                done();
            
            });   
            
        });
        
        it('Login failes when roomID is not given.', function (done) {

        	var body = {
                name : "test",
                avatarURL : "test",
                roomID : "",
                userID : "test",
        	};
    	
            request(app)
                .post('/spika/v1/user/login')
                .send(body)
        		.expect('Content-Type', /json/)
        		.expect(200) 
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}
                
                res.body.should.have.property('code');
                res.body.code.should.equal(1000002);
                
                done();
            
            });   
            
        });
        
        it('Login failes when name is not given.', function (done) {

        	var body = {
                name : "",
                avatarURL : "test",
                roomID : "test",
                userID : "test",
        	};
    	
            request(app)
                .post('/spika/v1/user/login')
                .send(body)
        		.expect('Content-Type', /json/)
        		.expect(200) 
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}
                
                res.body.should.have.property('code');
                res.body.code.should.equal(1000001);
                
                done();
            
            });   
            
        });
        
              
    });
    
});