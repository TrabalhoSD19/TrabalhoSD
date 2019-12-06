import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class PlaceManager extends UnicastRemoteObject implements PlacesListInterface {
    private static final long serialVersionUID = 1L;

    private static ArrayList<Place> array = new ArrayList<Place>();
    public static String[] args;
    public static List listA = new ArrayList();

    public <args> PlaceManager(int port) throws RemoteException, InterruptedException {

        String suuid = UUID.randomUUID().toString();
        /*Random rand = new Random();
        int rand_int = rand.nextInt(10);
*/
/*
        Thread u;
        u = new Thread() {
            public void run() {
                int count = 1;
                while (true) {
                    try {
                        Thread.sleep(10000);
                        Multicast mu = new Multicast();
                        mu.send("Olá, o meu id é:"+ uuid + "e passei aqui pela" + count + "ª vez" );

                        Thread.sleep(10000);
                        count ++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        u.start();*/

/*
        Thread t;
        t = new Thread() {
            public void run() {

            while (true) {
                Multicast mr = new Multicast();
                String response = mr.receive();
                System.out.println("Response: " + response + " - " + port);
            }
            }
        };
        t.start();
*/





        //heartbeat

        Thread i = new Thread(() -> {
            while (true) {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /* System.out.println("buum" + "_____:" + suuid);*/
                    Multicast mu = new Multicast();
                    mu.send("hearthbeat" + "," + suuid);

            }
        });
        i.start();

        Thread a = new Thread(() -> {

                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int tamanholista = listA.size();
                    int index_lista = tamanholista - 1;
                    Multicast mr = new Multicast();
                    String response = mr.receive();

                    String[] output = response.split(",");
                    if(output[0].equals("hearthbeat")){
                        if (output[1].equals(suuid)){
                           // System.out.println("o id é igual ao meu, logo nao vou adicionar À minha lista");
                        }else{
                            tamanholista = tamanholista-1;
                            for(;index_lista < tamanholista;index_lista++) {
                                if(listA.contains(suuid)){
                                  listA.add(suuid);
                              System.out.println(tamanholista);
                                    //System.out.println("adicionei À lista o id" + suuid);

                        }else{
                                //nao faz nada
                                }}}}
                    else {
                           // System.out.println("esta mensagem não é um hearthbeat");
                    }
                }
            });
        a.start();}
/*
        String phone = "012-3456789";
        String[] output = phone.split("-");
        System.out.println(output[0]);
        System.out.println(output[1]);*/







    @Override
    public void addPlace(Place p) {
        array.add(p);
    }

    @Override
    public ArrayList<Place> allPlaces() {
        return array;
    }

    @Override
    public Place getPlace(String codigoPostal) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getPostalCode().equals(codigoPostal)) {
                return array.get(i);
            }
        }
        return null;

    }

}