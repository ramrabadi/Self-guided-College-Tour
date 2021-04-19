# Deployment

The first thing to do will be to build and compile the application in Android Studio.

To do this you will open the project in Android Studio and then on the top menu bar hover over "Build", the Click on "Generate Signed Bundle or APK".

![Generate new APK](https://i.imgur.com/4QNDQZQ.png)

Then you can select either the "Android App Bundle", may be the better option here. This is the part where you will need to go and register an Android developer account.

When you have an account, you will either create a new "Key store path" or use an existing one depending on if this is the initial release of the application or a newer version.

The rest of the information will be related to your account or passwords for the new APK you just built.

## New App
- Add a title and description of the app
- Add Screen-shots of the application in use
- Determine Rating of the app (age rating)
- Select the category the app falls under, in this case mostlikley "Navigation"
- Add Privacy policy rules
- Upload APK file that you have already built

## Updating Existing App
- Add Update information
- Upload new APK you just built
- Make changes to privacy policy or description of the app


## JSON File

The Locations don't have to been in order. The names are case sensitive and need to be exactly as shown.  The JSON file should be in the app/src/main/assets folder. The name lat stands for Latitude and long for Longitude For information about Video, Image, Latitude and Longitude, see below.

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

## Get Latitude and Longitude

In the project folder under res/raw/ open the floor(1/2/3).osm in an application called [JOSM](https://josm.openstreetmap.de/) (Java Open Streat Map).  Once Installed open one of the .osm files and then mouse over the entrance to the room / area.  With these Lat, Lon values these will go into the JSON file for that "Location".  The exact path to the .osm files is app/src/main/res/raw.

## Where to place the API keys and JSON file name
To start the JSON file name can be anything you want it is currently set to "Test.json".  If you don't go with this you will need to change it in two location next to eachother in the MainActivity file.

![JSON File Name](https://i.imgur.com/Rh2DRuV.png)

Then to put your GraphHopper API Key stay in the MainActivity and at the very top.

![GraphHopper API](https://i.imgur.com/g57VuNY.png)

For the YouTube API Key open the TourActivity.kt file in the main directory and replace the key just before

![YouTube API Location](https://i.imgur.com/aLLl0UK.png)
