var should = require('should');
var request = require('supertest');
var app = require('../mainTest');

describe('WEB', function () {

    beforeEach(function () {

    });

    describe('/message/list/latest GET', function () {

        it('Get message list works', function (done) {
    	
            request(app)
                .get('/spika/v1/message/latest/test/0')
        		.expect(200) 
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}
                        
                res.body.should.have.property('success');
                
                done();
            
            });   
            
        });

                              
    });
    
});