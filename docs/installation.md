# Clone Repository

To install "OU Guided Tour" clone this repository to your local machine. 

To clone it to your machine, open a terminal and enter or copy and paste the following.
```
git clone https://github.com/senior-design-20-21/tw.git
```

# Install Android Studio 
To use this application in development you need Android Studio.

Go to 
> https://developer.android.com/studio/index.html#downloads

to and download the version which corrisponds to the operating system you are using. Once downloaded install it onto your machine 

# Run Guide

Once you have the repository on your machine and have downloaded and installed Android Studio. Open up Android Studio.

This should be the first thing you see. Click on the "Open an existing Android Studio project".

![Welcome to Android Studio](https://i.imgur.com/xSKMNai.png)

It should then bring this up and you should navigate to where you cloned the repository and click it and then hit "OK".

![Select Project](https://i.imgur.com/zpvsiyl.png)

Next you have to go through the steps on your physical phone, which can be found here to use your device to run the app.
> https://developer.android.com/studio/run/device

That also goes throught the steps of setting up an AVD if these instructions are not enough help to you.

## Setup an AVD
First select the AVD drop down and select "Open AVD Manager".
![Main Screen](https://i.imgur.com/Jpl6FHc.png)

Then click the "+ Create Virtual Device" Option which should be in the bottom left corner.
![AVD Manager](https://i.imgur.com/fVlWkjD.png)

Next Select the device you would like to emulate. It should be a Pixel 3 or newer. (This is to make sure that it has the features needed for this app to run)
![Select Device](https://i.imgur.com/yU8POgi.png)

The API level should be 29 or higher.
![Select API](https://i.imgur.com/sqfKN6F.png)

Next you can name your new AVD and then hit "Finish".
![Name AVD](https://i.imgur.com/WXTyQXA.png)

Now all you have to do is hit the "Run" button on the main page. This should build the application and open up the emulator you have selected.

## JSON File

The Locations don't have to been in order. The names are case sensitive and need to be exactly as shown.  The JSON file should be in the app/src/main/assets folder.

![JSON Format](https://i.imgur.com/qQIHAtL.png)


## Video's URL
So only videos on YouTube work for this it uses YouTube's API.  WIth a video URL like "https://www.youtube.com/watch?v=fqlPsAAsZss" we only need the "fqlPAAsZss", so everything after the "=".

## Image URL
Just need the url of the image, with the "http/https://".

## QR codes generation

For this just use any QR code generator and store the value of the 'id' of the 'Location'.

## YouTube API key

To create a YouTube API key you will need to go to [Google Cloud Platform](https://console.cloud.google.com/apis/library/).  You will need to have a google account.  Find the YouTube API Service.

![YouTubeAPI](https://i.imgur.com/c3Gw632.png)

Once in the YouTube API page click on the "MANAGE" button which will allow you to create your own API key.

![Manage YouTube API](https://i.imgur.com/3VbHUnT.png)

At the top of the page click "CREATE CREDENTIALS" button and select "API key". Then you will want to restric accss to the key to only this application.

![Restrict key](https://i.imgur.com/tuA0RBQ.png)

Next open the application in Android Studio and on the right hand side of the application click the gradle button.

![Gradle icon](https://i.imgur.com/HAMfjln.png)

Next you will click the name of the application then"Tasks", then "android", here you will double click the "signingReport".

![SigningReport](https://i.imgur.com/w2b5mYS.png)

The "run" tab will open up and you will see the following.  You will copy the value for the SHA-1 value. 

![Select SHA1](https://i.imgur.com/Tt2FzMJ.png)

The SHA-1 value will be put in the field of "SHA-1 certificate fingerprint".  The Package name will be the name of the package which unless changed is currently "com.tw.ouguidedtour".  When these value are in press "DONE". Below that section select the "Restrict key" button and then only select the "YouTube Data API".

![Restrict API to YoutTube](https://i.imgur.com/st8uO4r.png)

## Graph Hopper API 

To create the api key for GraphHopper Directions API go to [GraphHopper](https://www.graphhopper.com).  This is much more straight forword than the YouTube API key.
