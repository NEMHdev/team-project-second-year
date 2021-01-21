package uk.ac.ncl.team2.qrquest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.widget.TextView;

/**
 * TODO Replace with finished menus (Likely to consist of more than one activity)
 * Takes in User object from LoginActivity using intent.
 * Has to have a sign out button
 * Has to be able to start a quest from a list from online data storage
 *      Creates/Passes questProgress object to QuestActivity
 * Has to be able to create a new quest or edit a quest from online data storage
 *      Passes User object to the editor activity
 * Has to have button to get to options menu
 * Has to have button to get to help.
 */

/**
 * The main menu of the application. Allows the user to start a quest. Will later
 * allow user to edit a quest, go to options or look at help
 */
public class MainMenuActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("USER");
        TextView textViewWelcome = (TextView) findViewById(R.id.textView_Welcome);
        textViewWelcome.setText("Welcome " + user.getName());
    }

    /**
     * Sign out event listener. Will finish the activity and go back to login screen
     * @param view:View
     */
    public void signOut(View view)
    {
        finish();
    }

    /**
     * Start Quest event listener. Will take the first quest, create a new questProgress and
     * starts the quest
     * @param view:View
     */
    public void startQuest(View view)
    {
        Intent intent = new Intent(this, QuestActivity.class);
        Quest selectedQuest = DummyDatabase.quests.get(0);
        for(QuestProgress qp : DummyDatabase.questProgresses){
            if(qp.getUser() == user && qp.getQuest() == selectedQuest){
                intent.putExtra("QUEST", qp);
                startActivity(intent);
            }
        }
        QuestProgress quest = new QuestProgress(user, selectedQuest, 0);
        intent.putExtra("QUEST", quest);
        startActivity(intent);
    }
}
