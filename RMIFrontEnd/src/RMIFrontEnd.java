import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIFrontEnd {



    static Registry r = null;
    public synchronized static void main(String[] args) {
        // TODO Auto-generated method stub
        try{

            r = LocateRegistry.createRegistry(Integer.parseInt(args[0]));
        }catch(RemoteException a){
            a.printStackTrace();
        }

        try{
            FrontEnd frontEnd = new FrontEnd(Integer.parseInt(args[0]));

            r.rebind("frontend", frontEnd );

            System.out.println("FrontEnd server ready");
        }catch(Exception e) {
            System.out.println("FrontEnd server main " + e.getMessage());
        }

    }
}