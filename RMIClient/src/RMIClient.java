
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;




public class RMIClient {
    public static void main(String[] args) {
        PlacesListInterface l1 = null;
        //ola


        try{
            Thread t = new Thread() {
                public void run() {
                    System.out.println("Olapasseiaqui");

                    RMIServer.main(new String[]{"2025"});
                    RMIServer.main(new String[]{"2026"});
                    RMIServer.main(new String[]{"2027"});
                }
            };
            t.start();
            Thread.sleep(1000); // garante que todos os serviços estão disponíveis antes de executar o código do cliente

            System.out.println("Localizar servidor de Objetos...");
            l1  = (PlacesListInterface) Naming.lookup("rmi://localhost:2022/placelist");

            Place p = new Place("3510", "Viseu");
            System.out.println("Invocar addPlace() ...");
            l1.addPlace(p);



        } catch(Exception e) { /*fail("Problemas de Comunicação\n" + e.getMessage()); */}
    }
}