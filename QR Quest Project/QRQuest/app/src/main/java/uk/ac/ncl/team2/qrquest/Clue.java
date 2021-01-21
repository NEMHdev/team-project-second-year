package uk.ac.ncl.team2.qrquest;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by James Charsley on 08/02/2017.
 * Class containing information about each clue.
 */
public class Clue implements Serializable{

    private String name;
    private String clue;

    //TODO Change to required data type(s) and names for gps/maps
    private int xCoordinate;
    private int yCoordinate;

    //TODO Change to required data type for QR scanner
    private String qrCode;

    public Clue(String name, String clue, int x, int y, String qrCode){
        this.name = name;
        this.clue = clue;
        xCoordinate = x;
        yCoordinate = y;
        this.qrCode = qrCode;
    }

    public String getName(){
        return name;
    }

    public String getClue(){
        return clue;
    }

    public int getXCoordinate(){
        return xCoordinate;
    }

    public int getYCoordinate(){
        return yCoordinate;
    }

    public String getQrCode(){
        return qrCode;
    }
}
