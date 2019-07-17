package sample;


import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MonitoringSystem {

    private static ArrayList<Integer> openHeartBeats;
    private static ArrayList<Integer[]> openPressures;
    private static ArrayList<Integer> openTemps;
    private static ArrayList<Patient> patients = new ArrayList<>();
    private static Map <String, Integer> allarms = Map.of("Aritmia", 1, "Tachicardia", 1, "Fibrillazione ventricolare", 3,
            "Ipertensione", 2, "Ipotensione", 2, "Ipertermia", 2, "Ipotermia", 2);

    private static Date endAlarmTimestamp;

    /*
    ** map patient, array of 3 elements
    ** - heartbeat indx
    ** - temp indx
    ** - pressure idx
     */
    private static Map<Patient, int[]> patientIndex = new TreeMap<Patient, int[]>();

    public static void main (String[] args) {

        //get openMHealth data
        try {
            initHealthData();
        } catch (IOException e) {
            System.out.println("Error opening OpenMHealth data" );
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error opening OpenMHealth data" );
            e.printStackTrace();
        }

        /*
        * RMI CLIENT
         */

        RMIinterface server = null;
        try {
            //get the addres of server side
            server = (RMIinterface) Naming.lookup("rmi://localhost/RMIserver");
        } catch (NotBoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("Monitoring System started.");

        /*
        * MAIN LOOP
         */
        while (true){
            //get update list of patient
            try {
                readPatients();
            } catch (IOException e) {
                System.out.println("Can't read datastore file");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                continue;
            } catch (ClassNotFoundException e) {
                System.out.println("Can't read datastore file");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                continue;
            }
            //get current timestamp in minute
            long timestamp = new Date().getTime();
            timestamp = timestamp / 60000;

            //allarm generation
            if (ThreadLocalRandom.current().nextInt(0,  2000 + 1) % 1 == 0){
                if (!isThereAlarm()) {
                    System.out.println("Invoking Alarm...");
                    int rand_number = ThreadLocalRandom.current().nextInt(0, allarms.keySet().size());
                    for (String event : allarms.keySet()) {
                        if (rand_number == 0) {
                            try {
                                server.allarm(patients.get(ThreadLocalRandom.current().nextInt(0, patients.size())), event, allarms.get(event));
                                //save when the alarm time out
                                endAlarmTimestamp = new Date(System.currentTimeMillis() + ((4 - allarms.get(event))*60000) + 20000);
                            } catch (RemoteException e) {
                                System.out.println("Error calling allarm SERVER RMI");
                            }
                            break;
                        }
                        rand_number--;
                    }
                }else{
                    System.out.println("There is already an alarm.. ");
                }
            }

            //call server methods for store data
            try {
                if (timestamp % 2 == 0){
                    server.updatePressures(getPressures());
                }
                if (timestamp % 3 == 0){
                    server.updateTemperatures(getTemperatures());
                }
                if (timestamp % 5 == 0){
                    server.updateHeartbeats(getHeartbeats());
                }

            } catch (IOException e) {
                System.out.println("Server unreachble.. closing");
                System.exit(0);
            }

            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isThereAlarm() {
        if (new Date().before(endAlarmTimestamp))
            return true;
        return false;
    }

    private static Map<Patient, HeartBeat> getHeartbeats() {
        Map<Patient, HeartBeat> heartbeats = new TreeMap<Patient, HeartBeat>();

        /*
        * for every patient
        * if is a new patient add it to patinetindx (with a rand number)
        * then, get his index and use it for get data from openH health data
        */
        for (Patient p: patients){
            if (!patientIndex.containsKey(p)){
                int rand_indx = ThreadLocalRandom.current().nextInt(0,  2000 + 1);
                int rand_array[] = {rand_indx, rand_indx, rand_indx};
                patientIndex.put(p, rand_array);
            }

            int indx[] = patientIndex.get(p);

            heartbeats.put(p, new HeartBeat(openHeartBeats.get(indx[0])));

            //increase indx for next time
            indx[0]++;

            //update value on map
            patientIndex.put(p, indx);
        }
        return heartbeats;
    }

    private static Map<Patient, Temperature> getTemperatures() {
        Map<Patient, Temperature> temperatures = new TreeMap<Patient, Temperature>();

        /*
         * for every patient
         * if is a new patient add it to patinetindx (with a rand number)
         * then, get his index and use it for get data from openH health data
         */
        for (Patient p: patients){

            if (!(patientIndex.containsKey(p))){
                int rand_indx = ThreadLocalRandom.current().nextInt(0,  2000 + 1);
                int rand_array[] = {rand_indx, rand_indx, rand_indx};
                patientIndex.put(p, rand_array);
            }

            int indx[] = patientIndex.get(p);

            temperatures.put(p, new Temperature(openTemps.get(indx[1])));

            //increase indx for next time
            indx[1]++;

            //update value on map
            patientIndex.put(p, indx);
        }
        return temperatures;
    }

    private static Map<Patient, Pressure> getPressures(){
        Map<Patient, Pressure> pressures = new TreeMap<Patient, Pressure>();
        /*
         * for every patient
         * if is a new patient add it to patinetindx (with a rand number)
         * then, get his index and use it for get data from openH health data
         */
        for (Patient p: patients){
            if (!patientIndex.containsKey(p)){
                int rand_indx = ThreadLocalRandom.current().nextInt(0,  2000 + 1);
                int rand_array[] = {rand_indx, rand_indx, rand_indx};
                patientIndex.put(p, rand_array);
            }

            int indx[] = patientIndex.get(p);

            pressures.put(p, new Pressure(openPressures.get(indx[2])[0], openPressures.get(indx[2])[1]));

            //increase indx for next time
            indx[2]++;

            //update value on map
            patientIndex.put(p, indx);
        }
        return pressures;
    }

    /*
    * INIT OPENM HEALTH DATA
     */
    private static void initHealthData() throws IOException, ClassNotFoundException {
        FileInputStream in = new FileInputStream("healthData");
        ObjectInputStream stream = new ObjectInputStream(in);
        openHeartBeats = (ArrayList<Integer>) stream.readObject();
        openTemps = (ArrayList<Integer>) stream.readObject();
        openPressures = (ArrayList<Integer[]>) stream.readObject();
        System.out.println("Health Data Loaded!");
        endAlarmTimestamp = new Date(System.currentTimeMillis() + 60000);
    }

    /*
     * read from datastore file patients
     */

    private static void readPatients() throws IOException, ClassNotFoundException {

        FileInputStream in = new FileInputStream("datastore");
        ObjectInputStream stream = new ObjectInputStream(in);
        patients = (ArrayList<Patient>) stream.readObject();
        for (Patient p: patients){
            if (!p.getHospitalization())
                patients.remove(p);
        }
    }

}
