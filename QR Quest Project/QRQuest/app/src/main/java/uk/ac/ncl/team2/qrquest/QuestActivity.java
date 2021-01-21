package uk.ac.ncl.team2.qrquest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * TODO Replace with finished Activity
 * Takes in questProgress object from MainMenuActivity
 * Displays current clue to user
 * Has button to go to MapActivity
 * Has button to startActivityForResult QrScannerActivity
 *      Passes correct QR code using intents
 *  onActivityResult, if code was correct, go to next clue
 */
public class QuestActivity extends AppCompatActivity {

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 99;
    public  static final String[] PERMISSION_ARRAY = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int SCAN_REQUEST=1;
    public TextView textViewClue;
    private QuestProgress questProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        textViewClue = (TextView) findViewById(R.id.textView_clue);
        questProgress = (QuestProgress) getIntent().getSerializableExtra("QUEST");
        displayClue();

    }

    private void displayClue(){
        if(questProgress.getProgress() < questProgress.getQuest().getNumberOfClues())
            textViewClue.setText(questProgress.getClue(questProgress.getProgress()).getClue());
        else
            textViewClue.setText("No more clues");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public void requestMapPermission(View view)
    {
        //checking and asking for location permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(this, "request permission 2", Toast.LENGTH_LONG).show(); // for testing
            ActivityCompat.requestPermissions(this, PERMISSION_ARRAY, LOCATION_PERMISSION_REQUEST_CODE);
        }else{
            openMap();
        }
    }

    private void openMap(){
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void openScanner(View view)
    {
        if(questProgress.getProgress() < questProgress.getQuest().getNumberOfClues()) {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String correctQr = questProgress.getClue(questProgress.getProgress()).getQrCode();
        if(result != null){
            if(result.getContents()!=null){
                if(result.getContents().equals(correctQr)){
                    Toast.makeText(this, "Correct QR Code",Toast.LENGTH_LONG).show();
                    questProgress.incrementProgress();
                    displayClue();
                }
                else{
                    Toast.makeText(this, "Incorrect QR Code",Toast.LENGTH_LONG).show();
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // check if request is granted and has required permissions
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openMap();
            } else {
                // Permission denied, show message.
                Toast.makeText(this, "Unfortunately, without location permission you can not use the map", Toast.LENGTH_LONG).show();
            }
            return;
        }
    }
}
