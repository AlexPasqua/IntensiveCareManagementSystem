package sample;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String surname;
    private String username;
    private String password;
    private UserType type;


    public User(String name, String surname, String username, String password, UserType type){
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public boolean isValid(String username, String password){
        return username.equals(this.username) && password.equals(this.password);
    }

    public UserType getUserType(){
        return type;
    }

    public String getCompleteName(){
        return ("Dr." + this.surname + " " + this.name);
    }

    @Override
    public String toString(){
        return getClass().getSimpleName()+" [name=" + name + ", surname=" + surname + ", username=" + username + ", password=" + password + ", type=" + type + "]";
    }
}
