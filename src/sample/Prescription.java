package sample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Prescription implements Serializable {
    public static ArrayList<String> availMeds = new ArrayList<>();

    private final String medicine;
    private final Integer therapyDuration; //in days
    private final Integer dailyDoses;
    private final Integer mgDose; //in mg
    private final User doctor;
    private final Date timestamp;

    
    public Prescription(String medicine, Integer therapyDuration, Integer dailyDoses, Integer mgDose, User doctor) {
        medicine = medicine.toLowerCase();

        if (!(availMeds.contains(medicine)))
            availMeds.add(medicine);

        this.medicine = medicine;
        this.therapyDuration = therapyDuration;
        this.dailyDoses = dailyDoses;
        this.mgDose = mgDose;
        this.doctor = doctor;
        this.timestamp = new Date();
    }

    @Override
    public String toString() {
        return "Prescription [medicine=" + medicine + ", therapyDuration=" + therapyDuration + ", dailyDoses=" + dailyDoses + ", mgDose=" + mgDose + ", doctor=" + doctor + ", timestamp=" + timestamp.toString() + "]";
    }
}
