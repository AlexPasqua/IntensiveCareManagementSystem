package sample;

import com.sun.net.httpserver.Headers;
import com.sun.source.tree.Tree;

import javax.xml.crypto.Data;
import java.io.*;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class MonitoringSystem {

    private static ArrayList<Integer> openHeartBeats;
    private static ArrayList<Integer[]> openPressures;
    private static ArrayList<Integer> openTemps;
    private static ArrayList<Patient> patients = new ArrayList<>();

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

        RMIinterface server = null;
        try {
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

        while (true){
            long timestamp = new Date().getTime();
            timestamp = timestamp / 60000;

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


            } catch (IOException | ClassNotFoundException e) {
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

    private static Map<Patient, HeartBeat> getHeartbeats() throws IOException, ClassNotFoundException {
        Map<Patient, HeartBeat> heartbeats = new TreeMap<Patient, HeartBeat>();

        /*
        * for every patient
        * if is a new patient add it to patinetindx (with a rand number)
        * then, get his index and use it for get data from openH health data
        */
        readPatients();
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

    private static Map<Patient, Temperature> getTemperatures() throws IOException, ClassNotFoundException {
        Map<Patient, Temperature> temperatures = new TreeMap<Patient, Temperature>();

        /*
         * for every patient
         * if is a new patient add it to patinetindx (with a rand number)
         * then, get his index and use it for get data from openH health data
         */
        readPatients();
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

    private static Map<Patient, Pressure> getPressures() throws IOException, ClassNotFoundException {
        Map<Patient, Pressure> pressures = new TreeMap<Patient, Pressure>();
        readPatients();
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

    private static void initHealthData() throws IOException, ClassNotFoundException {
        FileInputStream in = new FileInputStream("healthData");
        ObjectInputStream stream = new ObjectInputStream(in);
        openHeartBeats = (ArrayList<Integer>) stream.readObject();
        openTemps = (ArrayList<Integer>) stream.readObject();
        openPressures = (ArrayList<Integer[]>) stream.readObject();
        System.out.println("Health Data Loaded!");


    }

    private static void readPatients() throws IOException, ClassNotFoundException {
        /*
        * read from datastore file patients
         */
        FileInputStream in = new FileInputStream("datastore");
        ObjectInputStream stream = new ObjectInputStream(in);
        patients = (ArrayList<Patient>) stream.readObject();
    }

}
