package com.marcelosantosti.fingerprintdialog;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.app.DialogFragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;

public class FingerprintAuthenticationDialogFragment extends DialogFragment {

    private static final String KEY_STORE_NAME = "AndroidKeyStore";
    private static final String KEY_ALIAS = "Finger";
    private static final String CRYPTO_ALGORITHYM = "SHA256withECDSA";
    private static final String GEN_PARAMETER_SPEC = "secp256r1";

    private Button mCancelButton;

    private FingerprintManagerCompat fingerprintManagerCompat;
    private Signature signature;
    private FingerprintManagerCompat.CryptoObject cryptoObject;
    private KeyStore keyStore;
    private KeyPairGenerator keyPairGenerator;
    private CancellationSignal cancellationSignal;

    private FingerprintCallback fingerprintCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                initFingerprint();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle(getString(R.string.sign_in));
        View v = inflater.inflate(R.layout.fingerprint_dialog_container, container, false);
        mCancelButton = (Button) v.findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;
    }

    private void initFingerprint() {

        try {

            fingerprintManagerCompat = FingerprintManagerCompat.from(getContext());
            signature = Signature.getInstance(FingerprintAuthenticationDialogFragment.CRYPTO_ALGORITHYM);
            cryptoObject = new FingerprintManagerCompat.CryptoObject(signature);

            initKeyStore();
            initSignature();

            fingerprintManagerCompat.authenticate(cryptoObject, 0, cancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errMsgId, CharSequence errString) {
                    super.onAuthenticationError(errMsgId, errString);

                    Log.e("FingerprintDialog", errMsgId + " - " + errString);

                    if (errMsgId != FingerprintManager.FINGERPRINT_ERROR_CANCELED)
                        sendFingerprintCallbackError(errMsgId, errString.toString());
                }

                @Override
                public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                    super.onAuthenticationHelp(helpMsgId, helpString);

                    sendFingerprintCallbackError(helpMsgId, helpString.toString());
                }

                @Override
                public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);

                    onFingerprintAuthenticated(result);
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();

                    sendFingerprintCallbackError(null, null);
                }
            }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendFingerprintCallbackError(Integer errorCode, String errorMessage) {

        if (fingerprintCallback != null)
            fingerprintCallback.onError(errorCode, errorMessage);

        dismiss();
    }

    private void onFingerprintAuthenticated(FingerprintManagerCompat.AuthenticationResult result) {

        try {

            Signature signature = result.getCryptoObject().getSignature();

            if (fingerprintCallback != null)
                fingerprintCallback.onAuthenticated(signature);

            dismiss();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void initKeyStore() throws Exception {

        keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, FingerprintAuthenticationDialogFragment.KEY_STORE_NAME);
        keyPairGenerator.initialize(new KeyGenParameterSpec.Builder(FingerprintAuthenticationDialogFragment.KEY_ALIAS, KeyProperties.PURPOSE_SIGN).setDigests(KeyProperties.DIGEST_SHA256).setAlgorithmParameterSpec(new ECGenParameterSpec(GEN_PARAMETER_SPEC)).setUserAuthenticationRequired(true).build());
        keyPairGenerator.generateKeyPair();
        keyStore = KeyStore.getInstance(FingerprintAuthenticationDialogFragment.KEY_STORE_NAME);
    }

    private boolean initSignature() throws Exception {

        keyStore.load(null);
        PrivateKey key = (PrivateKey) keyStore.getKey(FingerprintAuthenticationDialogFragment.KEY_ALIAS, null);
        signature.initSign(key);
        return true;
    }

//    private PublicKey getPublicKey() throws Exception {
//
//        KeyStore keyStore = KeyStore.getInstance(KEY_STORE_NAME);
//        keyStore.load(null);
//        PublicKey publicKey = keyStore.getCertificate(KEY_ALIAS).getPublicKey();
//        KeyFactory factory = KeyFactory.getInstance(publicKey.getAlgorithm());
//        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey.getEncoded());
//        PublicKey verificationKey = factory.generatePublic(spec);
//
//        return verificationKey;
//    }

    public FingerprintCallback getFingerprintCallback() {
        return fingerprintCallback;
    }

    public void setFingerprintCallback(FingerprintCallback fingerprintCallback) {
        this.fingerprintCallback = fingerprintCallback;
    }
}
