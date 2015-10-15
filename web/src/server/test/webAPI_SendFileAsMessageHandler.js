var should = require('should');
var request = require('supertest');
var app = require('../mainTest');

describe('WEB', function () {

    describe('/message/sendFile POST', function () {

         it('Send file as message works.', function (done) {
    	
            request(app)
                .post('/spika/v1/message/sendFile')
        		.expect(200) 
        		.field('roomID', 'test')
                .field('userID', 'test')
                .field('type', '2')
                .attach('file', 'test/samplefiles/max.jpg')
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}
                
                res.text.should.be.exactly("ok");
                                
                done();
            
            });   
            
        });
                              
    });
    
});