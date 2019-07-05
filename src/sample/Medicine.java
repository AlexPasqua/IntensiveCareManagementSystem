package sample;

import java.io.Serializable;

public class Medicine implements Serializable {

    private String name;
    public Medicine(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "Medicine [name=" + name + "]";
    }
}
