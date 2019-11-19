import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class PlaceManager extends UnicastRemoteObject implements PlacesListInterface {
    private static final long serialVersionUID = 1L;

    private static ArrayList<Place> array = new ArrayList<Place>();

    public PlaceManager(int port) throws RemoteException {
        Thread t;
        t = new Thread() {
            public void run() {
            while (true) {
                Multicast mr = new Multicast();
                String response = mr.receive();
                System.out.println("Response: " + response + " - " + port);
            }
            }
        };
        t.start();
    }

    @Override
    public void addPlace(Place p) {
        array.add(p);
    }

    @Override
    public ArrayList<Place> allPlaces() {
        return array;
    }

    @Override
    public Place getPlace(String codigoPostal) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getPostalCode().equals(codigoPostal)) {
                return array.get(i);
            }
        }
        return null;
    }

}