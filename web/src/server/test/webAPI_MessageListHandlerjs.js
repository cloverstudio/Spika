var should = require('should');
var request = require('supertest');
var app = require('../mainTest');

describe('WEB', function () {

    beforeEach(function () {

    });

    describe('/message/list GET', function () {

        it('Get message list works', function (done) {
    	
            request(app)
                .get('/spika/v1/message/list/test/0')
        		.expect(200) 
                .set('access-token', token1)
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}

                res.body.should.have.property('code');
                res.body.code.should.equal(1);
                res.body.should.have.property('data');
                                
                done();
            
            });   
            
        });

                              
    });
    
});