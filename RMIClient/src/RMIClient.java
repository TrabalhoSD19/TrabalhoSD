import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIClient {
    public static void main(String[] args) {
        try{
            Thread t = new Thread() {
                public void run() {
                    RMIServer.main(new String[]{"2025"});
                    RMIServer.main(new String[]{"2026"});
                    RMIServer.main(new String[]{"2027"});

                    RMIFrontEnd.main(new String[]{"2027"});

                    /*
                    try {
                        Thread.sleep(1000);

                        Multicast mu = new Multicast();
                        mu.send("Olá");
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                     */
                }
            };
            t.start();
        } catch(Exception e) { System.out.println("Problemas de Comunicação" + e.getMessage()); }
    }
}