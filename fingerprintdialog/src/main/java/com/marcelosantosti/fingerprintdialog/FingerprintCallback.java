package com.marcelosantosti.fingerprintdialog;

import java.security.PublicKey;
import java.security.Signature;

/**
 * Created by mlsantos on 11/05/2016.
 */
public interface FingerprintCallback {

    void onAuthenticated(Signature signature, PublicKey publicKey);
    void onError(Integer errorCode, String errorMessage);
}
