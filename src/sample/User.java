package sample;

public interface User {
    public void addPatient(Patient current);
    public void addAdministration(Patient pat, Prescription presc, Integer dose, String notes);
    public String toString();
}
