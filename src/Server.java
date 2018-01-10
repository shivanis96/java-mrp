import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.security.*;
import javax.crypto.*;

public class Server {
    private int portNumber = 15882;
    ServerSocket serverSoc = null;
    //Each client has a socketManager and the ArrayList adds a socketManager for the particular client on the list
    ArrayList<socketManager> clients = null;

    public Server(int port) {
        if (port > 2048) {
            portNumber = port;
        } else {
            System.err.println("Port number too low, defaulting to 15882");
        }
        //A new thread is created so new code does not block the main code. So the server can run on the side
        ServerHandler sh = new ServerHandler(portNumber);
        Thread shThread = new Thread(sh);
        shThread.start();
        //Allows user to quit by inputing 9
        System.out.println("Please enter 'Quit' to close the server");
        Scanner scan2 = new Scanner(System.in);
        String scanned = scan2.nextLine();
        if (scanned == "QUIT") {
            sh.kill();
            System.out.println("Goodbye!");
        }

    }

    //Main Method:- called first when running the class file.
    public static void main(String[] args) {
        System.out.println("Enter server port number: ");
        //User input of port number
        Scanner scan = new Scanner(System.in);
        int port = scan.nextInt();
        //Creating a new server object and starting it
        Server server = new Server(port);
    }
}

class ServerHandler implements Runnable {
    int portNumber;
    ServerSocket serverSoc = null;
    ArrayList<socketManager> clients = null;

    ServerHandler(int port) {
        portNumber = port;
    }

    public void run() {
        try {
            //Setup the socket for communication
            serverSoc = new ServerSocket(portNumber);
            clients = new ArrayList<socketManager>();
            //Infinite Loop
            while (true) {

                //accept incoming communication
                System.out.println("Waiting for client");
                //listening for client
                Socket soc = serverSoc.accept();
                System.out.println("220 localhost:" + serverSoc.getLocalPort() + " Simple Mail Transfer Service Ready");
                //Now creates new socketManager object
                socketManager temp = new socketManager(soc);
                //socketManager is added to the array list
                clients.add(temp);

                //create a new thread for the connection and start it.
                ServerConnetionHandler sch = new ServerConnetionHandler(clients, temp);
                Thread schThread = new Thread(sch);
                schThread.start();
            }

        } catch (Exception except) {
            //Exception thrown (except) when something went wrong, pushing message to the console
            System.err.println("Error --> " + except.getMessage());
        }


    }

    //Method to shut down server
    public void kill() {
        System.out.println("Server shutting");
        try {
            serverSoc.close();
            for (socketManager client : clients) {
                client.close();
            }
        } catch (Exception except) {
            //Exception thrown (except) when something went wrong, pushing message to the console
            System.err.println("Error --> " + except.getMessage());
        }
    }
}

class ServerConnetionHandler implements Runnable {
    socketManager selfs = null;
    ArrayList<socketManager> clients = null;
    boolean verbose = false;
    State currentState = State.NONE;
    State previousState = State.NONE;


    public ServerConnetionHandler(ArrayList<socketManager> l, socketManager inSoc) {
        selfs = inSoc;
        clients = l;
    }

    public ServerConnetionHandler(ArrayList<socketManager> l, socketManager inSoc, boolean v) {
        selfs = inSoc;
        clients = l;
        verbose = v;
    }

    public void run() {
        try {
            // Connection has been made with the client
            selfs.output.writeUTF("220 localhost:" + selfs.ip() + ":" + selfs.port() + " Simple Mail Transfer Service Ready");
            // Check if client wants to login or sign-up
            boolean check = false;
            while (!check) {
                String option = selfs.input.readUTF();
                if (option.equals("signup")) {
                    selfs.output.writeUTF("Enter your name");
                    String name = selfs.input.readUTF();
                    selfs.output.writeUTF("Enter your username");
                    String username = selfs.input.readUTF();
                    selfs.output.writeUTF("Enter your password");
                    String pw = selfs.input.readUTF();
                    AddUsers addUsers = new AddUsers(name, username, pw);
                    Boolean addCheck = addUsers.add();
                    if (addCheck) {
                        selfs.output.writeUTF("Success in signing up " + name);
                    } else {
                        selfs.output.writeUTF("There was an error adding you to the system. The username may be taken. Please try again");
                    }
                } else if (option.contains("login")) {
                    System.out.println("login");
                } else {
                    System.out.println("fail");
                }

            }


            String heloCheck = selfs.input.readUTF();
            if (heloCheck.contains("HELO")) {
                boolean check2 = false;
                while (!check2) {
                    selfs.output.writeUTF("start");
                    String message = selfs.input.readUTF();
                    String[] actionCheck = message.split(":");
                    String action = actionCheck[0];
                    //SMTP commands using switch case


                }
                //close the stream once we are done with it

            } else {
                //this is hello error
            }
        } catch (Exception except) {
            //Exception thrown (except) when something went wrong, pushing message to the console
            System.out.println("Error in ServerHandler--> " + except.getMessage());
        }
    }



}
