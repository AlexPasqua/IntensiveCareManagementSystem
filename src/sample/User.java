package sample;

import java.io.Serializable;

public class User implements Serializable {

    private final String name;
    private final Integer powers; // 0=infermiere, 1=dottore
    private final String username;
    private final String password;

    public User(String name, Integer powers, String username, String password){
        this.name = name;
        this.powers = powers;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", powers=" + powers + "]";
    }
}
