import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIClient {
    public static void main(String[] args) {
        try{
            Thread t = new Thread() {
                public void run() {
                    PlacesListInterface l1 = null;

                    //RMIRegistry.main(new String[0]);
                    //RMIReplicaManager.main(new String[0]);
                    RMIServer.main(new String[]{"2025"});
                    RMIServer.main(new String[]{"2025"});
                    RMIServer.main(new String[]{"2025"});

                    try {
                        Thread.sleep(1000); // garante que todos os serviços estão disponíveis antes de executar o código do cliente

                    /*
                    System.out.println("Localizar servidor de Objetos...");
                    l1  = (PlacesListInterface) Naming.lookup("rmi://localhost:2025/placelist");

                    Place p = new Place("3510", "Viseu");
                    System.out.println("Invocar addPlace() ...");

                        l1.addPlace(p);


                    System.out.println("Obter o endereço do servidor no Registry() ...");
                    ObjectRegistryInterface l2;
                    PlacesListInterface l3;

                        l2 = (ObjectRegistryInterface) Naming.lookup("rmi://localhost:2023/registry");
                        String address = l2.resolve("3510");
                        System.out.println("Address: " + address);

                        System.out.println("Invocar getPlace() no servidor de objetos...");
                        //erro!
                        l3  = (PlacesListInterface) Naming.lookup("rmi://localhost:2025/placelist");
                        //assertEquals("Viseu", l3.getPlace("3510").getLocality());
                        System.out.println(l3.getPlace("3510").getLocality());
                        Thread.sleep(1000);
*/

                        Multicast mu = new Multicast();

                        mu.send("Olá");
                        Thread.sleep(1000);
                    /*
                    } catch (MalformedURLException | NotBoundException e) {
                    // fail(e.getCause());
                    System.out.println(e.getCause());
                }*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }; t.start();
        } catch(Exception e) { System.out.println("Problemas de Comunicação\n" + e.getMessage()); }
    }
}