var should = require('should');
var request = require('supertest');
var app = require('../mainTest');

describe('routes', function () {

    var req, res;

    beforeEach(function () {
        req = {};
        res = {
            redirect: function () { },
            render : function () { }
        };
    });

    describe('/login POST', function () {

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
                
                res.body.should.have.property('success');
                res.body.success.should.equal(1);
                res.body.should.have.property('result');
                res.body.result.should.have.property('user');
                res.body.result.user.should.have.property('token');
                
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
                
                res.body.should.have.property('success');
                res.body.success.should.equal(1);
                res.body.should.have.property('result');
                res.body.result.should.have.property('user');
                res.body.result.user.should.have.property('token');
                
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
                
                res.body.should.have.property('success');
                res.body.success.should.equal(0);
                
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
                
                res.body.should.have.property('success');
                res.body.success.should.equal(0);
                
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
                
                res.body.should.have.property('success');
                res.body.success.should.equal(0);
                
                done();
            
            });   
            
        });
              
              
    });
    
});