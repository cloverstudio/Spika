var _ = require('lodash');
var LangTable = require('../lang/language');

var LocalizationManager = {
    
    langTable : {},
    init : function(language){
        
        this.langTable = LangTable[language];
	    console.log(LangTable,language,this.langTable);

    },
    localize : function(baseString){
        
        if(_.isEmpty(this.langTable))
            return baseString;
            
        if(!_.isEmpty(this.langTable[baseString]))
            return this.langTable[baseString];
        
        return baseString;
        
    }
    
}

module["exports"] = LocalizationManager;