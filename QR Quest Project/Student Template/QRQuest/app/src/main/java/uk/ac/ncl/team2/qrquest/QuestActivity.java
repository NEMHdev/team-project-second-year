package uk.ac.ncl.team2.qrquest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

    public void openMap(View view)
    {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void openScanner(View view)
    {
        if(questProgress.getProgress() < questProgress.getQuest().getNumberOfClues()) {
            String correctQR = questProgress.getClue(questProgress.getProgress()).getQrCode();
            Intent intent = new Intent(this, QrScannerActivity.class);
            intent.putExtra("CORRECT_QR", correctQR);
            startActivityForResult(intent, SCAN_REQUEST);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == SCAN_REQUEST && resultCode == RESULT_OK){
            questProgress.incrementProgress();
            displayClue();
        }
    }
}
