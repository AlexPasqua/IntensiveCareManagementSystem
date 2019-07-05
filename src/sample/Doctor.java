package sample;

public class Doctor extends Nurse {

    public Doctor(String name, String surname, String username, String password){
        super(name, surname, username, password);
    }

    public void setDiagnosis(Patient pat, String diagnosis){
        pat.setDiagnosis(diagnosis);
    }

    public void addPrescription(Patient pat, String medicine, Integer therapyDuration, Integer dailyDoses, Integer mgDose){
        pat.addPrescription(new Prescription(medicine, therapyDuration, dailyDoses, mgDose, this));
    }
}
