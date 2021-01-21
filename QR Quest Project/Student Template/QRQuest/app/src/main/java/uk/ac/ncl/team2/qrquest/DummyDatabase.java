package uk.ac.ncl.team2.qrquest;

import java.util.ArrayList;
import java.util.List;

/*
TODO Might have to use parcelable for User, Clue, Quest and QuestProgress
    private MyParcelable(Parcel in) mData = in.readInt();
    public int describeContents() return 0;
    public void writeToParcel(Parcel out, int flags) out.writeInt(mData);
    public static final Parcelable.Creator<MyParcelable> CREATOR
            = new Parcelable.Creator<MyParcelable>() {
        public MyParcelable createFromParcel(Parcel in) return new MyParcelable(in);
        public MyParcelable[] newArray(int size) return new MyParcelable[size];
    };
 */

/**
 * Created by James Charsley on 08/02/2017.
 * Temporary class containing test data for users and quests
 */
public class DummyDatabase {

    public static List<User> users = new ArrayList<User>();
    public static List<Quest> quests = new ArrayList<Quest>();
    public static List<QuestProgress> questProgresses = new ArrayList<QuestProgress>();

    public static void init(){
        users.add(new User("John Smith", "john.smith@test.com", "hello"));
        users.add(new User("Foo bar", "foo.bar@test.com", "foobar"));

        Quest quest1 = new Quest("quest1", new ArrayList<Clue>() );
        quest1.addClue( new Clue("clue1", "This is the first clue", 5, 5, "1234"));
        quest1.addClue( new Clue("clue2", "This is the second clue", 10, 10, "3456"));
        quest1.addClue( new Clue("clue3", "This is the third clue", 15, 15, "5678"));

        Quest quest2 = new Quest("quest2", new ArrayList<Clue>());
        quest2.addClue( new Clue("test1", "This is the first test", 3, 4, "9876"));
        quest2.addClue( new Clue("test2", "This is the second test", 6, 8, "7654"));
        quest2.addClue( new Clue("test3", "This is the third test", 9, 12, "5432"));

        quests.add(quest1);
        quests.add(quest2);
    }


}
