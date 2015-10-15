var should = require('should');
var request = require('supertest');
var app = require('../mainTest');

describe('WEB', function () {

    describe('/file/upload POST', function () {

        it('Upload image gives thumbnail.', function (done) {
    	
            request(app)
                .post('/spika/v1/file/upload')
        		.expect(200) 
                .attach('file', 'test/samplefiles/max.jpg')
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}
                
                res.body.should.have.property('success');
                res.body.should.have.property('result');
                res.body.result.should.have.property('file');
                res.body.result.file.should.have.property('id');

                res.body.result.should.have.property('thumb');
                res.body.result.thumb.should.have.property('id');
                                
                done();
            
            });   
            
        });

        it('Upload normal file works.', function (done) {
    	
            request(app)
            
                .post('/spika/v1/file/upload')
        		.expect(200) 
                .attach('file', 'test/samplefiles/test.text')
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}
                
                res.body.should.have.property('success');
                res.body.should.have.property('result');
                res.body.result.should.have.property('file');
                res.body.result.file.should.have.property('id');
                                
                done();
            
            });   
            
        });
                      
    });
    
});