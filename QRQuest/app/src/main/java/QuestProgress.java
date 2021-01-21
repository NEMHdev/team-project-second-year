package uk.ac.ncl.team2.qrquest;

import java.io.Serializable;

/**
 * Created by James Charsley on 08/02/2017.
 * Class containing information about a users progress through a quest.
 * Mainly contains the user, quest and number of completed clues.
 */
public class QuestProgress implements Serializable {
    private User user;
    private Quest quest;
    private int progress;

    public QuestProgress(User user, Quest quest, int progress){
        this.user = user;
        this.quest = quest;
        this.progress = progress;
    }

    public User getUser(){
        return user;
    }

    public int getProgress(){
        return progress;
    }

    public Quest getQuest(){
        return quest;
    }

    public Clue getClue(int index){
        return quest.getClue(index);
    }

    public void incrementProgress(){
        progress++;
    }


}
