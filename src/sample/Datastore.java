package sample;

import java.io.*;
import java.util.ArrayList;

public class Datastore {

    private static ArrayList<Patient> patients = new ArrayList<>();
    private static ArrayList<User> users = new ArrayList<>();
    private static User activeUser = null;  //active user
    private static ArrayList<String> availMeds = new ArrayList<>();


    //write on file patients array
    public static void write() {
        try{
            FileOutputStream out = new FileOutputStream("datastore");
            ObjectOutputStream stream = new ObjectOutputStream(out);
            stream.writeObject(patients);
            stream.writeObject(users);
            stream.writeObject(availMeds);
            stream.flush();
            System.out.println("Data Written!");
        }
        catch (IOException e){
            System.out.println("IOException occurred in Datastore.write()=" + e.toString());
        }
    }

    //read from file patients array
    @SuppressWarnings("unchecked")
    public static void read() throws IOException, ClassNotFoundException{
        FileInputStream in = new FileInputStream("datastore");
        ObjectInputStream stream = new ObjectInputStream(in);
        patients = (ArrayList<Patient>) stream.readObject();
        users = (ArrayList<User>) stream.readObject();
        availMeds = (ArrayList<String>) stream.readObject();
        System.out.println("Data Loaded!");
        System.out.println(patients.toString());
        System.out.println(users.toString());
        System.out.println(availMeds.toString());
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

    public static ArrayList<String> getAvailMeds() {
        return availMeds;
    }

    public static void addMed(String med) {
        if (!availMeds.contains(med))
            availMeds.add(med);
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