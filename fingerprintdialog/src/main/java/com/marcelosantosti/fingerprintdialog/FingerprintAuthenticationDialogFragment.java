package com.marcelosantosti.fingerprintdialog;

import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.app.DialogFragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;

public class FingerprintAuthenticationDialogFragment extends DialogFragment {

    private static final String KEY_STORE_NAME = "AndroidKeyStore";
    private static final String KEY_ALIAS = "Finger";
    private static final String CRYPTO_ALGORITHYM = "SHA256withECDSA";

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

        initFingerprint();
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
//
//            User user = new User();
//            user.setUsername("mlsantos");
//            user.setNonce(new SecureRandom().nextLong());

            //signature.update(user.toByteArray());

            if (fingerprintCallback != null)
                fingerprintCallback.onAuthenticated(signature);

            dismiss();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void initKeyStore() throws Exception {

        keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, FingerprintAuthenticationDialogFragment.KEY_STORE_NAME);
        keyPairGenerator.initialize(new KeyGenParameterSpec.Builder(FingerprintAuthenticationDialogFragment.KEY_ALIAS, KeyProperties.PURPOSE_SIGN).setDigests(KeyProperties.DIGEST_SHA256).setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1")).setUserAuthenticationRequired(true).build());
        keyPairGenerator.generateKeyPair();
        keyStore = KeyStore.getInstance(FingerprintAuthenticationDialogFragment.KEY_STORE_NAME);
    }

    private boolean initSignature() throws Exception {

        keyStore.load(null);
        PrivateKey key = (PrivateKey) keyStore.getKey(FingerprintAuthenticationDialogFragment.KEY_ALIAS, null);
        signature.initSign(key);
        return true;
    }

    public FingerprintCallback getFingerprintCallback() {
        return fingerprintCallback;
    }

    public void setFingerprintCallback(FingerprintCallback fingerprintCallback) {
        this.fingerprintCallback = fingerprintCallback;
    }
}
