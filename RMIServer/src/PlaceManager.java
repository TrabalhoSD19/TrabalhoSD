import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class PlaceManager extends UnicastRemoteObject implements PlacesListInterface {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static ArrayList<Place> array = new ArrayList<Place>();

    public PlaceManager(int port) throws RemoteException {

        String copy_address = null;

        PlacesListInterface places;
        if (copy_address != null) {
            try {
                places =(PlacesListInterface) Naming.lookup(copy_address);
                array = places.allPlaces();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
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