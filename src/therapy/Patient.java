package therapy;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import therapy.controller.GUI;
import therapy.controller.HomeController;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/* class that represents a patient */
public class Patient implements Serializable {
    private final String cod;
    private final String name;
    private final String surname;
    private final LocalDate birthDate;
    private final String birthTown;
    private String diagnosis = "";
    private ArrayList<Prescription> prescriptions;
    private ArrayList<Administration> administrations;
    private ArrayList<HeartBeat> heartBeats = new ArrayList<>();
    private ArrayList<Temperature> temperatures = new ArrayList<>();
    private ArrayList<Pressure> pressures = new ArrayList<>();
    private boolean hospitalized;
    private String dischargeLetter = "";


    public Patient(String cod, String name, String surname, LocalDate birthDate, String birthTown) {
        this.cod = cod;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.birthTown = birthTown;
        this.prescriptions = new ArrayList<>();
        this.administrations = new ArrayList<>();
        this.hospitalized = true;
    }

    public void addPrescription(Prescription current){ prescriptions.add(current); }

    public void addAdministration(Administration current){ administrations.add(current); }

    public void addHeartBeat(HeartBeat heartbeat){ heartBeats.add(heartbeat); }

    public void addTemperature(Temperature temperature) { temperatures.add(temperature); }

    public void addPressure(Pressure pressure) { pressures.add(pressure); }

    public void setDiagnosis(String diagnosis){ this.diagnosis = diagnosis; }

    public String getDiagnosis() { return diagnosis; }

    public void setHospitalization(boolean state){ hospitalized = state; }

    public String getFullName(){ return (surname + " " + name); }

    public String getCodFis(){ return cod; }

    public LocalDate getDate(){ return birthDate; }

    public String getBirthTown(){ return birthTown; }

    public LocalDate getBirthDate(){ return birthDate; }

    public boolean getHospitalization(){ return hospitalized; }

    public ArrayList<Prescription> getPrescriptions(){ return prescriptions; }

    public ArrayList<Administration> getAdministrations() { return administrations; }

    public ArrayList<HeartBeat> getHeartBeats() { return heartBeats; }

    public ArrayList<Temperature> getTemperatures() { return temperatures; }

    public ArrayList<Pressure> getPressures() { return pressures; }

    public void setDischargeLetter(String letter){ dischargeLetter = letter; }

    public String getDischargeLetter() { return dischargeLetter; }

    public void clearClinicalData(){
        heartBeats.clear();
        pressures.clear();
        temperatures.clear();
    }

    @Override
    public boolean equals(Object other){
        return (other instanceof Patient) && (cod.equals(((Patient)other).cod)) && (name.equals(((Patient)other).name)) &&
                (surname.equals(((Patient)other).surname)) && (birthDate.equals(((Patient)other).birthDate)) &&
                (birthTown.equals(((Patient)other).birthTown)) && (hospitalized == ((Patient)other).hospitalized);
    }

    @Override
    public String toString() {
        return "Patient [cod=" + cod + ", name=" + name + ", surname=" + surname + ", birthDate=" + birthDate.toString() + ", birthTown=" +
                birthTown + ", diagnosis=" + diagnosis + ", hospitalized=" + hospitalized + ", dischargeLetter=" + dischargeLetter + ", prescription=" +
                prescriptions.toString() + ", administrations=" + administrations.toString() + ", heartBeats=" + heartBeats.toString() + ", temperatures=" +
                temperatures.toString() + ", pressures=" + pressures.toString() + "]";
    }

    /*
    * Generate a random index for the open health data
    * take data from the index and put it in patient data array
    * To cover one week we need:
    * 5040 pressure values
    * 3360 temp values
    * 2016 heartbeat values
     */
    public void generateFakeData(){
        clearClinicalData();
        ArrayList<Integer> openHeartBeats = null;
        ArrayList<Integer> openTemps = null;
        ArrayList<Integer[]> openPressures = null;
        int rand_indx = ThreadLocalRandom.current().nextInt(0,  4000 + 1);

        FileInputStream in;
        ObjectInputStream stream;
        try {
            in = new FileInputStream("healthData");
            stream = new ObjectInputStream(in);
            openHeartBeats = (ArrayList<Integer>) stream.readObject();
            openTemps = (ArrayList<Integer>) stream.readObject();
            openPressures = (ArrayList<Integer[]>) stream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            GUI.showDialog(Alert.AlertType.WARNING, "Warning", "Impossibile accedere ai dati passati");
        }

        int heartbeatsIndx = rand_indx;
        int tempsIndx = rand_indx;
        int pressuresIndx = rand_indx;

        long current = new Date().getTime();
        long start = current - 604800000; //una settimana indietro

        //scorro da adesso a una settimana fa
        for(  ; current>start; current-=60000){
            long thiscurrent = current /60000;
            if (thiscurrent % 5 == 0)
                //System.out.println("");
                heartBeats.add(new HeartBeat(openHeartBeats.get(heartbeatsIndx++), current));
            if (thiscurrent % 3 == 0)
                //System.out.println("");
                temperatures.add(new Temperature(openTemps.get(tempsIndx++), current));
            if (thiscurrent % 2 == 0)
                //System.out.println("");
                pressures.add(new Pressure(openPressures.get(pressuresIndx)[0], openPressures.get(pressuresIndx++)[1], current));
        }
        //sistemo
        Collections.reverse(heartBeats);
        Collections.reverse(temperatures);
        Collections.reverse(pressures);

        //updating all graphs
        for(Map.Entry<String, FXMLLoader> entry: Datastore.allLoaders.entrySet()){
            if (entry.getKey().equals("dashboard")){
                HomeController controller = entry.getValue().getController();
                controller.reset();
                controller.loadList();
            }
        }
    }
}
