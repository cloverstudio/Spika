var SpikaAdapter = {
    
    listener : null,
    attach : function(options){
        
        if(!options)
            return;
            
        if(!options.attachTo)
            return;
        
        if(!options.spikaURL)
            return;
            
        if(!options.user)
            return;
        
        if(!options.user.id)
            return;
            
        if(!options.user.name)
            return;
            
        if(!options.user.roomID)
            return;
        
        
        window.bootOptions = options;

        if(options.mode == 'div'){
        
            window.startSpikaIntoDiv();   	
                    
        }else{
	            
            // attach to dom
            var iframe = document.createElement('iframe');
        
            var url = options.spikaURL;
                
            url += "?params=" + encodeURIComponent(JSON.stringify(options));
        
            iframe.src = url;
            iframe.width = '100%';
            iframe.height = '100%';
            iframe.frameBorder = 0;
        
            var node = document.getElementById(options.attachTo);
        
            while (node.hasChildNodes()) {
                node.removeChild(node.firstChild);
            }

            node.appendChild(iframe);
       
        }

        if(options.listener)
            this.listener = options.listener;
        
    }
    
}

// export to global
window.SpikaAdapter = SpikaAdapter;