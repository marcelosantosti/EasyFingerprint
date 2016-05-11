package com.marcelosantosti.fingerprintdialog;

import java.security.Signature;

/**
 * Created by mlsantos on 11/05/2016.
 */
public interface FingerprintCallback {

    void onAuthenticated(Signature signature);
    void onError(Integer errorCode, String errorMessage);
}
