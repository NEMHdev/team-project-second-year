package uk.ac.ncl.team2.qrquest;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by James Charsley on 08/02/2017.
 * Class contains information about a whole quest
 */
public class Quest implements Serializable{
    private String name;
    private List<Clue> clues;

    public Quest(String name, List<Clue> clues){
        this.name = name;
        this.clues = clues;
    }

    public String getName(){
        return name;
    }

    public Clue getClue(int index){
        if(index >= clues.size() || index < 0)
            throw new IllegalArgumentException("Index out of bounds");
        return clues.get(index);
    }

    public int getNumberOfClues(){
        return clues.size();
    }

    public void addClue(Clue clue){
        clues.add(clue);
    }

    public void removeClue(int index){
        if(index >= clues.size() || index < 0)
            throw new IllegalArgumentException("Index out of bounds");
        clues.remove(index);
    }
}
