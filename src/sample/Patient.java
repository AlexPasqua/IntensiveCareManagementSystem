package sample;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Patient implements Serializable {

    private final String cod;
    private final String name;
    private final String surname;
    private final LocalDate birthDate;
    private final String birthTown;
    private String diagnosis;
    private ArrayList<Prescription> prescriptions;
    private ArrayList<Administration> administrations;
    private boolean hospitalized;

    public Patient(String cod, String name, String surname, LocalDate birthDate, String birthTown) {
        this.cod = cod;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.birthTown = birthTown;
        this.prescriptions = new ArrayList<Prescription>();
        this.administrations = new ArrayList<Administration>();
        this.hospitalized = true;
    }

    public void addPrescription(Prescription current){
        prescriptions.add(current);
    }

    public void addAdministration(Administration current){
        administrations.add(current);
    }

    public void setDiagnosis(String diagnosis){
        this.diagnosis = diagnosis;
    }

    public void setHospitalization(boolean state){ hospitalized = state; };

    public String getFullName(){ return (surname + " " + name); };

    public String getCodFis(){ return cod; };

    public LocalDate getDate(){ return birthDate; };

    public String getBirthTown(){ return birthTown; };

    public boolean getHospitalization(){ return hospitalized; };

    @Override
    public boolean equals(Object other){
        return (other instanceof Patient) && (cod.equals(((Patient)other).cod)) && (name.equals(((Patient)other).name)) &&
                (surname.equals(((Patient)other).surname)) && (birthDate.equals(((Patient)other).birthDate)) &&
                (birthTown.equals(((Patient)other).birthTown));
    }

    @Override
    public String toString() {
        return "Patient [cod=" + cod + ", name=" + name + ", surname=" + surname + ", birthDate=" + birthDate.toString() + ", birthTown=" + birthTown + ", diagnosis=" + diagnosis + ", administrations=" + administrations.toString() + "]";
    }
}
