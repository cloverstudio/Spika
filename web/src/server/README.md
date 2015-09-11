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

$ cd Spika

$ npm install 

$ npm install gulp -g
```

#### 2.  Edit src/server/init.js
#### 3. Edit src/client/js/init.js
This is not necessary if you don't use web client. But I recommend you to setup web client to test configuration.


####4. Generate public files and start server.
```{r, engine='bash', count_lines}
# Generate files in public dir
$ gulp build-dist

# Start server
$ node src/server/main.js
```