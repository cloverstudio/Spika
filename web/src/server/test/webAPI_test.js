var should = require('should');
var request = require('supertest');
var app = require('../mainTest');

describe('WEB', function () {

    var req, res;

    beforeEach(function () {
        req = {};
        res = {
            redirect: function () { },
            render : function () { }
        };
    });

    describe('/test GET', function () {
    
        it('should be Hello', function (done) {

            request(app)
                .get('/spika/v1/test')
                .end(function (err, res) {

    			if (err) {
    				throw err;
    			}
			
                res.text.should.be.exactly("Hello");
                
                done();
            
            });   
            
        });
    });
    
});