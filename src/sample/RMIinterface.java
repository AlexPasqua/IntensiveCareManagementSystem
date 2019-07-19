package sample;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface RMIinterface extends Remote{
    /*
     * RMI INTERFACE
     * rmi functions declaration
     */
    void updateHeartbeats(Map<Patient, HeartBeat> heartbeats) throws RemoteException;
    void updatePressures(Map<Patient, Pressure> pressures) throws RemoteException;
    void updateTemperatures(Map<Patient, Temperature> temps) throws RemoteException;
    void allarm(Patient patient, String event, int severity) throws RemoteException;

}
