package sample;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;

public class MonitoringSystem {

    public static void main (String[] args){


        RMIinterface server = null;
        try {
            server = (RMIinterface) Naming.lookup("rmi://localhost/RMIserver");
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        System.out.println("Monitoring System started.");

        while (true){
            long timestamp = new Date().getTime();
            timestamp = timestamp / 60000;

            try {
                if (timestamp % 2 == 0){
                    server.updatePression(getPression());
                }
                if (timestamp % 3 == 0){
                    server.updateTemperature(getTemperature());
                }
                if (timestamp % 5 == 0){
                    server.updateHeartbeat(getHeartbeat());
                }


            } catch (RemoteException e) {
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

    private static int getHeartbeat() {
        return 72;
    }

    private static int getTemperature() {
        return 37;
    }

    private static int[] getPression() {
        int[] pressions = {80, 120};
        return pressions;
    }


}
