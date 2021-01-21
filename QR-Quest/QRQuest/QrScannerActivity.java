package uk.ac.ncl.team2.qrquest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * TODO Replace with finished QrScannerActivity
 * Takes in correct qr code using intents
 * Allows user to scan a qr code.
 *      If incorrect inform user
 *      If correct set result to RESULT_OK and finish
 * Include button to go back to QuestActivity
 */
public class QrScannerActivity extends AppCompatActivity {

    public TextView textViewCorrectQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        textViewCorrectQR = (TextView) findViewById(R.id.textView_correctQR);
        textViewCorrectQR.setText( getIntent().getStringExtra("CORRECT_QR") );
    }

    public void attemptScan(View view)
    {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
