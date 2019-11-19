import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIClient {
    public static void main(String[] args) {
        try{
            Thread t = new Thread() {
                public void run() {
                    RMIServer.main(new String[]{"1234"});
                    RMIServer.main(new String[]{"1235"});
                    RMIServer.main(new String[]{"1236"});

/*
                    try {
                        Thread.sleep(1000);

                        Multicast mu = new Multicast();
                        mu.send("Olá");
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                }
            };
            t.start();
        } catch(Exception e) { System.out.println("Problemas de Comunicação" + e.getMessage()); }
    }
}