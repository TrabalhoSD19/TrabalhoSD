import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface PlacesListInterface extends Remote{

    public void addPlace(Place p) throws RemoteException ;
    public ArrayList<Place> allPlaces() throws RemoteException;
    public Place getPlace(String cp) throws RemoteException ;
}
