package sample;

import java.io.*;
import java.util.ArrayList;

public class Datastore {

    private static ArrayList<Patient> patients = new ArrayList<>();
    private static ArrayList<User> users = new ArrayList<>();
    private static User activeUser = null;  //active user


    //write on file patients array
    public static void write() throws IOException {
        FileOutputStream out = new FileOutputStream("datastore");
        ObjectOutputStream stream = new ObjectOutputStream(out);
        stream.writeObject(patients);
        stream.writeObject(users);
        stream.flush();
        System.out.println("Data Written!");
    }

    //read from file patients array
    @SuppressWarnings("unchecked")
    public static void read() throws IOException, ClassNotFoundException{
        FileInputStream in = new FileInputStream("datastore");
        ObjectInputStream stream = new ObjectInputStream(in);
        patients = (ArrayList<Patient>) stream.readObject();
        users = (ArrayList<User>) stream.readObject();
        System.out.println("Data Loaded!");
        System.out.println(patients.toString());
        System.out.println(users.toString());
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

    //get logged user power
    /*public static UserType getActivePower(){
        if (activeUser.getClass().getSimpleName().equals("Nurse"))
            return UserType.NURSE;
        else if (activeUser.getClass().getSimpleName().equals("Doctor"))
            return UserType.DOCTOR;
        else if (activeUser.getClass().getSimpleName().equals("ChiefDoctor"))
            return UserType.CHIEFDOCTOR;
        else
            return null;
    }*/
}