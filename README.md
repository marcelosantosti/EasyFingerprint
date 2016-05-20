# Introduction #

This library is an easy implementation of the fingerprint API introduced in Android 6.0 (Android Marshmallow). The main feature of the library is the FingerprintDialog which you need just instantiate and set the callback associated for success and failure on authentication.

# How to Use #

There is a library release published in GitHub using **.aar** file created by Android Studio when compiling an Android Module. To publish the .aar to the world, Jitpack.IO was used poiting to the GitHub release and the library is available to be used on Gradle and Maven build system as dependencies. Because of that, you have to import the jitpack.io in your main Gradle and add dependency to EasyFingerprint, as exemplified below:

**main.gradle**
```
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

**module.gradle or application.gradle**
```
compile 'com.github.marcelosantosti:EasyFingerprint:0.1.0'
```

In your code, instantiate FingerprintDialog and set the callback function **setFingerprintCallback** to receive the **onAuthenticated** and **onFailure** events on class **FingerprintAuthenticationDialogFragment**. In the **onAuthenticated** callback. Example of usage:
```
if (FingerprintManagerCompat.from(this).hasEnrolledFingerprints()) {

    FingerprintAuthenticationDialogFragment fingerprintAuthenticationDialogFragment = new FingerprintAuthenticationDialogFragment();
    fingerprintAuthenticationDialogFragment.setFingerprintCallback(new FingerprintCallback() {
        @Override
        public void onAuthenticated(Signature signature) {

            onFingerprintAuthenticated(signature);
        }

        @Override
        public void onError(Integer errorCode, String errorMessage) {

            onFingerprintError(errorCode, errorMessage);
        }
    });
    fingerprintAuthenticationDialogFragment.show(getSupportFragmentManager(), "tag");
}
else {

    Toast.makeText(this, getString(R.string.register_at_least_one_fingerprint), Toast.LENGTH_LONG).show();
}
```

# How to Test #

The library can be test in any device with fingerprint sensor or with the latest version of Android Studio emulators (2.0.1).

# Known Issues #

When using the default Android Studio emulators, there is a bug that you have to register a new Fingerprint in security configurations of Android when the emulator starts. This bug doesn´t occur when using a real device.

# To Do's #

There are some improvements which I would like to develop, listed below:

1. Create an abstration of logic to authenticate with fingerprint to not be dependent on FingerprintDialog and give developers freedom to implenent the authentication anywhere, with or without feedbacks to the user.
2. Create an example to validate if the fingerprint is associated in some way with the user of an application.
3. Create an example in some backend tecnology, preferably .NET, to enroll and validate public key generated and associated with one fingerprint.
4. Encapsulate Samsung Fingerprint API in the library to permit the developer to use in Samsung devices with Samsung fingerprint hardwares and with Inprint API published by Google natively in Android. This abstration would permit developers to use both API's without concerning about spefics rules from one or another.
5. Localize string to be used in other languages.
6. Refactor instantiation of FingerprintDialog to use **newInstance** method adopted by Android when instantiating a new Fragment.
7. Include method **hasFingerprintEnrolled** in a Utility class in the library.