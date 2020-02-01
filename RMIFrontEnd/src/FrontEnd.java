import javax.swing.plaf.TableHeaderUI;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FrontEnd extends UnicastRemoteObject implements PlacesListInterface {
    private static final long serialVersionUID = 1L;

    private static final int msBetweenHB = 20000;

    public Integer port;

    public Integer leader = 0;

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
//                System.out.println("[Front end] Recebi: "+answer);
                String[] answerSplited = answer.split(";");
                if (answerSplited[0].equals("itl")) {
                    leader=Integer.parseInt(answerSplited[1]);
                    System.out.println("[FRONT-END] New leader: "+ leader);

                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }


    @Override
    public void addPlace(Place p) {

        PlacesListInterface plm;
        try {
            plm = (PlacesListInterface) Naming.lookup("rmi://localhost:" + leader + "/placelist");
            Thread.sleep(100);
            plm.addPlace(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Place getPlace(String cp) throws RemoteException {
        Integer randomServer = 2025+(int) (Math.random() * ((leader - 2025) + 1));
        System.out.println(randomServer);
        PlacesListInterface plm;
        try {
            plm = (PlacesListInterface) Naming.lookup("rmi://localhost:" + randomServer + "/placelist");
            Thread.sleep(100);
            return plm.getPlace(cp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}