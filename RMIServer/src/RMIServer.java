import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;
import java.util.UUID;


public class RMIServer {
    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;

    public void multicast(String multicastMessage) throws IOException {
        socket = new DatagramSocket();
        group = InetAddress.getByName("230.0.0.0");
        buf = multicastMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
        socket.send(packet);
        socket.close();
    }

    public static void main(String args[]) {

        try {
            PlacesListInterface placeList = new PlaceManager(Integer.parseInt(args[0]));
            System.out.println("Place server ready - " + args[0]);


            Thread t;
            t = new Thread() {
                public void run() {
                    int x = 0;
                    while (true) {
                        x++;
                        System.out.println(x);
                        Multicast mu = new Multicast();
                        mu.send("Olá");
                    }
                }
            };
            t.start();
        } catch(Exception e) {
            System.out.println("Place server main " + e.getMessage());
        }


    }
}

