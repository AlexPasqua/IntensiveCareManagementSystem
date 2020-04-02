package therapy;

import java.io.Serializable;

// class that represents a user (nurse / doctor / ...)
public class User implements Serializable {
    private String name;
    private String surname;
    private String username;
    private String password;
    private UserType type;  //enum


    public User(String name, String surname, String username, String password, UserType type){
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.type = type;
    }

    // check if the "current" user is valid
    public boolean isValid(String username, String password){
        return (username.equals(this.username) && password.equals(this.password));
    }

    // UserType is an ENUM -> the method returns UserType.NURSE / UserType.DOCTOR etc...
    public UserType getUserType(){
        return type;
    }

    public String getCompleteName(){
        if (type == UserType.CHIEFDOCTOR || type == UserType.DOCTOR)
            return ("Dr." + this.surname + " " + this.name);
        return ("Inf." + this.surname + " " + this.name);
    }

    public boolean compareUsername(String username){
        return username.trim().equalsIgnoreCase(this.username.trim());
    }

    @Override
    public String toString(){
        return getClass().getSimpleName()+" [name=" + name + ", surname=" + surname + ", username=" + username + ", password=" + password + ", type=" + type + "]";
    }
}
