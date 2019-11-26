import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.UUID;

public class PlaceManager extends UnicastRemoteObject implements PlacesListInterface {
    private static final long serialVersionUID = 1L;

    private static ArrayList<Place> array = new ArrayList<Place>();

    public PlaceManager(int port) throws RemoteException, InterruptedException {
        UUID uuid = UUID.randomUUID();

        Thread u;
        u = new Thread() {
            public void run() {
                int count = 1;
                while (true) {
                    try {
                        Thread.sleep(10000);
                        Multicast mu = new Multicast();
                        mu.send("Olá, o meu id é:"+ uuid + "e passei aqui pela" + count + "ª vez" );

                        Thread.sleep(10000);
                        count ++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        u.start();
        Thread.sleep(10000);

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