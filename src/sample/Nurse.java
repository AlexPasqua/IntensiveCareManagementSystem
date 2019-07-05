package sample;

import java.io.Serializable;

public class Nurse implements User, Serializable {
    private String name;
    private String surname;
    private String username;
    private String password;

    public Nurse(String name, String surname, String username, String password){
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString(){
        return "Nurse [name=" + name + ", surname=" + surname + ", username=" + username + ", password=" + password + "]";
    }
}
