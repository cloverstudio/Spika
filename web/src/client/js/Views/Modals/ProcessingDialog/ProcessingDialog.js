var $ = require('jquery');
var _ = require('lodash');
var U = require('../../../libs/utils.js');
var template = require('./ProcessingDialog.hbs');

var ProcessingDialog = {
    
    show : function(){
        
        var self = this;
        
        $('body').append(template());
                
        $('#modal-processing').on('hidden.bs.modal', function (e) {

            $('#modal-processing').remove();

        })
        
        $('#modal-processing').modal('show');
        
    },
    hide : function(){

        $('#modal-processing').modal('hide');
                
    }
    
}

module.exports = ProcessingDialog;