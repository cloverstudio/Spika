(function(global) {

    "use strict;"

    var Config = {};

    Config.apiBaseUrl = "http://128.199.248.72/spika/v1";
    Config.socketUrl = "http://128.199.248.72/spika";
    Config.defaultContainer = "#spika-container";
    Config.showSidebar = true;
    Config.showTitlebar = true;
    Config.thumbnailHeight = 256;
    
    // Exports ----------------------------------------------
    module["exports"] = Config;

})((this || 0).self || global);
