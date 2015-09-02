var U = require('../../libs/utils.js');
var Backbone = require('backbone');
var template = require('./Colors.hbs');


var ColorsView = Backbone.View.extend({
    
    el : null,
    
    initialize: function(options) {
        this.el = options.el;
        this.render();
    },

    render: function() {
	    
        $(this.el).html(template());
        
        var self = this;
        
        return this;

    }
    
});

module.exports = ColorsView;