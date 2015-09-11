# Spika Backend

Spika Backend is backend system for Spika Web/iOS/Android client. To setup this you need to have Linux based server with root permission.

## Installing Spika Backend to Ubuntu 14.04

```{r, engine='bash', count_lines}
$ sudo apt-get update
$ sudo apt-get install git mongodb npm nodejs imagemagick
$ ln -s /usr/bin/nodejs /usr/bin/node
$ git clone https://github.com/cloverstudio/Spika.git
$ cd Spika
$ npm install 
$ npm install gulp -g

```

Edit src/server/init.js

Edit src/client/js/init.js

```{r, engine='bash', count_lines}
$ gulp build-dist
node src/server/main.js
```