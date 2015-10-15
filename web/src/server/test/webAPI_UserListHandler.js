var should = require('should');
var request = require('supertest');
var app = require('../mainTest');

describe('WEB', function () {

    describe('/user/list/:roomid GET', function () {

        it('Get user list works.', function (done) {
    	
            request(app)
                .get('/spika/v1/user/list/test22')
        		.expect(200) 
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}
                
                res.body.should.have.property('success');
                
                done();
            
            });   
            
        });


        it('Fails when room id is not provided.', function (done) {
    	
            request(app)
                .get('/spika/v1/user/list/')
        		.expect(404) 
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}
                
                done();
            
            });   
            
        });
                      
    });
    
});