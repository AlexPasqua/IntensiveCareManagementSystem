package sample;

import javafx.beans.property.SimpleStringProperty;
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Prescription implements Serializable {
    private final String medicine;
    private final Integer therapyDuration; //in days
    private final Integer dailyDoses;
    private final Integer mgDose; //in mg
    private final User doctor;
    private final Date timestamp;

    
    public Prescription(String medicine, Integer therapyDuration, Integer dailyDoses, Integer mgDose, User doctor) {
        this.medicine = medicine;
        this.therapyDuration = therapyDuration;
        this.dailyDoses = dailyDoses;
        this.mgDose = mgDose;
        this.doctor = doctor;
        this.timestamp = new Date();
    }

    public String getMedicine() {
        return medicine;
    }

    public Integer getTherapyDuration() {
        return therapyDuration;
    }

    public Integer getDailyDoses() {
        return dailyDoses;
    }

    public Integer getMgDose() {
        return mgDose;
    }

    public User getDoctor() {
        return doctor;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public SimpleStringProperty timestampProperty(){
        Format formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        SimpleStringProperty s = new SimpleStringProperty(formatter.format(timestamp));
        return s;
    }
    public SimpleStringProperty doctorProperty(){
        return new SimpleStringProperty(doctor.getCompleteName());
    }


    @Override
    public String toString() {
        return "Prescription [medicine=" + medicine + ", therapyDuration=" + therapyDuration + ", dailyDoses=" + dailyDoses + ", mgDose=" + mgDose + ", doctor=" + doctor + ", timestamp=" + timestamp.toString() + "]";
    }

    public String forComboBox(){
        return medicine + "<" + dailyDoses + " al giorno, " + mgDose + "mg a dose>";
    }
}
