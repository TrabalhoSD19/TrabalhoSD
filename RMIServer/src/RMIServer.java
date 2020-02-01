import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class RMIServer {

    static Registry r = null;
    public synchronized static void main(String[] args) {
        // TODO Auto-generated method stub
        try{

            r = LocateRegistry.createRegistry(Integer.parseInt(args[0]));
        }catch(RemoteException a){
            a.printStackTrace();
        }

        try{
            PlaceMan placeList = new PlaceMan(Integer.parseInt(args[0]));
            r.rebind("placelist", placeList );

            System.out.println("Place server ready");
        }catch(Exception e) {
            System.out.println("Place server main " + e.getMessage());
        }

    }
}

