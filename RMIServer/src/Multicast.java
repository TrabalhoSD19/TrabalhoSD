import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class Multicast {
    String grupo = "230.0.0.0";
    int port =4446;

    public void send(String object) {
        try {
            InetAddress	group = InetAddress.getByName(grupo);
            MulticastSocket multicastSocket =new MulticastSocket(port);
            multicastSocket.joinGroup(group);
            byte[] sendBuf = object.getBytes();
            DatagramPacket resposta_order = new DatagramPacket(sendBuf, sendBuf.length, group, port);
            multicastSocket.send(resposta_order);
            multicastSocket.send(resposta_order);
            multicastSocket.close();

            System.out.println("Sending: " + object + " - " + port);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String receive() {
        try {
            InetAddress	group = InetAddress.getByName(grupo);
            MulticastSocket multicastSocket =new MulticastSocket(port);
/*
            //WINDOWS!!!
            multicastSocket.joinGroup(group);
*/
            //iOS INICIO
            SocketAddress socketAddress = new InetSocketAddress(group, port);
            NetworkInterface networkInterface = NetworkInterface.getByName("en0");
            multicastSocket.joinGroup(socketAddress, networkInterface);
            //iOs FIM

            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            multicastSocket.receive(packet);
            String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());

            return msg;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }
}