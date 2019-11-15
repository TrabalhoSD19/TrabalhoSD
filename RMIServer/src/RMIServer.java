import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class RMIServer {
    public static void main(String args[]) {

        System.out.println("valor recebido" + args[0]);


        try {
            PlacesListInterface placeList = new PlaceManager(Integer.parseInt(args[0]));
            System.out.println("Place server ready");
        } catch(Exception e) {
            System.out.println("Place server main " + e.getMessage());
        }
    }
}