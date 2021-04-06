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
- Select the category the app falls under, in this case mostlikley "navigation"
- Add Privacy policy rules
- Upload APK file that you have already built

## Updating Existing App
- Add Update information
- Upload new APK you just built
- Make changes to privacy policy or description of the app


## JSON File

The Locations don't have to been in order. The names are case sensitive and need to be exactly as shown.  The JSON file should be in the app/src/main/assets folder.

![JSON Format](https://i.imgur.com/qQIHAtL.png)


## QR codes generation (TBD)

For this we still are trying to make it so that the owner can generate a qr code for a stop(i.e. a location) and also have it so that if the user doesn't have the app already installed then it will take them to the Play Store.

