# Spika Chat Module

## Use demo application:

### Prerequisites for demo application

 - Android studio
 - SDK version 23
 - Build tools version 22.0.1 or higher 

### Steps:

 - Download DemoCloverApp
 - Download SpikaChatModule
 - Put it to same folder
 - Open DemoCloverApp.iml from DemoCloverApp and run application


## Use spika chat module in new project

 - Download SpikaChatModule
 - Put it at same folder of your new project
 - Make new project in AndroidStudio
 - Open Project structure (File > Project Structure)
 - Click on green plus icon to add new module
 - Choose import gradle project
 - Find SpikaChatModule, check import and set module name ":module" (can set any other name to module) and click finish and wait for gradle to sync
 - Click on your project in module, and go to dependencies tab, click on right green plus icon and select module dependecy and double click to module
 - In gradle of your aplication paste this below buildTypes 
```
		 packagingOptions {
			exclude 'META-INF/DEPENDENCIES'
			exclude 'META-INF/LICENSE'
			exclude 'META-INF/LICENSE.txt'
			exclude 'META-INF/license.txt'
			exclude 'META-INF/NOTICE'
			exclude 'META-INF/NOTICE.txt'
			exclude 'META-INF/notice.txt'
			exclude 'META-INF/ASL2.0'
		    }
```
 - In your application res/style.xml set base AppTheme parent to Theme.AppCompat.Light.NoActionBar
 - Now you can run application
 - To open ChatActivity, create User object
```
		User user = new User();
        	user.roomID = "room_id";  ->  id of room
        	user.userID = "user_id";  ->  id of user
        	user.name = "User Name";  ->  name of user
        	user.avatarURL = "http://45.55.81.215/spika/img/avatar.jpg";  ->  user avatar, this is optional
```
and start activity with:  
```
		ChatActivity.startChatActivity(this, user);
```
 - You can use Your own backend server, just create Config object
```
		Config config = new Config();
		config.apiBaseUrl = "http://ossdemo.spika.chat/spika/v1/";
		config.socketUrl = "http://ossdemo.spika.chat/spika";
```
and start activity with:
```
		ChatActivity.startChatActivityWithConfig(this, user, config);
```

