package marcelosantosti.com.easyfingerprintsample;

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.marcelosantosti.fingerprintdialog.FingerprintAuthenticationDialogFragment;
import com.marcelosantosti.fingerprintdialog.FingerprintCallback;
import com.marcelosantosti.fingerprintdialog.FingerprintUtils;

import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

public class MainActivity extends AppCompatActivity {

    private Button buttonAddFingerprint;
    private Button buttonValidateFingerprint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initViews();
        this.initListeners();
    }

    private void initViews() {

        this.buttonAddFingerprint = (Button)super.findViewById(R.id.buttonAddFingerprint);
        this.buttonValidateFingerprint = (Button)super.findViewById(R.id.buttonValidateFingerprint);
    }

    private void initListeners() {

        this.buttonAddFingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onButtonAddFingerprintClicked();
            }
        });

        this.buttonValidateFingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onButtonValidateFingerprintClicked();
            }
        });
    }

    private void onButtonAddFingerprintClicked() {

        if (FingerprintManagerCompat.from(this).hasEnrolledFingerprints()) {

            FingerprintAuthenticationDialogFragment fingerprintAuthenticationDialogFragment = new FingerprintAuthenticationDialogFragment();
            fingerprintAuthenticationDialogFragment.setFingerprintCallback(new FingerprintCallback() {
                @Override
                public void onAuthenticated(Signature signature, PublicKey publicKey) {

                    onFingerprintAuthenticated(signature, publicKey);
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
    }

    private void onButtonValidateFingerprintClicked() {

        FingerprintAuthenticationDialogFragment fingerprintAuthenticationDialogFragment = new FingerprintAuthenticationDialogFragment();
        fingerprintAuthenticationDialogFragment.setFingerprintCallback(new FingerprintCallback() {
            @Override
            public void onAuthenticated(Signature signature, PublicKey publicKey) {

                //onFingerprintAuthenticated(signature);

                try {
                    boolean validate = FingerprintUtils.validateFingerprintPublicKey(publicKey, getApplicationContext());

                    if (validate)
                        Toast.makeText(getApplicationContext(), "Digital Válida", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "Digital inválida", Toast.LENGTH_LONG).show();

                } catch (SignatureException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Integer errorCode, String errorMessage) {

                onFingerprintError(errorCode, errorMessage);
            }
        });
        fingerprintAuthenticationDialogFragment.show(getSupportFragmentManager(), "tag");
    }

    private void onFingerprintAuthenticated(Signature signature, PublicKey publicKey) {

        try {

            Toast.makeText(this, "Usuário Autenticado", Toast.LENGTH_LONG).show();
            String teste = "";
            publicKey.getEncoded();

            //FingerprintUtils.saveFingerprintPublicKey(signature, this);
            FingerprintUtils.saveFingerprintPublicKey(publicKey, this);
        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void onFingerprintError(Integer errorCode, String errorMessage) {

        try {

            Toast.makeText(this, "Usuário Não Autenticado", Toast.LENGTH_LONG).show();

            Log.e("FingerprintSample", errorCode + " - " + errorMessage);
        }
        catch (Exception e) {


        }
    }
}
