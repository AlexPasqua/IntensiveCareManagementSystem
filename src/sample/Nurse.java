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

    public boolean isValid(String username, String password){
        return username.equals(this.username) && password.equals(this.password);
    }


    public void addPatient(Patient current){
        if (!Datastore.getPatients().contains(current))
            Datastore.getPatients().add(current);

        //TODO: in caso reagisci al fatto che il paziente che stai inserendo esiste già
    }

    public void addAdministration(Patient pat, Prescription presc, Integer dose, String notes){
        pat.addAdministration(new Administration(presc, dose, notes));
    }

    @Override
    public String toString(){
        return "Nurse [name=" + name + ", surname=" + surname + ", username=" + username + ", password=" + password + "]";
    }
}