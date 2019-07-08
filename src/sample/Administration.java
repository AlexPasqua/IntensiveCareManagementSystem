package sample;

import java.io.Serializable;
import java.util.Date;

public class Administration implements Serializable{

    private final Prescription prescription;
    private final Integer mgDose;
    private final String notes;
    private final Date timestamp;

    public Administration(Prescription prescription, Integer mgDose, String notes) {
        this.prescription = prescription;
        this.mgDose = mgDose;
        this.notes = notes;
        this.timestamp = new Date();
    }

    @Override
    public String toString() {
        return "Administration [prescription=" + prescription.toString() + ", mgDose=" + mgDose + ", notes=" + notes + ", timestamp=" + timestamp.toString() + "]";
    }
}
