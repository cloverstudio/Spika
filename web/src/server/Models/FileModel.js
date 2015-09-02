var mongoose = require('mongoose');
var _ = require('lodash');
var Const = require('../const.js');
var async = require('async');
var Util = require('../lib/Utils');
var Settings = require("../lib/Settings");

var FileModel = function(){
    
};

FileModel.prototype.model = null;

FileModel.prototype.init = function(){

    // Defining a schema
    var fileSchema = new mongoose.Schema({
        name: String,
        mimeType: String,
        size: Number,
        created: Number
    });
    
    this.model = mongoose.model(Settings.options.dbCollectionPrefix + "files", fileSchema);
    return this.model;	        
}

module["exports"] = new FileModel();