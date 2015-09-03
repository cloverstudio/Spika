var CONST = require('../consts');
var _ = require('lodash');
var U = require('./utils.js');
var LocalizationManager = require('./localizationManager.js');
var Handlebars = require('hbsfy/runtime');
Handlebars.registerHelper('test', function(context, options) {
  return options.fn(context);
});

(function(global) {
    "use strict;"

    // Class ------------------------------------------------
    function ViewHelpers() {
    };

    // Header -----------------------------------------------
    ViewHelpers.prototype.attach = attach; 

    // Implementation ---------------------------------------
    
    function attach(){
 
        Handlebars.registerHelper("formatDate", function(ut) {
          return  U.formatDate(ut,false);
        });

        Handlebars.registerHelper("formatTime", function(ut) {
          return  U.formatTime(ut);
        });

        Handlebars.registerHelper("length", function(ary) {
          return  ary.length;
        });
        
        Handlebars.registerHelper("l10n", function(text) {
          return  LocalizationManager.localize(text);
        });
        
    }
    
    // Exports ----------------------------------------------
    module["exports"] = new ViewHelpers();

})((this || 0).self || global);