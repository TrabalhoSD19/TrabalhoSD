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
        UUID uuid = UUID.randomUUID();
        try {
            PlacesListInterface placeList = new PlaceManager(Integer.parseInt(args[0]));
            System.out.println("Place server ready - " + args[0]);
        } catch(Exception e) {
            System.out.println("Place server main " + e.getMessage());
        }
        Thread t;
        t = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000);
                        Multicast mu = new Multicast();
                        mu.send("Olá, o meu id é:"+ uuid);
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();

    }
}

