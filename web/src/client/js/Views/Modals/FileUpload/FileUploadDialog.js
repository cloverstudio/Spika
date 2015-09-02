var $ = require('jquery');
var _ = require('lodash');
var U = require('../../../libs/utils.js');
var template = require('./FileUploadDialog.hbs');

var ErrorDialog = {
    
    onSend : null,
    show : function(postURL,params,onSend){
        
        var self = this;
        
        this.onSend = onSend;
        
        console.log(params);
        
        $('body').append(template({
            postURL:postURL,
            params:params
        }));
        
        $('#modal1').on('hidden.bs.modal', function (e) {

            $('#modal1').remove();

        })
        
        $('#modal1').modal('show');
        
        
        $('#modal-btn-close').on('click',function(){
            self.hide();
        });
        
        $('#fileupload-dialog-file-input').on('change',function(event){
            
            $('#fileupload-dialog-uploading').show();
            
            $('#fileupload-dialog-iframe').on('load',function(){
                
                self.hide();
                
            });
            
            if(self.onSend)
                onSend();

        });
                
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