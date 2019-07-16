package sample;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface RMIinterface extends Remote{
    /*
     * RMI INTERFACE
     * rmi functions declaration
     */
    public void updateHeartbeats(Map<Patient, HeartBeat> heartbeats) throws RemoteException;
    public void updatePressures(Map<Patient, Pressure> pressures) throws RemoteException;
    public void updateTemperatures(Map<Patient, Temperature> temps) throws RemoteException;
    public void allarm(Patient patient, String event, int severity) throws RemoteException;

}
