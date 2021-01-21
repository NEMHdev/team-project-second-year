package uk.ac.ncl.team2.qrquest;

import java.io.Serializable;

/**
 * Created by James Charsley on 08/02/2017.
 * Class containing information about a user.
 */
public class User implements Serializable{
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }
}
