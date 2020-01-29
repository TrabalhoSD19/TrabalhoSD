import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.*;

public class FrontEnd extends UnicastRemoteObject implements PlacesListInterface {
    private static final long serialVersionUID = 1L;

    private static final int msBetweenHB = 20000;

    public Integer port;
    ArrayList<Long> listA = new ArrayList();
    ArrayList<Long> listB = new ArrayList();

    public FrontEnd(int port) throws RemoteException {
        this.port = port;

        Thread t = new Thread() {
            public void run() {
                listeningHBOnMulticast();
            }
        };
        t.start();


    }


    private void listeningHBOnMulticast() {
        while (true) {
            try {
                Multicast multicast = new Multicast();
                String answer = multicast.receive();
//                System.out.println("["+port+"] Recebi: "+answer);
                String[] answerSplited = answer.split(";");
                if (answerSplited[0].equals("hb")) {
                    if (listA.contains(Long.parseLong(answerSplited[1]))) {
                        listB.set(listA.indexOf(Long.parseLong(answerSplited[1])), Long.parseLong(answerSplited[2]));
                    } else {
                        listA.add(Long.parseLong(answerSplited[1]));
                        listB.add(Long.parseLong(answerSplited[2]));
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }


    public Long getLeader() {
        for (int i = 0; i < listA.size(); i++) {
            Long actualTimeStamp = System.currentTimeMillis();
            if (actualTimeStamp - listB.get(i) > msBetweenHB) {
                System.out.println("server [" + listA.get(i) + "] is inactive!\n " +
                        "Last Call:\t\t" + new Date(listB.get(i)).toString() + "\n " +
                        "Actual time:\t " + new Date(actualTimeStamp).toString());

                listA.remove(i);
                listB.remove(i);
            }
        }
        if (listA.size() == 0) {
            System.out.println("ERROR: Don't exists any runnings server");
            return null;
        }
        System.out.println("The lider is [" + Collections.max(listA) + "]");
        return Collections.max(listA);
    }


    @Override
    public void addPlace(Place p) {

        PlacesListInterface plm;
        try {
            plm = (PlacesListInterface) Naming.lookup("rmi://localhost:" + getLeader() + "/placelist");
            Thread.sleep(100);
            plm.addPlace(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Place getPlace(String cp) throws RemoteException {
        Integer randomServer = (int) (Math.random() * ((listA.size() - 1 - 0) + 1)) + 0;
        System.out.println("Array size:" + listA.size() + "\tPosition: " + randomServer);

        PlacesListInterface plm;
        try {
            plm = (PlacesListInterface) Naming.lookup("rmi://localhost:" + listA.get(randomServer) + "/placelist");
            Thread.sleep(100);
            return plm.getPlace(cp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}