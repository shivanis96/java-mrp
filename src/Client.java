import java.io.*;
import java.net.*;
import java.util.*;
import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Client {

    //Main Method:- called when running the class file.
    public static void main(String[] args) {
        System.out.println("Add a port number:");
        Scanner scan = new Scanner(System.in);
        int port = scan.nextInt();

        //Portnumber:- number of the port we wish to connect on.
        int portNumber = 15882;
        //ServerIP:- IP address of the server.
        String serverIP = "localhost";

        if (port > 2048) {
            portNumber = port;
        } else {
            System.err.println("Port number too low, defaulting to 15882");
        }

        try {
            //Create a new socket for communication
            Socket soc = new Socket(serverIP, portNumber);

            // create new instance of the client writer thread, intialise it and start it running
//            ClientReader clientRead = new ClientReader(soc);
//            Thread clientReadThread = new Thread(clientRead);
//
//            clientReadThread.start();

            // create new instance of the client writer thread, intialise it and start it running
            ClientWriter clientWrite = new ClientWriter(soc);
            Thread clientWriteThread = new Thread(clientWrite);
            clientWriteThread.start();


        } catch (Exception except) {
            //Exception thrown (except) when something went wrong, pushing message to the console
            System.out.println("Error --> " + except.getMessage());
        }
    }
}

//This thread is responcible for writing messages
class ClientWriter implements Runnable {
    Socket cwSocket = null;
    DataOutputStream dataOut = null;
    DataInputStream serverStream = null;



    public ClientWriter(Socket outputSoc) {
        cwSocket = outputSoc;

    }

    public void run() {
        Random rand = new Random();
//    	int start = rand.nextInt(cwSocket.getLocalPort());


        try {
            //Create the streams to send data through
            DataOutputStream dataOut = new DataOutputStream(cwSocket.getOutputStream());
            DataInputStream serverStream = new DataInputStream(cwSocket.getInputStream());
            Scanner sc = new Scanner(System.in);

            System.out.println("Client writer running");
            //Read hat comes in from server and splits all the words with a space
            String[] successMessage = serverStream.readUTF().split(" ");
            //Checks first postion equals 220
            if (successMessage[0].equals("220")) {
                System.out.println("Do you want to 1. Sign up and login or 2. Login? 3.QUIT? Please choose a number");
                int input = sc.nextInt();
                sc.nextLine();
                boolean authCheck = false;
                boolean loggedIn = false;
                String username=null;
                String name;
                String password;
                String hashedPW;
                Hashing hash;
                RSAwithDigitalMessage RSAE = new RSAwithDigitalMessage();
                String message;


                while (!authCheck) {
                    switch (input) {
                        case 1:
                            dataOut.writeUTF("signup");
                            // Sign up
                            message = serverStream.readUTF();
                            System.out.println(message);

                            name = sc.nextLine();
                            dataOut.writeUTF(name);
                            message = serverStream.readUTF();
                            System.out.println(message);
                            username = sc.nextLine();
                            dataOut.writeUTF(username);
                            message = serverStream.readUTF();
                            System.out.println(message);
                            password = sc.nextLine();
                            hash = new Hashing(password);
                            hashedPW = hash.getHashed();
                            dataOut.writeUTF(hashedPW);
                            message = serverStream.readUTF();
                            System.out.println(message);
                            if (message.contains("Success in signing up")){
                               System.out.println("Now creating your keys");
                               KeyPair pair = RSAE.generateKeyPair();
                               RSAE.SaveKeyPair(pair,username);
                               PublicKey pubKey = pair.getPublic();
                               byte[] bytes = pubKey.getEncoded();
                               System.out.println(Arrays.toString(bytes));
                               dataOut.writeInt(bytes.length);
                               dataOut.write(bytes);
                               message = serverStream.readUTF();
                               System.out.println(message);
                               if(message.equals("You are now logged in!")){
                                   authCheck = true;
                                   loggedIn = true;
                               }
                            }
                            break;
                        case 2:
                            // Login;
                            dataOut.writeUTF("login");
                            message = serverStream.readUTF();
                            System.out.println(message);
                            username = sc.nextLine();
                            dataOut.writeUTF(username);
                            message = serverStream.readUTF();
                            System.out.println(message);
                            password = sc.nextLine();
                            hash = new Hashing(password);
                            hashedPW = hash.getHashed();
                            // We do not want the password stored
                            password = null;
                            dataOut.writeUTF(hashedPW);
                            message = serverStream.readUTF();
                            System.out.println(message);
                            if(message.equals("You are now logged in!")){
                                authCheck = true;
                                loggedIn = true;
                            }
                            break;
                        case 3:
                            // quit;
                            authCheck = true;
                            break;
                        default:
                            System.out.println("That is not a vaid number.Do you want to 1. Sign up or 2. Sign in? 3. quit;Please choose a number");
                            input = sc.nextInt();
                            break;
                    }

                }

                while(loggedIn){
                    KeyPair myKP= RSAE.LoadKeyPair(username);
                    System.out.println("What do you want to do");
                    // TODO: Add instructions here

                    String mainChoice = sc.nextLine().toUpperCase();

                    switch(mainChoice){
                        case "COMPOSE":
                            dataOut.writeUTF("COMPOSE");
                            InputStream is = cwSocket.getInputStream();
                            ObjectInputStream ois = new ObjectInputStream(is);
                            List<HashMap<String,byte[]>> myMessageArray = (List<HashMap<String,byte[]>>) ois.readObject();
                            System.out.println("Please choose the number of the recipient you want to send to:");
                            for (HashMap<String,byte[]> s : myMessageArray) {
                                for (String key : myMessageArray.get(myMessageArray.indexOf(s)).keySet()){
                                    System.out.println("User " + myMessageArray.indexOf(s)+ ": " + key);
                                }
                            }
                            int index = sc.nextInt();
                            sc.nextLine();
                            HashMap<String,byte[]> rcptTo;
                            String subject;
                            String body;
                            String DS;

                            rcptTo = myMessageArray.get(index);
                            System.out.println("We are now writing to: " + rcptTo);
                            System.out.println("What is the subject of your message?");
                            subject = sc.nextLine();
                            System.out.println("What is the body of your message?");
                            body = sc.nextLine();
                            System.out.println("Encrypting...");



                            break;
                        case "INBOX":
                            System.out.println("writing mail");
                            break;
                        case "DELETEALL":
                            System.out.println("writing mail");
                            break;
                        case "LOGOUT":
                            System.out.println("writing mail");
                            break;
                        default:
                            System.out.println("That was not an option. Please choose an action again");
                            break;
                    }








                }


            }

//            System.out.println("Sending HELO message");
//            dataOut.writeUTF("HELO " + cwSocket.getInetAddress().getHostAddress() + ":" + cwSocket.getPort());
//
//            System.out.println("Enter QUIT to close the connection");
//            dataOut.writeUTF("QUIT");


        } catch (Exception except) {
            //Exception thrown (except) when something went wrong, pushing message to the console
            System.out.println("Error in Writer--> " + except.getMessage());
        }


    }
}

