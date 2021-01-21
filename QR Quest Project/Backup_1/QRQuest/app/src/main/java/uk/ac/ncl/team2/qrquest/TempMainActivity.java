package uk.ac.ncl.team2.qrquest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

public class TempMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_main);

        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText("Hi there.\n" +
                "This is a test main function.\n" +
                "Replace with the appropiate activity.\n" +
                "There are " + getIntent().getIntExtra("NoOfCredentials", -1) + " logins");

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_temp_main);
        layout.addView(textView);
    }
}
