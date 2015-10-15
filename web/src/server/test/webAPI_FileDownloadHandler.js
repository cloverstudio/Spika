var should = require('should');
var request = require('supertest');
var app = require('../mainTest');

describe('WEB', function () {

    beforeEach(function () {

    });

    describe('/file/download GET', function () {

        it('Download works', function (done) {
    	
            request(app)
                .get('/spika/v1/file/download/aaaa')
        		.expect(200) 
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}
                    
                done();
            
            });   
            
        });

        it('Download fails if file id is not provided.', function (done) {
    	
            request(app)
                .get('/spika/v1/file/download/')
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