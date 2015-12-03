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
                .attach('file', 'src/server/test/samplefiles/max.jpg')
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}
                
                res.text.should.be.exactly("ok");
                                
                done();
            
            });   
            
        });
        
         it('Return error when file is not sent', function (done) {
    	
            request(app)
                .post('/spika/v1/message/sendFile')
        		.expect(200) 
        		.field('roomID', 'test')
                .field('userID', 'test')
                .field('type', '2')
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}
                
                res.body.should.have.property('code');
                res.body.code.should.equal(1000007);
                                
                done();
            
            });   
  
        });
                            
         it('Return error when roomID is not sent', function (done) {
    	
            request(app)
                .post('/spika/v1/message/sendFile')
        		.expect(200) 
                .field('userID', 'test')
                .field('type', '2')
                .attach('file', 'src/server/test/samplefiles/max.jpg')
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}
                
                res.body.should.have.property('code');
                res.body.code.should.equal(1000008);
                                
                done();
            
            });   
  
        });
                            

         it('Return error when userID is not sent', function (done) {
    	
            request(app)
                .post('/spika/v1/message/sendFile')
        		.expect(200) 
        		.field('roomID', 'test')
                .field('userID', '')
                .field('type', '2')
                .attach('file', 'src/server/test/samplefiles/max.jpg')
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}
                
                res.body.should.have.property('code');
                res.body.code.should.equal(1000009);
                                
                done();
            
            });   
  
        });
        
         it('Return error when roomID is not sent', function (done) {
    	
            request(app)
                .post('/spika/v1/message/sendFile')
        		.expect(200) 
        		.field('roomID', 'test')
                .field('userID', 'test')
                .attach('file', 'src/server/test/samplefiles/max.jpg')
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}
                
                res.body.should.have.property('code');
                res.body.code.should.equal(1000010);
                                
                done();
            
            });   
  
        });
                                    
    });
    
});