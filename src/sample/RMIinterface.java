package sample;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIinterface extends Remote{
    public void updateHeartbeat(int heartbeat) throws RemoteException;
    public void updatePression(int pressions[]) throws RemoteException;
    public void updateTemperature(int temp) throws RemoteException;

}
