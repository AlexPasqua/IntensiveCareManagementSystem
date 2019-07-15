package sample;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface RMIinterface extends Remote{
    public void updateHeartbeats(Map<Patient, HeartBeat> heartbeats) throws RemoteException;
    public void updatePressures(Map<Patient, Pressure> pressures) throws RemoteException;
    public void updateTemperatures(Map<Patient, Temperature> temps) throws RemoteException;

}
