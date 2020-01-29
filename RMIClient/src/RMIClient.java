import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    public static void main(String[] args) {
        try {
            Thread t = new Thread() {
                public void run() {
                    RMIFrontEnd.main(new String[]{"2030"});
                    try {
                        Thread.sleep(3000);
                        RMIServer.main(new String[]{"2025"});
                        Thread.sleep(500);
                        RMIServer.main(new String[]{"2026"});
                        Thread.sleep(500);
                        RMIServer.main(new String[]{"2027"});
                        Thread.sleep(500);

                        //RMIFrontEnd.main(new String[]{"2030"});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    PlacesListInterface plm;

                    try {
                        Thread.sleep(1000);
                        plm = (PlacesListInterface) Naming.lookup("rmi://localhost:2030/frontend");
                        Thread.sleep(1000);
                        Place p1 = new Place("3515", "Viseu");
                        plm.addPlace(p1);
                        Thread.sleep(3000);

                        checkPlacesListOnServer("2025");
                        checkPlacesListOnServer("2026");
                        checkPlacesListOnServer("2027");

                        Place x = plm.getPlace("3515");
                        System.out.println(x);

                        Thread.sleep(30000);
                  //      Registry r = LocateRegistry.getRegistry(2027);
                     //   r.unbind("placelist");

                        plm = (PlacesListInterface) Naming.lookup("rmi://localhost:2030/frontend");
                        Thread.sleep(1000);
                        p1 = new Place("3510", "Coimbra");
                        plm.addPlace(p1);
                        Thread.sleep(3000);

                        checkPlacesListOnServer("2025");
                        checkPlacesListOnServer("2026");
                        checkPlacesListOnServer("2027");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();
        } catch (Exception e) {
            System.out.println("Problemas de Comunicação: " + e.getMessage());
        }
    }

    public static void checkPlacesListOnServer(String port) {
        PlacesListInterface plm;
        try {
            System.out.println("---------------------------------------------");
            System.out.println("Objects on Server [" + port + "]");
            plm = (PlacesListInterface) Naming.lookup("rmi://localhost:" + port + "/placelist");
            Thread.sleep(1000);
            plm.allPlaces().forEach(place -> {
                System.out.println(place.toString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}