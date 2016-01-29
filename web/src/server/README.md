# Spika Backend

Spika Backend is backend system for Spika Web/iOS/Android client. To setup this you need to have Linux based server with root permission.

## Installing Spika Backend to Ubuntu 14.04

####  1. Setup environment

```{r, engine='bash', count_lines}

$ sudo apt-get update

$ sudo apt-get install git mongodb npm nodejs imagemagick

# It is only for Ubuntu 14.04
$ ln -s /usr/bin/nodejs /usr/bin/node

$ git clone https://github.com/cloverstudio/Spika.git

$ cd Spika/web

$ npm install 

$ npm install gulp -g
```

#### 2.  Edit src/server/init.js

Change following lines
```ini
Config.host = "localhost";
Config.port = 80; 
Config.urlPrefix = '/spika'; 
Config.socketNameSpace = '/spika';
```
Config.urlPrefix is that you can configure root URL for Spika. For example if you want run spika http://[ipaddress]/aaaa/bbbb/spika, it will be '/aaaa/bbbb/spika'.

Config.socketNameSpace is name space for web socket, in most case you need not change this.


#### 3. Edit src/client/js/init.js
This is not necessary if you don't use web client. But I recommend you to setup web client to test configuration.

```ini
Config.apiBaseUrl = "http://localhost:8080/spika/v1";
Config.socketUrl = "http://localhost:8080/spika";
```

Config.apiBaseUrl should be host + urlPrefix + "v1" from server configuration.

Config.socketUrl should be same as host + socketNameSpace.


####4. Generate public files and start server.
```{r, engine='bash', count_lines}
# Generate files in public dir
$ gulp build-dist

# Start server in stand alone mode
$ node src/server/main.js
```

####5. Open standalone web client in browser
http[s]://host:port/[urlPrefix]

ex): http://45.55.81.215/spika/


