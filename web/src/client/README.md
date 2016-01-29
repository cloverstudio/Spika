# Spika Web Client

## Change /src/client/js/init.js

Change this part to fit your environment.

```
    Config.apiBaseUrl = "http://localhost:8080/spika/v1";
    Config.socketUrl = "http://localhost:8080/spika";
    
```

## Try standalone.

Just opent http://localhost:8080/spika from your web browser.

## Embed 

First import adapter.js to your web page.

```
<script src=" path to adapter.js" type="text/javascript"></script>
```

Then start spika like this.

```

 SpikaAdapter.attach({

    spikaURL: Config.SpikaBaseURL,
    attachTo : "chat", // id or div to show into
    user : {
        id : UserManager.user.id,
        name : UserManager.user.name,
        avatarURL : UserManager.user.avatar,
        roomID : channel
    },
    config : {
        apiBaseUrl : window.location.origin + Config.SpikaBaseURL + "/v1",
        socketUrl : window.location.origin + Config.SpikaBaseURL,
        showSidebar : false,
        showTitlebar : false
    },
    listener : {

        onPageLoad: function(){
            
        },
        onNewMessage:function(obj){

        },
        onNewUser:function(obj){

        },
        onUserLeft:function(obj){

        },
        OnUserTyping:function(obj){

        },
        OnMessageChanges:function(obj){

        },
        onOpenMessage:function(obj){
            return true;
        },
        OnOpenFile:function(obj){
            return true;
        }
    }

});

```

It creates iframe inside of div which id is given to attachTo parameter.