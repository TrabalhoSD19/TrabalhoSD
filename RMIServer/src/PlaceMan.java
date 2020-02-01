import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class PlaceMan extends UnicastRemoteObject implements PlacesListInterface {
    private static final long serialVersionUID = 1L;
    private static final int msBetweenHB = 5000;

    Boolean flag =  true;

    ArrayList<Place> list = new ArrayList<Place>();
    Integer port;



    HashMap<String,Long> serversList=new HashMap<String,Long>();


    public PlaceMan(int port) throws RemoteException {
        this.port = port;
        Long currentTime=System.currentTimeMillis();
        serversList.put(String.valueOf(port),currentTime);

        Thread a = new Thread(() -> {
            listeningOnMulticast();
        });
        a.start();
        Thread t2 = new Thread(() -> {
            sendHearthbeats();
        });
        t2.start();
        /*Thread t3 = new Thread(() -> {
            {
                try{
                    while (true){

                        sleep(10000);
                        verifyWhoIsLeader();


                    }    }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        t3.start();*/

    }

    private synchronized  void  verifyWhoIsLeader(){
        HashMap<String,Long> x = new HashMap<>(serversList);

        for (Map.Entry me :x.entrySet()) {
            Long actualTimeStamp = System.currentTimeMillis();
            if (actualTimeStamp -  Long.parseLong(me.getValue().toString()) > 10000) {
                serversList.remove(me.getKey());
            }

            if (serversList.isEmpty()) {
                System.out.println("ERROR: No server is running!");
            }
            else{
                //if(Collections.max(serversList.keySet()).equals(Integer.toString(port))){
                    Multicast multicast = new Multicast();
                    // itl => im the leader
                    multicast.send("itl;" + Collections.max(serversList.keySet()));
                //}
            }
        }
    }

    private  void listeningOnMulticast() {
        int count = 0;
        while (flag) {
            try {
                Multicast multicast = new Multicast();
                String answer = multicast.receive();
                if (answer.split(";")[0].equals("add")) {
                    System.out.println("[" + port + "] Recebi: " + answer + "\u001B[0m");
                    addPlaceWithoutReply(new Place(answer.split(";")[1], answer.split(";")[2]));
                }
                if (answer.split(";")[0].equals("hb")) {
                    serversList.put(answer.split(";")[1],Long.parseLong(answer.split(";")[2]));
                }
                verifyWhoIsLeader();
                count++;

                if (port == 2027 && count == 5) { //Kill server on port 2027 after 2 iterations
                    System.out.println("[2027] is down!");
                    return;
                }

                for (Place x: list) {
                    System.out.println("ID:" + port + x.getLocality() + "/" + x.getPostalCode());
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }

    private void sendHearthbeats() {
        Multicast multicast = new Multicast();
        int count = 0;
        while (flag) {
            try {
                //multicast.send("hb;" + port.toString() + ";" + System.currentTimeMillis());
                multicast.send("hb;" + port.toString() + ";" + System.currentTimeMillis());
                sleep(msBetweenHB);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            count++;

            if (port == 2027 && count == 5) { //Kill server on port 2027 after 2 iterations
                System.out.println("[2027] is down!");
                flag = false;
                return;
            }
        }
    }


    @Override
    public void addPlace(Place p) {
        list.add(p);
        Multicast mu = new Multicast();
        mu.send("add;" + p.getPostalCode() + ';' + p.getLocality());
        System.out.println("[" + port + "] Enviei: " + p.getPostalCode() + ';' + p.getLocality() + "\u001B[0m");

    }

    @Override
    public void addPlaceWithoutReply(Place p) {
        for (Place place : list) {
            if (place.getPostalCode().equals(p.getPostalCode()))
                return;
        }

        list.add(p);
    }


    @Override
    public ArrayList<Place> allPlaces() {
        return list;
    }

    @Override
    public Place getPlace(String codigoPostal) {
        System.out.println( "[" + port + "] Retriving place: " + codigoPostal + "\u001B[0m");

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPostalCode().equals(codigoPostal)) {
                return list.get(i);
            }
        }
        return null;

    }

}