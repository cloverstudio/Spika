// define global functions here
var Config = require('../init');

// Spika Selector
window.SS = function(sel){
    var selector = Config.defaultContainer + " " + sel;
    return $(selector);
}