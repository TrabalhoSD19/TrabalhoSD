import java.net.MalformedURLException;
        import java.rmi.Naming;
        import java.rmi.NotBoundException;
        import java.rmi.RemoteException;
        import java.rmi.server.UnicastRemoteObject;
        import java.sql.Timestamp;
        import java.util.*;

public class PlaceMan extends UnicastRemoteObject implements PlacesListInterface {
    private static final long serialVersionUID = 1L;

    private static ArrayList<Place> array = new ArrayList<Place>();
    public static String[] args;
    public static List listA = new ArrayList();

    public <args> PlaceMan(int port) throws RemoteException, InterruptedException {
        final String suuid = UUID.randomUUID().toString();
        final List listA = new ArrayList();
        ArrayList<Long> listB = new ArrayList();
        String lider = "2026";
        String newline = System.getProperty("line.separator");

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        listA.add(suuid);
        listB.add(timestamp.getTime());
        System.out.println("ID DO PORTO '" + port + "'   : " + suuid);
        System.out.println("TEMPO DO PORTO '" + port + "': " + timestamp.getTime());


        Thread i = new Thread(() -> {
            int numero = 0;
            while (numero < 10) {
                System.out.println(newline);
                System.out.println("/*************** START SENDING ***************/");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Multicast mu = new Multicast();
                mu.send("hearthbeat" + "," + suuid);

                Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());

                for(int j = 0; j < listB.size(); j++){
                    /*
                    System.out.println(port);
                    System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
                    System.out.println("Time: " + timestamp.getTime());
                    System.out.println("listB.get(j): " + listB.get(j));
                    System.out.println("(timestamp.getTime() - listB.get(j)): " + (timestamp.getTime() - listB.get(j)));
                    System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
                    */
                    if((timestamp2.getTime() - listB.get(j)) > 20000) {
                        System.out.println(timestamp2.getTime() + " - " + listB.get(j) + " > " + j);
                        listA.remove(j);
                        listB.remove(j);

                        System.out.println("Removi do porto '" + port + "' da lista na posição: " + j);
                    }
                }

                System.out.println(newline + "Port:            " + port + " " + newline + "Nº de loops:     " + numero + newline + "Lista dos ids:   " + listA + " " + newline + "Lista timestamp: " + listB);


                if(port == 2025)
                    numero++;
            }
        });

        i.start();

        Thread a = new Thread(() -> {
            int numero = 0;

            while (numero < 10) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int tamanholista = listA.size();
                //int index_lista = tamanholista - 1;
                Multicast mr = new Multicast();
                String response = mr.receive();
                String[] output = response.split(",");

                if(output[0].equals("hearthbeat")){
                    Timestamp timestamp3 = new Timestamp(System.currentTimeMillis());
                    listB.set(0, timestamp3.getTime());

                /*    System.out.println("|---------------------------------------------|");
                    System.out.println("Id: " + suuid + " : ");
*/
                    if(output[1].equals(suuid)){

                        //System.out.println(suuid + ": ");
                        //System.out.println("Não me vou adicionar a mim mesmo!");
                    }else{
                        if(!listA.contains(output[1])){
                            listA.add(output[1]);
                            listB.add(timestamp3.getTime());

                            System.out.println("Adicionei à lista o id " + output[1] + ". Pos: " + tamanholista + " - Porto: " + port);

                            if(tamanholista >= 2 && lider == "empty") {
                                System.out.println("O líder é: " + Collections.max(listA));
                            }
                        }else{

                            int pos = listA.indexOf(output[1]);
                            listB.set(pos, timestamp3.getTime());

                            System.out.println("Atualizei o porto: " + port + " na posição: " + pos + " || output[1]: " + output[1]);
                            /*
                            System.out.println(suuid + ": ");
                            System.out.println("A lista já contem o id " + output[1]);*/
                        }
                    }

                    //System.out.println("ListaA    : " + listA + newline + "Lista time: " + listB);
                    //test.set((String) Collections.max(listA));


                    if(port == 2025)
                        numero++;
                }
            }});
        a.start();
    }

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