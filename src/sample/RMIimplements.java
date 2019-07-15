package sample;

public class RMIimplements implements RMIinterface {
    @Override
    public void updateHeartbeat(int heartbeat) {
        System.out.println("heartbeat received: " + heartbeat);
    }

    @Override
    public void updatePression(int pressions[]) {
        System.out.println("pressions received. MIN: " + pressions[0] + " MAX: " + pressions[1]);
        //System.exit(0);

    }

    @Override
    public void updateTemperature(int temp) {
        System.out.println("temperature received: " + temp);


    }
}
