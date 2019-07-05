package sample;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class Patient implements Serializable {

    private final String cod;
    private final String name;
    private final String surname;
    private final Date date;
    private final String town;
    private final String diagnosis;
    private ArrayList<Administration> administrations;

    public Patient(String cod, String name, String surname, Date date, String town, String diagnosis) {
        this.cod = cod;
        this.name = name;
        this.surname = surname;
        this.date = date;
        this.town = town;
        this.diagnosis = diagnosis;
        this.administrations = new ArrayList<>();
    }


    @Override
    public String toString() {
        return "Patient [cod=" + cod + ", name=" + name + ", surname=" + surname + ", date=" + date.toString() + ", town=" + town + ", diagnosis=" + diagnosis + ", administrations=" + administrations.toString() + "]";
    }
}
