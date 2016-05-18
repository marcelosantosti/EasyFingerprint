# Introduction #

This library is an easy implementation of the fingerprint API introduced in Android 6.0 (Android Marshmallow). The main feature of the library is the FingerprintDialog which you need just instantiate using the **newInstance** method pattern adopted by Android passing the callback associated for success and failure on authentication.

# How to Use #

There is a library release published in GitHub using **.aar** file created by Android Studio when compiling an Android Module. To publish the .aar to the world, Jitpack.IO was used poiting to the GitHub release and the library is available to be used on Gradle and Maven build system as dependencies. Because of that, you have to import the jitpack.io in your Gradle and add dependency to EasyFingerprint, as exemplified below:

** include example of Gradle dependency **

In your code, instantiate FingerprintDialog using the static method **newInstance** passing the callback function to receive the **onAuthenticated** and **onFailure** events. In the **onAuthenticated** callback, you will receive a parameter called **publicKey** which you can store and associate in some way with an user, validate if the fingerprint is valid and if it corresponds with the previous fingerprint recognized. This type of verification can be done localy or the public key can be transmited to a server to be validated. Example of usage:

**include an example of usage**

# To Do's #

There are some improvements which I would like to develop, listed below:

1. Create an abstration of logic to authenticate with fingerprint to not be dependent on FingerprintDialog and give developers freedom to implenent the authentication anywhere, with or without feedbacks to the user.
2. Create an example in some backend tecnology, preferably .NET, to enroll and validate private key generated and associated with one fingerprint.
3. Encapsulate Samsung Fingerprint API in the library to permit the developer to use in Samsung devices with Samsung fingerprint hardwares and with Inprint API published by Google natively in Android. This abstration would permit developers to use both API's without concerning about spefics rules from one or another.
