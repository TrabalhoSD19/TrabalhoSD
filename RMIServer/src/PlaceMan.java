import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.*;

public class PlaceMan extends UnicastRemoteObject implements PlacesListInterface {
    private static final long serialVersionUID = 1L;
    private static final int msBetweenHB = 10000;


    ArrayList<Place> list = new ArrayList<Place>();
    Integer port;


    public PlaceMan(int port) throws RemoteException {
        this.port = port;
        Thread l = new Thread(() -> {
            listeningOnMulticast();
        });
        l.start();
        Thread s = new Thread(() -> {
            sendHearthBeats();
        });
        s.start();
    }

    private void listeningOnMulticast() {
        while (true) {
            try {
                Multicast multicast = new Multicast();
                String answer = multicast.receive();
                if (answer.split(";")[0].equals("add")) {
                    System.out.println("[" + port + "] Recebi: " + answer);
                    addPlaceWithoutReply(new Place(answer.split(";")[1], answer.split(";")[2]));
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void sendHearthBeats() {
        Multicast multicast = new Multicast();
        int cont = 0;

        while (true) {
            try {
                //System.out.println("["+port+"] Sending heathbeat");
                multicast.send("hb;" + port.toString() + ";" + System.currentTimeMillis());
                Thread.sleep(msBetweenHB);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            cont++;

            if (port == 2027 && cont == 2) { //Kill server on port 2027 after 2 iterations
                return;
            }
        }
    }


    @Override
    public void addPlace(Place p) {
        list.add(p);
        Multicast mu = new Multicast();
        mu.send("add;" + p.getPostalCode() + ';' + p.getLocality());
        System.out.println("[" + port + "] Enviei: " + p.getPostalCode() + ';' + p.getLocality());

    }

    @Override
    public void addPlaceWithoutReply(Place p) {
        for (Place place : list) {
            if (place.getPostalCode().equals(p.getPostalCode()))
                return;
        }

        list.add(p);
    }


    @Override
    public ArrayList<Place> allPlaces() {
        return list;
    }

    @Override
    public Place getPlace(String codigoPostal) {
        System.out.println("[" + port + "] Retriving place: " + codigoPostal);

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPostalCode().equals(codigoPostal)) {
                return list.get(i);
            }
        }
        return null;

    }

}