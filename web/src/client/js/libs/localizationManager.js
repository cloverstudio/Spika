var _ = require('lodash');

var LocalizationManager = {
    
    langTable : {},
    init : function(language){
	   	 
		//this.langTable = require('../lang/' + language + '.js');
	    
    },
    localize : function(baseString){
                
        if(!_.isUndefined(this.langTable[baseString]))
            return this.langTable[baseString];
        
        return baseString;
        
    }
    
}

module["exports"] = LocalizationManager;