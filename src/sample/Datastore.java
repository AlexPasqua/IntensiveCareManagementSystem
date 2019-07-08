package sample;

import java.io.*;
import java.util.ArrayList;

public class Datastore {

    //patients array
    private static ArrayList<Patient> patients = new ArrayList<>();
    //users array
    private static ArrayList<User> users = new ArrayList<>();
    //active user
    private static User activeUser = null;


    //write on file patients array
    public static void write() throws IOException {
        FileOutputStream out = new FileOutputStream("patients");
        ObjectOutputStream s = new ObjectOutputStream(out);
        s.writeObject(patients);
        s.flush();
        System.out.println("Data Written!");
    }

    //read from file patients array
    @SuppressWarnings("unchecked")
    public static void read() throws IOException, ClassNotFoundException{
        FileInputStream in = new FileInputStream("patients");
        ObjectInputStream s = new ObjectInputStream(in);
        patients = (ArrayList<Patient>) s.readObject();
        System.out.println("Data Loaded!");
        System.out.println(patients.toString());
    }

    //return active user
    public static User getActiveUser(){
        return activeUser;
    }

    //set active user
    public static void setActiveUser(User user){
        activeUser = user;
    }

    //get patients list
    public static ArrayList<Patient> getPatients(){
        return patients;
    }

    //get users list
    public static ArrayList<User> getUsers(){
        return users;
    }

    //add patient
    public static void addPatient(Patient patient){
        patients.add(patient);
    }

    //discharge patient
    public static void dischargePatient(Patient patient){
        for (Patient p: patients)
            if (p.equals(patient)) {
                p.setHospitalization(false);
                return;
            }
    }

    //add user
    public static void addUser(User user){
        users.add(user);
    }
}
