package sample;

import java.io.*;
import java.util.ArrayList;

public class Main {

    private static ArrayList<Patient> pazienti = new ArrayList<>();
    public static ArrayList<Medicine> medicines = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Hello World!");

        //crea pazienti
        //pazienti.add(paziente1);

        try{
            read();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    // DATA STORE

    public static void write() throws IOException {
        FileOutputStream out = new FileOutputStream("pazienti");
        ObjectOutputStream s = new ObjectOutputStream(out);
        s.writeObject(pazienti);
        s.flush();
        System.out.println("Data Written!");
    }

    public static void read() throws IOException, ClassNotFoundException{
        FileInputStream in = new FileInputStream("pazienti");
        ObjectInputStream s = new ObjectInputStream(in);
        pazienti = (ArrayList<Patient>) s.readObject();
        System.out.println("Data Loaded!");
        System.out.println(pazienti.toString());
    }

    //
}
