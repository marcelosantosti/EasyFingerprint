package marcelosantosti.com.fingerprintserverintegrationsample;

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.marcelosantosti.fingerprintdialog.FingerprintAuthenticationDialogFragment;
import com.marcelosantosti.fingerprintdialog.FingerprintCallback;

import java.security.Signature;

public class MainActivity extends AppCompatActivity {

    private Button buttonAddFingerprint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initViews();
        this.initListeners();
    }

    private void initViews() {

        this.buttonAddFingerprint = (Button)super.findViewById(R.id.buttonAddFingerprint);
    }

    private void initListeners() {

        this.buttonAddFingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onButtonAddFingerprintClicked();
            }
        });
    }

    private void onButtonAddFingerprintClicked() {

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
    }

    private void onFingerprintAuthenticated(Signature signature) {

        try {

            Toast.makeText(this, "Usuário Autenticado", Toast.LENGTH_LONG).show();
            String teste = "";
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
