SmartCATCH-Client
=================

An android client for the SmartCATCH/MGH project


# Project Configuration

There are few steps you need to take to get the SmartCATCH client building.

Using the Eclipse + ADT IDE is the preferred way of building the
application.

## Importing SmartCATCH & dependencies

First, import the SmartCATCH app and its dependencies into your workspace.

In a new directory, clone this repo and the following repositories:

[funf-v4](https://github.com/OpenSensing/funf-v4)
[OpenPDS-Client](https://github.com/HumanDynamics/openPDSClient)

Then, for each project, do the following in Eclipse: go to

    `New -> Other -> Android Project From Existing Code
    
and navigate to the project directory, importing the code. To make
things easier, be sure to select `Copy Project into Workspace`.


## Importing Android dependencies

These projects all rely on Android dependencies you should have or can
download through the Android SDK manager.

Follow these steps to get the required deps in your workspace:

1. Import google play services

    Import an android project from existing code, and import the google
    play services library.

    For me, this was located at:

    `..../path-to-android-sdk/extras/google/google_play_services/libproject/google-play-services_lib`

2. Import appcompat-v7

    Same thing as for the google play services library, but for appcompat.

    For me, it was located at:

    `..../path-to-android-sdk/extras/android/support/v7/appcompat`

## Resolve project dependencies in the build paths

We need to change the build paths of the projects to use the
dependencies they need. We'll go through each project, making sure the
dependencies are listed correctly.

1. Make sure `funf`, `google-play-services-lib`, and `appcompat` are
   listed as libraries.

    For each project, open its' properties, select `Android` on the
    left frame, and check the box `Is Library` in the middle of the window.

1. OpenPDSClient

    Open the Project Properties for OpenPDSClient, and select
    `Android` on the left frame.

    In the `Library` frame of the window, click `Add...`, and add
    `funf` and `google-play-services-lib` as library dependencies.

2. SmartCATCH-Client

    Open the Project Properties for SmartCATCH-Client (It will be
    listed as `SplashActivity`), and select `Android` on the left frame.

    In the Library frame in the bottom tab, add `OpenPDSClient`,
    `funf`, and `appcompat` as library dependencies.

A final step that isn't necessary but can resolve possible
complications is to make sure the project is using a single android
support library. `appcompat` comes with `android-support-v4.jar` as a
library, but our `OpenPDSClient` comes with `android-support-v13`,
which contains `v4`. You might have seen some errors complaining that
there are two support libraries in the project, and that the build
system is defaulting to `v13`.

To only use `v13`, right-click on the `android-support-v4.jar` in the
`appcompat` project, and delete it.

Then, open the project properties for `appcompat`, select `Java Build
Path`, open the `Libraries` tab, and click `Add JARs...` on the right
side of the window.

Then, open the `OpenPDSClient` directory, then the `libs` dir nested
inside of it, and select `android-support-v13.jar`.

Then, delete the `android-support-v4.jar` from the `OpenPDSClient` lib
folder, if it exists.

You should be able to build `SplashActivity` (the smartCATCH client)
now with no problems.
