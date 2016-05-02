package marcelosantosti.com.fingerprintserverintegrationsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        FingerprintAuthenticationDialogFragment fingerprintAuthenticationDialogFragment = new FingerprintAuthenticationDialogFragment();
        fingerprintAuthenticationDialogFragment.show(getSupportFragmentManager(), "tag");
    }
}
