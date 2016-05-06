package marcelosantosti.com.fingerprintserverintegrationsample;

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
            fingerprintAuthenticationDialogFragment.show(getSupportFragmentManager(), "tag");
        }
        else {

            Toast.makeText(this,
                    "Go to 'Settings -> Security -> Fingerprint' and register at least one fingerprint",
                    Toast.LENGTH_LONG).show();
        }
    }
}
