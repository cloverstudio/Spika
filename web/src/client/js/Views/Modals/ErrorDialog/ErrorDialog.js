var $ = require('jquery');
var _ = require('lodash');
var U = require('../../../libs/utils.js');
var template = require('./ErrorDialog.hbs');

var ErrorDialog = {
    
    show : function(title,text,onRetry){
        
        var self = this;
        
        $('body').append(template({
            title:title,
            text:text
        }));
        

        $('#modal1').on('hidden.bs.modal', function (e) {

            $('#modal1').remove();

        })
        
        $('#modal1').modal('show');
        
        
        $('#modal-btn-close').on('click',function(){
            self.hide();
        });
        
        if(_.isUndefined(onRetry)){
            
            $('#modal-btn-retry').hide();
            
        }else{
        
            $('#modal-btn-retry').on('click',function(){
                
                if(!_.isUndefined(onRetry))
                    onRetry();
    
            });
            
        }

        
        
    },
    hide : function(onFinish){

        $('#modal1').on('hidden.bs.modal', function (e) {

            $('#modal1').remove();
            
            if(!_.isUndefined(onFinish)){
                onFinish();
            }

        })

        $('#modal1').modal('hide');
                
    }
    
}

module.exports = ErrorDialog;