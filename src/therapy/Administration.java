package therapy;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
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

    public Integer getMgDose() { return mgDose; }
    public Date getTimestamp() { return timestamp; }
    public String getMedicine() { return prescription.getMedicine(); }
    public String getNotes() {
        return notes;
    }

    SimpleStringProperty medicineProperty(){ return new SimpleStringProperty(prescription.getMedicine()); }
    SimpleStringProperty mgDoseProperty(){ return new SimpleStringProperty(mgDose.toString()); }

    SimpleStringProperty timestampProperty(){
        Format formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        SimpleStringProperty result = new SimpleStringProperty(formatter.format(timestamp));
        return result;
    }

    @Override
    public String toString() {
        return "Administration [prescription=" + prescription.toString() + ", mgDose=" + mgDose + ", notes=" + notes + ", timestamp=" + timestamp.toString() + "]";
    }
}
