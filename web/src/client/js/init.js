(function(global) {

    "use strict;"

    var Config = {};

    Config.apiBaseUrl = "http://45.55.81.215/v1";
    Config.socketUrl = "http://45.55.81.215";
    Config.defaultContainer = "#spika-container";
    Config.showSidebar = true;
    Config.showTitlebar = true;
    Config.thumbnailHeight = 256;
    
    // Exports ----------------------------------------------
    module["exports"] = Config;

})((this || 0).self || global);
