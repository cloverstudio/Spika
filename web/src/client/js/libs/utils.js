(function(global) {
    "use strict;"

    // Class ------------------------------------------------
    function Utils() {
    };

    // Header -----------------------------------------------
    Utils.prototype.l = logging; 
    Utils.prototype.goPage = goPage;
    Utils.prototype.ie8Fix = ie8Fix;
    Utils.prototype.formatDate = formatDate;
    Utils.prototype.getRandomString = getRandomString;
    Utils.prototype.now = now;
    Utils.prototype.escapeHtml = escapeHtml;
    Utils.prototype.linkify = linkify;
    Utils.prototype.getURLQuery = getURLQuery;
    
    // Implementation ---------------------------------------
    function logging(obj) {
        console.log(obj);
    }

    function goPage(pageName) {
        document.location.href = "#" + pageName;
    }

    function ie8Fix() {
        
        if (typeof window.console == "undefined") {
            window.console = {log: function() {}};
        }

    }
    
    function formatDate(ut,useUserFriendlyText){
        
        var date = new Date(ut);
        // hours part from the timestamp
        var hours = date.getHours();
        // minutes part from the timestamp
        var minutes = date.getMinutes();
        // seconds part from the timestamp
        var seconds = date.getSeconds();
        
        // will display time in 10:30:23 format
        var month = date.getMonth() + 1;
        var day = date.getDate();
        var year = date.getYear();
        
        // dont want include browser detaction library so use this dumb style.
        if(year < 1000){
            year += 1900;
        }
        
        if(hours < 10)
            hours = '0' + hours;
            
        if(minutes < 10)
            minutes = '0' + minutes;
            
        if(seconds < 10)
            seconds = '0' + seconds;
        
        
        if(month < 10)
            month = '0' + month;
        
        if(day < 10)
            day = '0' + day;
        
        var formattedTime = year + '/' + month + '/' + day + ' ' + hours + ':' + minutes + ':' + seconds;
        
        if(useUserFriendlyText == false)
            return formattedTime;
        
        var nowDate = new Date();
        var now = new Date().getTime() / 1000;
        var interval = now - ut;
        
        if(interval < 60){
            return 'now';
        }
        else if(interval < 60*60){
            return  Math.floor(interval / 60) + " min ago";
        }
        else if(interval < 60*60*24){
            return  Math.floor(interval / 60 / 60) + " hours ago";
        }
        else if(nowDate.getDate() == date.getDate() && nowDate.getMonth() == date.getMonth() && nowDate.getYear() == date.getYear()){
            return  hours + ':' + minutes + ':' + seconds;
        }
        else{
            formattedTime;
        }
        
        
        return formattedTime;
    }
    
    function getRandomString(){
    
        var text = "";
        var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    
        for( var i=0; i < 32; i++ )
            text += possible.charAt(Math.floor(Math.random() * possible.length));
    
        return text;
    }
    
    function now(){
        Date.now = Date.now || function() { return +new Date; }; 
        
        return Date.now();
        
    }

    function escapeHtml(string) {
            
        var entityMap = {
            "&": "&amp;",
            "<": "&lt;",
            ">": "&gt;",
            '"': '&quot;',
            "'": '&#39;'
        };
    
        return String(string).replace(/[&<>"']/g, function (s) {
            return entityMap[s];
        });
    }

    function linkify(inputText) {
        var replacedText, replacePattern1, replacePattern2, replacePattern3;
    
        //URLs starting with http://, https://, or ftp://
        replacePattern1 = /(\b(https?|ftp):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/gim;
        replacedText = inputText.replace(replacePattern1, '<a href="$1" target="_blank">$1</a>');
    
        //URLs starting with "www." (without // before it, or it'd re-link the ones done above).
        replacePattern2 = /(^|[^\/])(www\.[\S]+(\b|$))/gim;
        replacedText = replacedText.replace(replacePattern2, '$1<a href="http://$2" target="_blank">$2</a>');
    
        //Change email addresses to mailto:: links.
        replacePattern3 = /(([a-zA-Z0-9\-\_\.])+@[a-zA-Z\_]+?(\.[a-zA-Z]{2,6})+)/gim;
        replacedText = replacedText.replace(replacePattern3, '<a href="mailto:$1">$1</a>');
                
        return replacedText;
    }
    
    function getURLQuery(){
        
        var split = window.location.search.substr(1).split('&');
        
        if (split == "") return {};
        
        var params = {};
        
        for (var i = 0; i < split.length; ++i)
        {
            var p=split[i].split('=', 2);
            if (params.length == 1)
                params[p[0]] = "";
            else
                params[p[0]] = decodeURIComponent(p[1].replace(/\+/g, " "));
        }
    
        return params;

    }


    // Exports ----------------------------------------------
    module["exports"] = new Utils();

})((this || 0).self || global);