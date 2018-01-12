import java.net.*;
import java.util.*;
import java.io.*;

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
            Boolean loggedIn = false;
            selfs.output.writeUTF("220 localhost:" + selfs.ip() + ":" + selfs.port() + " Simple Mail Transfer Service Ready");
            // Check if client wants to login or sign-up
            String username;
            String name;
            String pw;
            String pwAuth;
            String mainChoice;
            boolean authCheck = false;

            // This deals with login and signup process
            while (!authCheck) {
                String option = selfs.input.readUTF();
                switch(option){
                    case "signup":
                        selfs.output.writeUTF("Enter your name");
                        name = selfs.input.readUTF();
                        selfs.output.writeUTF("Enter your username");
                        username = selfs.input.readUTF();
                        selfs.output.writeUTF("Enter your password");
                        pw = selfs.input.readUTF();
                        DB_AddUsers addUsers = new DB_AddUsers(name, username, pw);
                        Boolean addCheck = addUsers.add();
                        if (addCheck) {
                            selfs.output.writeUTF("Success in signing up " + name);
                            int length = selfs.input.readInt();                    // read length of incoming message
                            if(length>0) {
                                byte[] pkByte = new byte[length];
                                selfs.input.readFully(pkByte, 0, pkByte.length); // read the message
                                System.out.println(Arrays.toString(pkByte));
                                DB_UpdatePK uPK = new DB_UpdatePK(username,pkByte);
                                loggedIn = uPK.updatepk();
                                if(loggedIn){
                                    System.out.println("The client has logged in!!");
                                    selfs.output.writeUTF("You are now logged in!");
                                    authCheck = true;
                                }
                            }
                        } else {
                            selfs.output.writeUTF("There was an error adding you to the system. The username may be taken. Please try again");
                        }
                        break;

                    case "login":
                        selfs.output.writeUTF("Enter your username");
                        username = selfs.input.readUTF();
                        selfs.output.writeUTF("Enter your password");
                        pw = selfs.input.readUTF();
                        DB_CheckPassword pwCheck = new DB_CheckPassword(username,pw);
                        pwAuth=pwCheck.checkPW();
                        switch(pwAuth){
                            case "no_user":
                                System.out.println("No User In Database");
                                selfs.output.writeUTF("The username does not exists. Please try entering your details again");
                                break;
                            case "password_ok":
                                System.out.println("Password has matched");
                                loggedIn = true;
                                selfs.output.writeUTF("You are now logged in!");
                                authCheck = true;
                                break;
                            case "error":
                                System.out.println("Error. Please try Again.");
                                selfs.output.writeUTF("There has been an error. This may be with our database. Please try entering your details again");
                                break;
                            case "wrong_password":
                                System.out.println("Wrong password has been entered.");
                                selfs.output.writeUTF("The wrong password has been entered. Please try entering your details again");
                                break;

                        }
                        break;
                }


            }

            while(loggedIn){

                mainChoice = selfs.input.readUTF();
                switch(mainChoice){
                    case "COMPOSE":
                        ObjectOutputStream oos = new ObjectOutputStream(selfs.soc.getOutputStream());
                        DB_get_all_users getUsers = new DB_get_all_users();
                        List<HashMap<String,byte[]>> usernamesList = getUsers.getUsers();
                        oos.writeObject(usernamesList);

                        // read incoming message

                        InputStream is = selfs.soc.getInputStream();
                        ObjectInputStream ois = new ObjectInputStream(is);
                        HashMap<String,String> incomingMessage = (HashMap<String,String>) ois.readObject();
                        String message_mail = incomingMessage.get("Mail From");
                        String message_rcpt = incomingMessage.get("Rcpt to");
                        String message_subject = incomingMessage.get("Subject");
                        String message_body = incomingMessage.get("Body");
                        String message_sign = incomingMessage.get("DigitalSignature");
                        DB_AddMessages newMsg = new DB_AddMessages(message_rcpt,message_mail,message_subject,message_body,message_sign);

                        boolean messageSuccess = newMsg.addMessage();

                        if (messageSuccess){
                            System.out.println("Message has been added");
                            selfs.output.writeUTF("Your message has been added and encrypted.");


                        } else{
                            System.out.println("Message has been failed");
                            selfs.output.writeUTF("Your message has failed. Please try again");
                        }

                        break;
                    case "INBOX":
                        System.out.println("writing mail");
                        break;
                    case "DELETEALL":
                        System.out.println("writing mail");
                        break;
                    case "LOGOUT":
                        System.out.println("writing mail");
                        loggedIn=false;
                        break;

                    default:
                        System.out.println("That was not an option. Please choose an action again");
                        break;

                }



            }


            String heloCheck = selfs.input.readUTF();
//            if (heloCheck.contains("HELO")) {
//                boolean check2 = false;
//                while (!check2) {
//                    selfs.output.writeUTF("start");
//                    String message = selfs.input.readUTF();
//                    String[] actionCheck = message.split(":");
//                    String action = actionCheck[0];
//                    //SMTP commands using switch case
//
//
//                }
//                //close the stream once we are done with it
//
//            } else {
//                //this is hello error
//            }



        } catch (Exception except) {
            //Exception thrown (except) when something went wrong, pushing message to the console
            System.out.println("Error in ServerHandler--> " + except.getMessage());
        }
    }



}
