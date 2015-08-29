# openhab-fx [![Build Status](https://travis-ci.org/swehacker/openhab-fx.svg)](https://travis-ci.org/swehacker/openhab-fx) [![Issues](http://img.shields.io/github/issues/swehacker/openhab-fx.svg)]( https://github.com/swehacker/openhab-fx/issues )
Old Ipad's and Android devices started to pile up and I asked myself what to do with them, so I came up with the idea to
 use them as control interfaces for my home automation system. Since this was a "lazy sunday" project so I did not want
 to spend to much time of hacking Swift and Java for Android and Desktop I decided to test out JavaFX and deploy it to different
 systems.

This is just a test on how to use JavaFX to handle the UI for OpenHAB, and deploy it to different platform like desktop, iOS and Android.

![Screenshot1](/docs/images/screenshot-desktop1.png)

## How to build
To build and deploy for the respective device (iOS and Android) you need their sdk's installed. For mac it's XCode which
you can download from AppStore and for Android go to http:///developer.android.com

Sadly you need to buy a developer license to be able to deploy to iOS devices, but you can use the iOS Simulator.

### Compile JavaFX
```
gradle build
```

### How to build and test iOS
To build the .ipa file for iOS devices just type 
```
gradle ios
```

To use the iOS simulator that comes with XCode:
```

```

For deploying to a connected iOS device you need only to type:
```
gradle launchIOSDevice
```
Remember to add your device to your xcode environment or you will get an error.

### How to build and test Android
To build the .apk file for Android devices just type 
```
gradle android
```

For deploying to a connected device you need only to type:
```
gradle androidinstall
```
Remember to enable DeveloperOption on your device to allow USB debugging and "Unknown sources".

